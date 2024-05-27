package com.walking.api.converter;

import com.walking.api.service.dto.PredictedData;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.stream.Collectors;

public final class TrafficDetailConverter {

	private TrafficDetailConverter() {}

	/**
	 * PredictedData를 기반으로 TrafficDetail를 생성합니다.
	 *
	 * @param predictedData 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터
	 * @return 예측 값을 바탕으로 만든 TrafficDetail
	 */
	public static TrafficDetail execute(PredictedData predictedData) {

		TrafficEntity trafficEntity = predictedData.getTraffic();

		return TrafficDetail.builder()
				.id(trafficEntity.getId())
				.color(predictedData.getCurrentColor().toString())
				.timeLeft(predictedData.getCurrentTimeLeft())
				.point(
						PointDetail.builder().lng(trafficEntity.getLng()).lat(trafficEntity.getLat()).build())
				.redCycle(predictedData.getRedCycle())
				.greenCycle(predictedData.getGreenCycle())
				.detail(TrafficDetailInfoConverter.execute(trafficEntity))
				.isFavorite(false)
				.viewName(trafficEntity.getName())
				.build();
	}

	/**
	 * PredictedData를 기반으로 TrafficDetail의 List를 생성합니다.
	 *
	 * @param predictedData 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터 리스트
	 * @return 예측 값을 바탕으로 만든 TrafficDetail의 List
	 */
	public static List<TrafficDetail> execute(List<PredictedData> predictedData) {

		return predictedData.stream()
				.map(
						predictedDatum ->
								TrafficDetail.builder()
										.id(predictedDatum.getTraffic().getId())
										.color(predictedDatum.getCurrentColor().toString())
										.timeLeft(predictedDatum.getCurrentTimeLeft())
										.point(
												PointDetail.builder()
														.lng(predictedDatum.getTraffic().getLng())
														.lat(predictedDatum.getTraffic().getLat())
														.build())
										.redCycle(predictedDatum.getRedCycle())
										.greenCycle(predictedDatum.getGreenCycle())
										.detail(TrafficDetailInfoConverter.execute(predictedDatum.getTraffic()))
										.isFavorite(false)
										.viewName(predictedDatum.getTraffic().getName())
										.build())
				.collect(Collectors.toList());
	}
}
