package com.walking.api.converter;

import com.walking.api.domain.traffic.service.model.PredictedData;
import com.walking.api.web.dto.response.detail.FavoriteTrafficDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TrafficDetailConverter {

	private TrafficDetailConverter() {}

	/**
	 * PredictedData를 기반으로 TrafficDetail를 생성합니다.
	 *
	 * @param predictedData 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터
	 * @return 예측 값을 바탕으로 만든 TrafficDetail
	 */
	public static TrafficDetail execute(
			PredictedData predictedData, Optional<FavoriteTrafficDetail> favoriteTrafficDetail) {

		TrafficEntity trafficEntity = predictedData.getTraffic();
		boolean isFavorite = false;
		String viewName = trafficEntity.getName();

		if (favoriteTrafficDetail.isPresent()
				&& favoriteTrafficDetail.get().getId().equals(trafficEntity.getId())) {
			isFavorite = true;
			viewName = favoriteTrafficDetail.get().getName();
		}

		return TrafficDetail.builder()
				.id(trafficEntity.getId())
				.color(predictedData.getCurrentColorDescription())
				.timeLeft(predictedData.getCurrentTimeLeft().orElse(null))
				.point(
						PointDetail.builder().lng(trafficEntity.getLng()).lat(trafficEntity.getLat()).build())
				.redCycle(predictedData.getRedCycle().orElse(null))
				.greenCycle(predictedData.getGreenCycle().orElse(null))
				.detail(TrafficDetailInfoConverter.execute(trafficEntity))
				.isFavorite(isFavorite)
				.viewName(viewName)
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
										.color(predictedDatum.getCurrentColorDescription())
										.timeLeft(predictedDatum.getCurrentTimeLeft().orElse(null))
										.point(
												PointDetail.builder()
														.lng(predictedDatum.getTraffic().getLng())
														.lat(predictedDatum.getTraffic().getLat())
														.build())
										.redCycle(predictedDatum.getRedCycle().orElse(null))
										.greenCycle(predictedDatum.getGreenCycle().orElse(null))
										.detail(TrafficDetailInfoConverter.execute(predictedDatum.getTraffic()))
										.isFavorite(false)
										.viewName(predictedDatum.getTraffic().getName())
										.build())
				.collect(Collectors.toList());
	}
}
