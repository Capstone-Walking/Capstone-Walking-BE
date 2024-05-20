package com.walking.batch.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
