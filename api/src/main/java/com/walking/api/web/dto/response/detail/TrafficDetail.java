package com.walking.api.web.dto.response.detail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TrafficDetail {

	private Long id;
	private String state;
	private Long remainTime;
	private Long greenCycle;
	private Long redCycle;
	private PointDetail point;
	private String color;
}
