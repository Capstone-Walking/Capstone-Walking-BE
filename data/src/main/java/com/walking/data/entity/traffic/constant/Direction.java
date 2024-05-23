package com.walking.data.entity.traffic.constant;

import lombok.Getter;

@Getter
public enum Direction {
	nt("북쪽"),
	et("동쪽"),
	st("남쪽"),
	wt("서쪽"),
	ne("북동"),
	se("남동"),
	sw("남서"),
	nw("북서");

	private String description;

	Direction(String description) {
		this.description = description;
	}
}
