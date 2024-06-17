package com.walking.api.domain.traffic.dto.detail;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TrafficDetailInfo {
	private Long trafficId;
	private String apiSource;
	private String direction;

	public TrafficDetailInfo(Long trafficId, String apiSource, String direction) {
		this.trafficId = trafficId;
		this.apiSource = apiSource;
		this.direction = direction;
	}
}
