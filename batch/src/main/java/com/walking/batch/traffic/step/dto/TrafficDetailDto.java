package com.walking.batch.traffic.step.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TrafficDetailDto {

	private Long itstId;
	private String direction;
	private String color;
	private Float timeLeft;
	private OffsetDateTime colorRegDt; // 색상 정보 등록일자
	private OffsetDateTime timeLeftRegDt; // 잔여 시간 등록일자
}
