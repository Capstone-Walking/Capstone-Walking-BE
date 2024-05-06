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
public class FavoriteTrafficDetail {

	private Long id;
	private String detail;
	private String name;
	private PointDetail point;
	private LocalDateTime createdAt;
}
