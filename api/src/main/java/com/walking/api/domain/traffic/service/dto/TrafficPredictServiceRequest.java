package com.walking.api.domain.traffic.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrafficPredictServiceRequest {
	private List<Long> trafficIds;
}
