package com.walking.api.domain.path.service.dto;

import com.walking.api.data.entity.path.TrafficDirection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

/** ExtractPathTrafficInfoService 에서 사용하는 쿼리 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EPTIQueryWithDirections {
	private List<Point> traffics;
	List<TrafficDirection> trafficDirections;
}
