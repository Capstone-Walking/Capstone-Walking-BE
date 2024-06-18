package com.walking.api.web.dto.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetailInfo;
import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrafficDetailConverter {

	/**
	 * PredictedData를 기반으로 TrafficDetail를 생성합니다.
	 *
	 * @param predictedTraffic 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터
	 * @return 예측 값을 바탕으로 만든 TrafficDetail
	 */
	public TrafficDetail execute(
			PredictedTraffic predictedTraffic, Optional<FavoriteTrafficDetail> favoriteTrafficDetail) {

		TrafficEntity trafficEntity = predictedTraffic.getTraffic();
		boolean isFavorite = false;
		String viewName = trafficEntity.getName();

		if (favoriteTrafficDetail.isPresent()
				&& favoriteTrafficDetail.get().getId().equals(trafficEntity.getId())) {
			isFavorite = true;
			viewName = favoriteTrafficDetail.get().getName();
		}

		return TrafficDetail.builder()
				.id(trafficEntity.getId())
				.color(predictedTraffic.getCurrentColorDescription())
				.timeLeft(predictedTraffic.getCurrentTimeLeft().orElse(null))
				.point(
						PointDetail.builder().lng(trafficEntity.getLng()).lat(trafficEntity.getLat()).build())
				.redCycle(predictedTraffic.getRedCycle().orElse(null))
				.greenCycle(predictedTraffic.getGreenCycle().orElse(null))
				.detail(convertToTrafficDetailInfo(trafficEntity))
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
	public List<TrafficDetail> execute(List<PredictedTraffic> predictedData) {

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
										.detail(convertToTrafficDetailInfo(predictedDatum.getTraffic()))
										.isFavorite(false)
										.viewName(predictedDatum.getTraffic().getName())
										.build())
				.collect(Collectors.toList());
	}

	/**
	 * api.traffic_detail 의 detail 값(JSON)을 파싱하여 TrafficDetailInfo 로 변환합니다.
	 *
	 * @param trafficEntity
	 * @return
	 */
	private static TrafficDetailInfo convertToTrafficDetailInfo(TrafficEntity trafficEntity) {
		ObjectMapper objectMapper = new ObjectMapper();
		TrafficDetailInfo trafficDetailInfo =
				TrafficDetailInfo.builder().trafficId(-1L).apiSource("ERROR").direction("ERROR").build();
		try {
			trafficDetailInfo =
					objectMapper.readValue(trafficEntity.getDetail(), TrafficDetailInfo.class);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Convert to TrafficDetailInfo fail", e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Convert to TrafficDetailInfo fail", e);
		}

		return trafficDetailInfo;
	}
}
