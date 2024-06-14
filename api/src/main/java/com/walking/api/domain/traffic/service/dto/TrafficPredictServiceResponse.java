package com.walking.api.domain.traffic.service.dto;

import com.walking.api.domain.traffic.service.model.PredictedData;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrafficPredictServiceResponse {

	/** Key: 신호등 아이디, Value: 예측된 데이터 */
	private Map<Long, PredictedData> predictedData;
}
