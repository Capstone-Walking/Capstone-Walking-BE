package com.walking.batch.chunk.reader;

import com.walking.batch.chunk.dto.TrafficApiDetailDto;
import com.walking.batch.chunk.dto.TrafficDetailDto;
import com.walking.batch.service.TrafficColorApiServiceV2;
import com.walking.batch.service.TrafficTimeLeftApiServiceV2;
import com.walking.batch.service.dto.TrafficColorResponseDto;
import com.walking.batch.service.dto.TrafficTimeLeftResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
			Iterable<TrafficTimeLeftResponseDto> timeLeftResponses = trafficTimeLeftClient.request();
			Iterable<TrafficColorResponseDto> colorResponses = trafficColorClient.request();

			Map<Long, TrafficTimeLeftResponseDto> timeLeftResponseMap = new HashMap<>();
			for (TrafficTimeLeftResponseDto timeLeftResponseDto : timeLeftResponses) {
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

			Map<Long, TrafficColorResponseDto> colorResponseMap = new HashMap<>();
			for (TrafficColorResponseDto colorResponseDto : colorResponses) {
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
					TrafficApiDetailDto apiDto =
							createTrafficApiDetailDto(itstId, colorResponseMap, timeLeftResponseMap);
					trafficDetailDtos.addAll(apiDto.getTrafficDetailDtos());
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

	/**
	 * 특정 교차로에 대한 신호 정보, 잔여시간 정보를 동시에 반영한 DTO를 생성합니다.
	 *
	 * @param itstId 교차로 아이디
	 * @param colorResponseMap 신호 정보 응답
	 * @param timeLeftResponseMap 잔여시간 응답
	 * @return 신호 정보, 잔여시간 정보를 동시에 반영한 DTO
	 */
	private TrafficApiDetailDto createTrafficApiDetailDto(
			Long itstId,
			Map<Long, TrafficColorResponseDto> colorResponseMap,
			Map<Long, TrafficTimeLeftResponseDto> timeLeftResponseMap) {
		return TrafficApiDetailDto.builder()
				.itstId(itstId)
				.ntPdsgStatNm(colorResponseMap.get(itstId).getNtPdsgStatNm())
				.etPdsgStatNm(colorResponseMap.get(itstId).getEtPdsgStatNm())
				.stPdsgStatNm(colorResponseMap.get(itstId).getStPdsgStatNm())
				.wtPdsgStatNm(colorResponseMap.get(itstId).getWtPdsgStatNm())
				.nePdsgStatNm(colorResponseMap.get(itstId).getNePdsgStatNm())
				.sePdsgStatNm(colorResponseMap.get(itstId).getSePdsgStatNm())
				.swPdsgStatNm(colorResponseMap.get(itstId).getSwPdsgStatNm())
				.nwPdsgStatNm(colorResponseMap.get(itstId).getNwPdsgStatNm())
				.ntPdsgRmdrCs(timeLeftResponseMap.get(itstId).getNtPdsgRmdrCs())
				.etPdsgRmdrCs(timeLeftResponseMap.get(itstId).getEtPdsgRmdrCs())
				.stPdsgRmdrCs(timeLeftResponseMap.get(itstId).getStPdsgRmdrCs())
				.wtPdsgRmdrCs(timeLeftResponseMap.get(itstId).getWtPdsgRmdrCs())
				.nePdsgRmdrCs(timeLeftResponseMap.get(itstId).getNePdsgRmdrCs())
				.sePdsgRmdrCs(timeLeftResponseMap.get(itstId).getSePdsgRmdrCs())
				.swPdsgRmdrCs(timeLeftResponseMap.get(itstId).getSwPdsgRmdrCs())
				.nwPdsgRmdrCs(timeLeftResponseMap.get(itstId).getNwPdsgRmdrCs())
				.colorRegDt(colorResponseMap.get(itstId).getRegDt())
				.timeLeftRegDt(timeLeftResponseMap.get(itstId).getRegDt())
				.build();
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
			Map<Long, TrafficColorResponseDto> colorResponseMap,
			Map<Long, TrafficTimeLeftResponseDto> timeLeftResponseMap) {
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
			Map<Long, TrafficColorResponseDto> colorResponseMap,
			Map<Long, TrafficTimeLeftResponseDto> timeLeftResponseMap) {
		return colorResponseMap.size() < timeLeftResponseMap.size()
				? colorResponseMap.keySet()
				: timeLeftResponseMap.keySet();
	}
}
