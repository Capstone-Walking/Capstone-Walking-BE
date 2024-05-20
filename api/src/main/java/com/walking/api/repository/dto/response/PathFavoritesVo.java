package com.walking.api.repository.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PathFavoritesVo {

	private Long id;
	private Geometry startPoint;
	private Geometry endPoint;
	private String startAlias;
	private String endAlias;
	private String name;
	private LocalDateTime createdAt;
}
