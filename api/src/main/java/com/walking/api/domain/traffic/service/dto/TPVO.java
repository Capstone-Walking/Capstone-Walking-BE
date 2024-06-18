package com.walking.api.domain.traffic.service.dto;

import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/** TrafficPredictService 에서 사용하는 VO */
@Getter
@Builder
public class TPVO {

	/** Key: 신호등 아이디, Value: 예측된 데이터 */
	private Map<Long, PredictedTraffic> predictedData;
}
