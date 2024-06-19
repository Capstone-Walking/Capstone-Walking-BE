package com.walking.api.domain.traffic.dto.detail;

import java.time.LocalDateTime;
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
public class FavoriteTrafficDetail {

	private Long id;
	private TrafficDetailInfo detail;
	private String name;
	private PointDetail point;
	private LocalDateTime createdAt;
}
