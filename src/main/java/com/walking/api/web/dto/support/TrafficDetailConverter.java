package com.walking.api.web.dto.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.traffic.dto.detail.PointDetail;
import com.walking.api.traffic.dto.detail.TrafficDetail;
import com.walking.api.traffic.dto.detail.TrafficDetailInfo;
import com.walking.api.traffic.service.model.PredictTargetTraffic;
import com.walking.api.traffic.service.model.TargetTrafficVO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrafficDetailConverter {

	/**
	 * PredictedData를 기반으로 TrafficDetail를 생성합니다.
	 *
	 * @param predictTargetTraffic 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터
	 * @return 예측 값을 바탕으로 만든 TrafficDetail
	 */
	public TrafficDetail execute(
			PredictTargetTraffic predictTargetTraffic,
			Optional<FavoriteTrafficDetail> favoriteTrafficDetail) {

		TargetTrafficVO trafficEntity = predictTargetTraffic.getTraffic();
		boolean isFavorite = false;
		String viewName = trafficEntity.getName();

		if (favoriteTrafficDetail.isPresent()
				&& favoriteTrafficDetail.get().getId() == (trafficEntity.getId())) {
			isFavorite = true;
			viewName = favoriteTrafficDetail.get().getName();
		}

		return new TrafficDetail(
				trafficEntity.getId(),
				predictTargetTraffic.getCurrentColor().toString(),
				predictTargetTraffic.getCurrentTimeLeft(),
				new PointDetail(trafficEntity.getPoint().getY(), trafficEntity.getPoint().getX()),
				predictTargetTraffic.getRedCycle() == null ? 0 : predictTargetTraffic.getRedCycle(),
				predictTargetTraffic.getGreenCycle() == null ? 0 : predictTargetTraffic.getGreenCycle(),
				convertToTrafficDetailInfo(trafficEntity),
				isFavorite,
				viewName);
	}

	/**
	 * PredictedData를 기반으로 TrafficDetail의 List를 생성합니다.
	 *
	 * @param predictedData 사이클 정보 와 현재 색상 및 잔여시간을 예측한 데이터 리스트
	 * @return 예측 값을 바탕으로 만든 TrafficDetail의 List
	 */
	public List<TrafficDetail> execute(List<PredictTargetTraffic> predictedData) {
		return predictedData.stream()
				.map(
						predictedDatum ->
								new TrafficDetail(
										predictedDatum.getTraffic().getId(),
										predictedDatum.getCurrentColor().toString(),
										predictedDatum.getCurrentTimeLeft(),
										new PointDetail(
												predictedDatum.getTraffic().getPoint().getY(),
												predictedDatum.getTraffic().getPoint().getX()),
										predictedDatum.getRedCycle(),
										predictedDatum.getGreenCycle(),
										convertToTrafficDetailInfo(predictedDatum.getTraffic()),
										false,
										predictedDatum.getTraffic().getName()))
				.collect(Collectors.toList());
	}

	/**
	 * api.traffic_detail 의 detail 값(JSON)을 파싱하여 TrafficDetailInfo 로 변환합니다.
	 *
	 * @param trafficEntity
	 * @return
	 */
	private static TrafficDetailInfo convertToTrafficDetailInfo(TargetTrafficVO trafficEntity) {
		ObjectMapper objectMapper = new ObjectMapper();
		TrafficDetailInfo trafficDetailInfo = new TrafficDetailInfo(-1L, "ERROR", "ERROR");
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
