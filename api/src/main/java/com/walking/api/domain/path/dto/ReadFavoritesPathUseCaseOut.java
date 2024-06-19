package com.walking.api.domain.path.dto;

import java.time.LocalDateTime;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadFavoritesPathUseCaseOut {
	private Long id;
	private Point startPoint;
	private Point endPoint;
	private String startAlias;
	private String endAlias;
	private String name;
	private LocalDateTime createdAt;
	private Long order;
}
