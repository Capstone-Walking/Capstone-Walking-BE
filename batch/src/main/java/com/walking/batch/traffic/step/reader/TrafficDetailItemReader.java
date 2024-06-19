package com.walking.batch.traffic.step.reader;

import com.walking.batch.traffic.service.TrafficColorApiServiceV2;
import com.walking.batch.traffic.service.TrafficTimeLeftApiServiceV2;
import com.walking.batch.traffic.service.dto.TrafficColorVO;
import com.walking.batch.traffic.service.dto.TrafficTimeLeftVO;
import com.walking.batch.traffic.step.dto.TrafficDetailDto;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@RequiredArgsConstructor
@Slf4j
public class TrafficDetailItemReader implements ItemReader<TrafficDetailDto> {

	private final TrafficColorApiServiceV2 trafficColorClient;
	private final TrafficTimeLeftApiServiceV2 trafficTimeLeftClient;
	private Iterator<TrafficDetailDto> iterator;

	@Override
	public TrafficDetailDto read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (Objects.isNull(iterator)) {
			Iterable<TrafficTimeLeftVO> timeLeftResponses = trafficTimeLeftClient.request();
			Iterable<TrafficColorVO> colorResponses = trafficColorClient.request();

			Map<Long, TrafficTimeLeftVO> timeLeftResponseMap = new HashMap<>();
			for (TrafficTimeLeftVO timeLeftResponseDto : timeLeftResponses) {
				Long itstId = timeLeftResponseDto.getItstId();
				timeLeftResponseMap.compute(
						itstId,
						(id, existingDto) -> {
							if (existingDto == null) {
								// 맵에 해당 키가 없으면 현재 값을 저장
								return timeLeftResponseDto;
							} else {
								// 맵에 해당 키가 있으면 regDt를 비교하여 업데이트할지 결정
								if (existingDto.getRegDt().compareTo(timeLeftResponseDto.getRegDt()) <= 0) {
									return timeLeftResponseDto; // 새로운 값으로 업데이트
								} else {
									return existingDto; // 기존 값 유지
								}
							}
						});
			}
			log.debug("중복을 제거한 timeLeftResponse 데이터의 수량은 " + timeLeftResponseMap.size());

			Map<Long, TrafficColorVO> colorResponseMap = new HashMap<>();
			for (TrafficColorVO colorResponseDto : colorResponses) {
				Long itstId = colorResponseDto.getItstId();
				colorResponseMap.compute(
						itstId,
						(id, existingDto) -> {
							if (existingDto == null) {
								// 맵에 해당 키가 없으면 현재 값을 저장
								return colorResponseDto;
							} else {
								// 맵에 해당 키가 있으면 regDt를 비교하여 업데이트할지 결정
								if (existingDto.getRegDt().compareTo(colorResponseDto.getRegDt()) <= 0) {
									return colorResponseDto; // 새로운 값으로 업데이트
								} else {
									return existingDto; // 기존 값 유지
								}
							}
						});
			}
			log.debug("중복을 제거한 colorResponse 데이터의 수량은 " + colorResponseMap.size());

			Set<Long> itstIds = getDistinctItstIds(colorResponseMap, timeLeftResponseMap);
			List<TrafficDetailDto> trafficDetailDtos = new ArrayList<>();
			for (Long itstId : itstIds) {
				if (isAllPresent(itstId, colorResponseMap, timeLeftResponseMap)) {
					trafficDetailDtos.addAll(
							getTrafficDetailDtos(itstId, colorResponseMap, timeLeftResponseMap));
				}
			}
			iterator = trafficDetailDtos.iterator();
		}
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			iterator = null;
			return null;
		}
	}

	private List<TrafficDetailDto> getTrafficDetailDtos(
			Long itstId,
			Map<Long, TrafficColorVO> colorResponseMap,
			Map<Long, TrafficTimeLeftVO> timeLeftResponseMap) {
		List<TrafficDetailDto> detailDtos = new ArrayList<>();
		List.of("nt", "et", "st", "wt", "ne", "se", "sw", "nw")
				.forEach(
						direction ->
								generateTrafficDetailDto(
												itstId,
												colorResponseMap.get(itstId).getRegDt(),
												timeLeftResponseMap.get(itstId).getRegDt(),
												colorResponseMap.get(itstId).getNtPdsgStatNm(),
												timeLeftResponseMap.get(itstId).getNtPdsgRmdrCs(),
												direction)
										.ifPresent(detailDtos::add));
		return detailDtos;
	}

	private Optional<TrafficDetailDto> generateTrafficDetailDto(
			Long itstId,
			OffsetDateTime colorRegDt,
			OffsetDateTime timeLeftRegDt,
			String pdsgStatNm,
			Float pdsgRmdrCs,
			String direction) {
		if (pdsgStatNm == null || pdsgRmdrCs == null) {
			return Optional.empty();
		}
		return Optional.of(
				TrafficDetailDto.builder()
						.itstId(itstId)
						.direction(direction)
						.color(convertToColor(itstId, pdsgStatNm))
						.timeLeft(pdsgRmdrCs)
						.colorRegDt(colorRegDt)
						.timeLeftRegDt(timeLeftRegDt)
						.build());
	}

	private String convertToColor(Long itstId, String pdsgStatNm) {
		if (pdsgStatNm.equals("stop-And-Remain")) {
			return "RED";
		}
		if (pdsgStatNm.equals("protected-Movement-Allowed")
				|| pdsgStatNm.equals("permissive-Movement-Allowed")
				|| pdsgStatNm.equals("permissive-clearance")) {
			return "GREEN";
		}
		if (pdsgStatNm.equals("dark") || pdsgStatNm.equals("protected-clearance")) {
			return "DARK";
		}
		throw new IllegalArgumentException(
				"itstId " + itstId + "의 전달 값 [" + pdsgStatNm + "]은(는) 잘못된 타입 입니다.");
	}

	/**
	 * 신호 정보, 잔여시간 정보에서 모두 존재하는 교차로 인지 판별합니다.
	 *
	 * @param itstId 교차로 아이디
	 * @param colorResponseMap 신호 정보 응답 데이터
	 * @param timeLeftResponseMap 잔여시간 정보 응답 데이터
	 * @return 두 응답에서 모두 존재하면 true 그렇지 않으면 false를 반환합니다.
	 */
	private boolean isAllPresent(
			Long itstId,
			Map<Long, TrafficColorVO> colorResponseMap,
			Map<Long, TrafficTimeLeftVO> timeLeftResponseMap) {
		return colorResponseMap.containsKey(itstId) && timeLeftResponseMap.containsKey(itstId);
	}

	/**
	 * 중복을 제거한 교차로 아이디 중 더 적은 수량을 가진 Map의 KeySet을 반환합니다.
	 *
	 * @param colorResponseMap
	 * @param timeLeftResponseMap
	 * @return KeySet
	 */
	private static Set<Long> getDistinctItstIds(
			Map<Long, TrafficColorVO> colorResponseMap,
			Map<Long, TrafficTimeLeftVO> timeLeftResponseMap) {
		return colorResponseMap.size() < timeLeftResponseMap.size()
				? colorResponseMap.keySet()
				: timeLeftResponseMap.keySet();
	}
}
