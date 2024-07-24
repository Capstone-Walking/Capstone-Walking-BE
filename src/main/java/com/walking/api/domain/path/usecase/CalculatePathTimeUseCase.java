package com.walking.api.domain.path.usecase;

// todo refactor path 패키지 내부 클래스를 사용하도록 수정

import com.walking.api.domain.client.TMapClient;
import com.walking.api.domain.client.dto.request.TMapRequestDto;
import com.walking.api.domain.client.dto.response.TMapResponseDto;
import com.walking.api.domain.path.dto.CalculatePathTimeUseCaseIn;
import com.walking.api.domain.path.dto.CalculatePathTimeUseCaseOut;
import com.walking.api.domain.path.model.SearchPath;
import com.walking.api.domain.path.model.SearchPath.PathPrimaryVO;
import com.walking.api.domain.path.service.CalculateRouteDetailService;
import com.walking.api.domain.path.service.ExtractPathTrafficInfoService;
import com.walking.api.domain.path.service.dto.CRDQuery;
import com.walking.api.domain.path.service.dto.EPTIQuery;
import com.walking.api.domain.path.service.dto.PathTrafficVO;
import com.walking.api.domain.path.service.dto.RouteDetailVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CalculatePathTimeUseCase {

	private final TMapClient tMapClient;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;
	private final CalculateRouteDetailService calculateRouteDetailService;

	public CalculatePathTimeUseCaseOut execute(CalculatePathTimeUseCaseIn in) {
		TMapResponseDto tMapPathData =
				searchPath(in.getStartLat(), in.getStartLng(), in.getEndLat(), in.getEndLng());
		SearchPath searchPath = new SearchPath(tMapPathData);

		PathPrimaryVO primaryData = searchPath.extractPrimaryDataByTMap();

		// LineString 추출
		LineString lineString = searchPath.extractLineString();

		// 신호등 중간값 좌표 추출
		List<Point> traffics = searchPath.extractAllTrafficPoints();
		PathTrafficVO pathTrafficVo =
				extractPathTrafficInfoService.execute(EPTIQuery.builder().traffics(traffics).build());

		RouteDetailVO routeDetailVO =
				calculateRouteDetailService.execute(
						CRDQuery.builder()
								.startLat(in.getStartLat())
								.startLng(in.getStartLng())
								.endLat(in.getEndLat())
								.endLng(in.getEndLng())
								.traffics(traffics)
								.pathTrafficVo(pathTrafficVo)
								.primaryData(primaryData)
								.lineString(lineString)
								.build());

		return CalculatePathTimeUseCaseOut.builder()
				.nowTime(routeDetailVO.getNowTime())
				.totalTime(routeDetailVO.getTotalTime())
				.trafficCount(routeDetailVO.getTrafficCount())
				.departureTimes(routeDetailVO.getDepartureTimes())
				.timeToFirstTraffic(routeDetailVO.getTimeToFirstTraffic())
				.totalDistance(routeDetailVO.getTotalDistance())
				.startPoint(routeDetailVO.getStartPoint())
				.endPoint(routeDetailVO.getEndPoint())
				.traffics(routeDetailVO.getTraffics())
				.trafficIdsInPath(routeDetailVO.getTrafficIdsInPath())
				.paths(routeDetailVO.getPaths())
				.build();
	}

	// todo service 클래스로 분리
	private TMapResponseDto searchPath(
			double startLat, double startLng, double endLat, double endLng) {
		// 가져와야될 것 -> 신호등 어떤게 있는지, 길  정보
		return tMapClient.searchPath(
				TMapRequestDto.builder()
						.startX(startLng)
						.startY(startLat)
						.endX(endLng)
						.endY(endLat)
						.startName("출발지")
						.endName("도착지")
						.build());
	}
}
