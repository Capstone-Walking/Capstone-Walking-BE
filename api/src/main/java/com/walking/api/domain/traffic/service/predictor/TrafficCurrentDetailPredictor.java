package com.walking.api.domain.traffic.service.predictor;

import com.walking.api.domain.traffic.service.dto.CurrentDetailsResponse;
import com.walking.api.domain.traffic.service.model.PredictedData;
import com.walking.api.repository.dao.traffic.TrafficDetailRepository;
import com.walking.api.util.OffsetDateTimeCalculator;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import com.walking.data.entity.traffic.constant.TrafficColor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
public class TrafficCurrentDetailPredictor {

	private final TrafficDetailRepository trafficDetailRepository;

	@Transactional(readOnly = true)
	public CurrentDetailsResponse execute(
			TrafficCyclePredictor trafficCyclePredictor, List<Long> trafficIds) {
		return doExecute(trafficCyclePredictor.execute(trafficIds));
	}

	CurrentDetailsResponse doExecute(Map<Long, PredictedData> predictedMap) {
		Set<Long> trafficIdSet = predictedMap.keySet();
		List<Long> trafficIds = new ArrayList<>(trafficIdSet);

		List<TrafficDetailEntity> topTrafficDetails =
				trafficDetailRepository.findAllTopDataInTrafficIds(trafficIds);
		Map<Long, TrafficDetailEntity> mappedTrafficDetail =
				mappedTrafficDetailByTrafficId(topTrafficDetails);

		OffsetDateTime now = OffsetDateTime.now();
		for (Long id : trafficIds) {
			PredictedData predictedData = predictedMap.get(id);
			if (!predictedData.isPredictCycleSuccessful()) {
				continue;
			}

			TrafficDetailEntity trafficDetail = mappedTrafficDetail.get(id);
			Float timeLeft = trafficDetail.getTimeLeft();
			float differenceInSeconds =
					OffsetDateTimeCalculator.getDifferenceInSeconds(trafficDetail.getTimeLeftRegDt(), now);
			TrafficColor currentColor = trafficDetail.getColor();
			updatePredictData(predictedData, currentColor, timeLeft, differenceInSeconds);
		}
		return CurrentDetailsResponse.builder().currentDetails(predictedMap).build();
	}

	private Map<Long, TrafficDetailEntity> mappedTrafficDetailByTrafficId(
			List<TrafficDetailEntity> mostRecentlyData) {
		return mostRecentlyData.stream()
				.collect(
						Collectors.toMap(
								trafficDetailEntity -> trafficDetailEntity.getTraffic().getId(),
								trafficDetailEntity -> trafficDetailEntity));
	}

	/** 하나의 신호등에 대하여 현재 신호 색상과 잔여시간을 예측하는 작업을 수행 */
	private void updatePredictData(
			PredictedData predictedData,
			TrafficColor currentColor,
			float timeLeft,
			float differenceInSeconds) {
		differenceInSeconds = differenceInSeconds - timeLeft;
		while (differenceInSeconds >= 0) {
			if (currentColor.isRed()) {
				currentColor = TrafficColor.GREEN;
				Float cycleOfNextColor = predictedData.getCycleByColor(TrafficColor.GREEN);
				differenceInSeconds -= cycleOfNextColor;
			} else {
				currentColor = TrafficColor.RED;
				Float cycleOfNextColor = predictedData.getCycleByColor(TrafficColor.RED);
				differenceInSeconds -= cycleOfNextColor;
			}
		}
		predictedData.updateCurrentColor(currentColor);
		predictedData.updateCurrentTimeLeft(Math.abs(differenceInSeconds));
	}
}
