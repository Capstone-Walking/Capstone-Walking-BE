package com.walking.api.service;

import com.walking.api.repository.traffic.TrafficDetailRepository;
import com.walking.api.service.dto.ColorAndTimeLeft;
import com.walking.api.service.dto.PredictedData;
import com.walking.api.service.dto.request.CurrentDetailRequestDto;
import com.walking.api.service.dto.response.CurrentDetailResponseDto;
import com.walking.api.util.OffsetDateTimeCalculator;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import com.walking.data.entity.traffic.constant.TrafficColor;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficCurrentDetailPredictService {

	private final TrafficDetailRepository trafficDetailRepository;

	/**
	 * 예측한 사이클 정보를 바탕으로 현재 신호 색상과 잔여 시간을 계산하여 반환합니다.
	 *
	 * @param dto 사이클까지 예측된 데이터를 담은 DTO
	 * @return 전달 받은 DTO에 존재하는 map에 현재 신호 색상과 잔여 시간 데이터를 마저 채워 반환합니다.
	 */
	@Transactional(readOnly = true)
	public CurrentDetailResponseDto execute(CurrentDetailRequestDto dto) {
		Map<TrafficEntity, PredictedData> predictedMap = dto.getPredictedCycleMap();
		Set<TrafficEntity> traffics = predictedMap.keySet();
		List<Long> trafficIds =
				traffics.stream().map(TrafficEntity::getId).collect(Collectors.toList());

		List<TrafficDetailEntity> mostRecenlyData =
				trafficDetailRepository.findMostRecenlyData(trafficIds);
		for (TrafficDetailEntity mostRecenlyDatum : mostRecenlyData) {
			log.debug(
					mostRecenlyDatum.getTraffic().getId()
							+ "의 최근 데이터는 ("
							+ mostRecenlyDatum.getColor()
							+ ", "
							+ mostRecenlyDatum.getTimeLeft()
							+ ", "
							+ mostRecenlyDatum.getTimeLeftRegDt()
							+ ")");
		}
		Map<TrafficEntity, TrafficDetailEntity> separatedData = separateByTraffic(mostRecenlyData);

		Map<TrafficEntity, ColorAndTimeLeft> currentDetails = new HashMap<>();
		OffsetDateTime now = OffsetDateTime.now();
		for (TrafficEntity traffic : traffics) {
			PredictedData predictedData = predictedMap.get(traffic);
			if (isUnpredictedData(predictedData)) { // 사이클 예측이 완료되지 않은 데이터
				continue;
			}

			TrafficDetailEntity currentTrafficDetailEntity = separatedData.get(traffic);
			float differenceInSeconds =
					OffsetDateTimeCalculator.getDifferenceInSeconds(
							currentTrafficDetailEntity.getTimeLeftRegDt(), now);

			ColorAndTimeLeft currentColorAndtimeLeft =
					predictCurrentColorAndTimeLeft(
							traffic, currentTrafficDetailEntity, differenceInSeconds, predictedData);

			currentDetails.put(traffic, currentColorAndtimeLeft);
			predictedData.updateCurrentColor(currentColorAndtimeLeft.getTrafficColor());
			predictedData.updateCurrentTimeLeft(currentColorAndtimeLeft.getTimeLeft());
		}
		return CurrentDetailResponseDto.builder().currentDetails(predictedMap).build();
	}

	/**
	 * 사이클 예측 과정에서 사이클을 계산하는데 성공한 데이터인지 검증
	 *
	 * @param predictedData
	 * @return 예측이 실패한 데이터면 true, 아니면 false
	 */
	private static boolean isUnpredictedData(PredictedData predictedData) {
		return !predictedData.isPredictCycleSuccessful();
	}

	/**
	 * 하나의 신호등에 대하여 현재 신호 색상과 잔여시간을 예측하는 작업을 수행
	 *
	 * @param traffic 예측할 신호등
	 * @param currentTrafficDetailEntity
	 * @param differenceInSeconds 예측을 해야하는 시간의 크기
	 * @param predictedData 예측에 필요한 사이클 정보
	 * @return 현재 신호 색상 및 잔여시간
	 */
	private static ColorAndTimeLeft predictCurrentColorAndTimeLeft(
			TrafficEntity traffic,
			TrafficDetailEntity currentTrafficDetailEntity,
			float differenceInSeconds,
			PredictedData predictedData) {
		ColorAndTimeLeft currentColorAndtimeLeft =
				new ColorAndTimeLeft(
						currentTrafficDetailEntity.getColor(), currentTrafficDetailEntity.getTimeLeft());
		TrafficColor currentColor = currentTrafficDetailEntity.getColor();

		// 현재 색상에 대한 잔여시간을 먼저 소진시켜본다.
		differenceInSeconds = differenceInSeconds - currentTrafficDetailEntity.getTimeLeft();

		log.debug("신호등 [" + traffic.getId() + "]의 현재 시간을 예측 하고 있습니다...");
		while (differenceInSeconds >= 0) {
			if (currentColor.isRed()) { // 최근 데이터의 색상이 red
				currentColor = TrafficColor.GREEN;
				Float cycleOfNextColor = predictedData.getCycleByColor(TrafficColor.GREEN);
				differenceInSeconds -= cycleOfNextColor;
			} else {
				currentColor = TrafficColor.RED;
				Float cycleOfNextColor = predictedData.getCycleByColor(TrafficColor.RED);
				differenceInSeconds -= cycleOfNextColor;
			}
		}

		currentColorAndtimeLeft.updateColor(currentColor);
		currentColorAndtimeLeft.updateTimeLeft(Math.abs(differenceInSeconds));
		return currentColorAndtimeLeft;
	}

	private Map<TrafficEntity, TrafficDetailEntity> separateByTraffic(
			List<TrafficDetailEntity> mostRecentlyData) {
		Map<TrafficEntity, TrafficDetailEntity> separatedData = new HashMap<>();

		for (TrafficDetailEntity recentlyDatum : mostRecentlyData) {
			separatedData.put(recentlyDatum.getTraffic(), recentlyDatum);
		}

		return separatedData;
	}
}
