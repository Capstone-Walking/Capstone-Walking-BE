package com.walking.api.web.dto.response.detail;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PointDetail {

	private double lat;
	private double lng;

	@Builder
	public PointDetail(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
