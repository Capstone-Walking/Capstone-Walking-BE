package com.walking.api.service.path;

import com.walking.api.service.dto.PathExtractor;
import com.walking.api.service.dto.PathPrimaryData;
import com.walking.api.service.dto.PathTrafficData;
import com.walking.api.web.client.TMapClient;
import com.walking.api.web.client.dto.request.TMapRequestDto;
import com.walking.api.web.client.dto.response.TMapResponseDto;
import com.walking.api.web.dto.response.RouteDetailResponse;
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
public class CalculatePathTimeService {

	private final TMapClient tMapClient;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;
	private final RouteDetailResponseService routeDetailResponseService;

	public RouteDetailResponse execute(
			double startLat, double startLng, double endLat, double endLng) {

		TMapResponseDto tMapPathData = getTMapPathData(startLat, startLng, endLat, endLng);

		PathExtractor pathExtractor = new PathExtractor(tMapPathData);
		PathPrimaryData primaryData = pathExtractor.extractPrimaryDataByTMap();

		// 신호등 중간값 좌표 추출
		List<Point> traffics = pathExtractor.extractAllTrafficPoints();

		// LineString 추출
		LineString lineString = pathExtractor.extractLineString();

		PathTrafficData pathTrafficData = extractPathTrafficInfoService.execute(traffics);

		return routeDetailResponseService.execute(
				startLat, startLng, endLat, endLng, traffics, pathTrafficData, primaryData, lineString);
	}

	private TMapResponseDto getTMapPathData(
			double startLat, double startLng, double endLat, double endLng) {
		// 가져와야될 것 -> 신호등 어떤게 있는지, 길  정보

		TMapResponseDto tMapResponseDto =
				tMapClient.TMapDetailPathSearch(
						TMapRequestDto.builder()
								.startX(startLng)
								.startY(startLat)
								.endX(endLng)
								.endY(endLat)
								.startName("출발지")
								.endName("도착지")
								.build());
		System.out.println("결과 : " + tMapResponseDto.toString());
		return tMapResponseDto;
	}
}
