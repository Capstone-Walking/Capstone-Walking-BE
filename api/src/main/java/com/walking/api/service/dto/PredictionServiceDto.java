package com.walking.api.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PredictionServiceDto {

	private List<Long> trafficIds;
	private int dataInterval;
}
