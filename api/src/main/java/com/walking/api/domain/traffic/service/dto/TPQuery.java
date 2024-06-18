package com.walking.api.domain.traffic.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/** TrafficPredictService 에서 사용하는 쿼리 */
@Getter
@Builder
public class TPQuery {
	private List<Long> trafficIds;
}
