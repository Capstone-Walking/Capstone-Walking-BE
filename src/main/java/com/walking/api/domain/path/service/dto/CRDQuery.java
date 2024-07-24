package com.walking.api.domain.path.service.dto;

import com.walking.api.domain.path.model.SearchPath.PathPrimaryVO;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

/** CalculateRouteDetailService 에서 사용하는 쿼리 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CRDQuery {
	private double startLat;
	private double startLng;
	private double endLat;
	private double endLng;
	private List<Point> traffics;
	private PathTrafficVO pathTrafficVo;
	private PathPrimaryVO primaryData;
	private LineString lineString;
}
