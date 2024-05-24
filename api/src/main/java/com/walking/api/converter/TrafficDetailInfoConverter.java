package com.walking.api.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.web.dto.response.detail.TrafficDetailInfo;
import com.walking.data.entity.traffic.TrafficEntity;

public final class TrafficDetailInfoConverter {

	private TrafficDetailInfoConverter() {}

	/**
	 * api.traffic_detail 의 detail 값(JSON)을 파싱하여 TrafficDetailInfo 로 변환합니다.
	 *
	 * @param trafficEntity
	 * @return
	 */
	public static TrafficDetailInfo execute(TrafficEntity trafficEntity) {
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
