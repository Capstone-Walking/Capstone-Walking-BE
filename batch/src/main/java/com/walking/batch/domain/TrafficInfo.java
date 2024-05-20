package com.walking.batch.domain;

import com.walking.batch.domain.constants.Color;
import com.walking.batch.domain.constants.Direction;
import lombok.Getter;

/** 신호등 정보 */
@Getter
public class TrafficInfo {

	private Direction direction;
	private Double timeLeft;
	private Color color;
}
