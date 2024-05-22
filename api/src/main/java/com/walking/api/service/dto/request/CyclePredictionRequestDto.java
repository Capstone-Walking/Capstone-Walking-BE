package com.walking.api.service.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CyclePredictionRequestDto {

	private List<Long> trafficIds;
	private int dataInterval;
}
