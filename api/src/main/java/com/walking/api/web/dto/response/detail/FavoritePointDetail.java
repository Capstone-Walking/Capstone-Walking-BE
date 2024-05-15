package com.walking.api.web.dto.response.detail;

import java.time.LocalDateTime;
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
public class FavoritePointDetail {

	private Long id;
	private String name;
	private PointDetail startPoint;
	private PointDetail endPoint;
	private LocalDateTime createdAt;
	private String startAlias;
	private String endAlias;
	private Long order;
}
