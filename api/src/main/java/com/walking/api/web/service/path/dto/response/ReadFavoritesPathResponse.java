package com.walking.api.web.service.path.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadFavoritesPathResponse {

	private Long id;
	private Point startPoint;
	private Point endPoint;
	private String startAlias;
	private String endAlias;
	private String name;
	private LocalDateTime createdAt;
	private Long order;
}
