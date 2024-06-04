package com.walking.api.service.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntegrationPredictRequestDto {

	private List<Long> trafficIds;
}
