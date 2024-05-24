package com.walking.api.service;

import com.walking.api.service.dto.PredictedData;
import com.walking.api.service.dto.request.CurrentDetailRequestDto;
import com.walking.api.service.dto.request.CyclePredictionRequestDto;
import com.walking.api.service.dto.request.IntegrationPredictRequestDto;
import com.walking.api.service.dto.response.CurrentDetailResponseDto;
import com.walking.api.service.dto.response.IntegrationPredictResponseDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 신호등의 현재 잔여 시간, 현재 색상, 각 색상별 사이클을 예측한 결과를 리턴합니다. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrafficIntegrationPredictService {

	private final TrafficCyclePredictServiceImpl trafficCyclePredictService;
	private final TrafficCurrentDetailPredictService trafficCurrentDetailPredictService;

	@Value("${walking.predict.dataInterval}")
	private int dataInterval;

	@Transactional(readOnly = true)
	public IntegrationPredictResponseDto execute(IntegrationPredictRequestDto requestDto) {
		List<Long> trafficIds = requestDto.getTrafficIds();
		CyclePredictionRequestDto cyclePredictionRequestDto =
				new CyclePredictionRequestDto(trafficIds, dataInterval);
		Map<Long, PredictedData> predictedCycleMap =
				trafficCyclePredictService.execute(cyclePredictionRequestDto);

		CurrentDetailRequestDto currentDetailRequestDto =
				CurrentDetailRequestDto.builder().predictedCycleMap(predictedCycleMap).build();
		CurrentDetailResponseDto currentDetailResponseDto =
				trafficCurrentDetailPredictService.execute(currentDetailRequestDto);

		// 사이클 예측값과 현재시간에 대한 예측값을 모아서 리턴하도록
		return IntegrationPredictResponseDto.builder()
				.predictedDataMap(currentDetailResponseDto.getCurrentDetails())
				.build();
	}
}
