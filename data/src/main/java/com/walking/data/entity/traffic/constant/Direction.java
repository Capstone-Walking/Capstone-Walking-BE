package com.walking.data.entity.traffic.constant;

import lombok.Getter;

@Getter
public enum Direction {
	NT("북쪽"),
	ET("동쪽"),
	ST("남쪽"),
	WT("서쪽"),
	NE("북동"),
	SE("남동"),
	SW("남서"),
	NW("북서");

	private String description;

	Direction(String description) {
		this.description = description;
	}
}
