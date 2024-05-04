package com.walking.api.web.dto.response.detail;

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
public class FavoriteIntersectionDetail {

	private Long trafficId;
	private Long id;
	private String name;
	private PointDetail point;
	private Boolean isFavorite;
	private String alias;
}
