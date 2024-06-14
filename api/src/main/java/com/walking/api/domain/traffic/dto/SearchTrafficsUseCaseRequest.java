package com.walking.api.domain.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTrafficsUseCaseRequest {
	private Float vblLng;
	private Float vblLat;
	private Float vtrLng;
	private Float vtrLat;
}
