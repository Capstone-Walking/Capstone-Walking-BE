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
public class SavePathFavoritesUseCaseIn {
	private Long memberId;
	private String name;
	private String startName;
	private Double startLat;
	private Double startLng;
	private String endName;
	private Double endLat;
	private Double endLng;
}
