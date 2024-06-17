package com.walking.api.domain.path.dto;

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
public class SavePathFavoritesUseCaseRequest {
	private Long memberId;
	private String name;
	private String startName;
	private double startLat;
	private double startLng;
	private String endName;
	private double endLat;
	private double endLng;
}
