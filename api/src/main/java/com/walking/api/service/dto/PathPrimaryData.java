package com.walking.api.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PathPrimaryData {

	private Integer totalTime;
	private Integer untilTrafficTime;
	private Integer totalDistance;
}
