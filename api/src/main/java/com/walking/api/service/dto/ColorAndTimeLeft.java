package com.walking.api.service.dto;

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

	public TrafficColor updateColor(TrafficColor trafficColor) {
		this.trafficColor = trafficColor;
		return this.trafficColor;
	}

	public Float updateTimeLeft(Float timeLeft) {
		this.timeLeft = timeLeft;
		return this.timeLeft;
	}
}
