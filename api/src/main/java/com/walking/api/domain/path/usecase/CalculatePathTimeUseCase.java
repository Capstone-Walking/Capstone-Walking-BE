package com.walking.api.domain.path.usecase;

// todo refactor path 패키지 내부 클래스를 사용하도록 수정

import com.walking.api.domain.client.TMapClient;
import com.walking.api.domain.client.dto.request.TMapRequestDto;
import com.walking.api.domain.client.dto.response.TMapResponseDto;
import com.walking.api.domain.path.dto.CalculatePathTimeUseCaseResponse;
import com.walking.api.domain.path.dto.PathPrimaryData;
import com.walking.api.domain.path.dto.PathTrafficData;
import com.walking.api.domain.path.model.SearchPath;
import com.walking.api.domain.path.service.ExtractPathTrafficInfoService;
import com.walking.api.domain.path.service.RouteDetailResponseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CalculatePathTimeUseCase {

	private final TMapClient tMapClient;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;
	private final RouteDetailResponseService routeDetailResponseService;

	public CalculatePathTimeUseCaseResponse execute(
			double startLat, double startLng, double endLat, double endLng) {

		TMapResponseDto tMapPathData = searchPath(startLat, startLng, endLat, endLng);
		SearchPath searchPath = new SearchPath(tMapPathData);

		PathPrimaryData primaryData = searchPath.extractPrimaryDataByTMap();
		// LineString 추출
		LineString lineString = searchPath.extractLineString();

		// 신호등 중간값 좌표 추출
		List<Point> traffics = searchPath.extractAllTrafficPoints();
		PathTrafficData pathTrafficData = extractPathTrafficInfoService.execute(traffics);

		return routeDetailResponseService.execute(
				startLat, startLng, endLat, endLng, traffics, pathTrafficData, primaryData, lineString);
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
