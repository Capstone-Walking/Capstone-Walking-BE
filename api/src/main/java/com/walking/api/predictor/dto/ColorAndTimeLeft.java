package com.walking.api.predictor.dto;

import com.walking.data.entity.traffic.constant.TrafficColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorAndTimeLeft {

	private TrafficColor trafficColor;
	private Float timeLeft;
}
