package com.walking.api.domain.path.usecase;

// todo refactor path 패키지 내부 클래스를 사용하도록 수정

import com.walking.api.data.entity.member.MemberEntity;
import com.walking.api.data.entity.path.PathFavoritesEntity;
import com.walking.api.data.entity.path.TrafficDirection;
import com.walking.api.data.entity.path.TrafficInPathFavoritesEntity;
import com.walking.api.domain.client.TMapClient;
import com.walking.api.domain.client.dto.request.TMapRequestDto;
import com.walking.api.domain.client.dto.response.TMapResponseDto;
import com.walking.api.domain.path.dto.SavePathFavoritesUseCaseIn;
import com.walking.api.domain.path.model.SearchPath;
import com.walking.api.domain.path.model.SearchPath.PathPrimaryVO;
import com.walking.api.domain.path.service.ExtractPathTrafficInfoService;
import com.walking.api.domain.path.service.dto.EPTIQuery;
import com.walking.api.domain.path.service.dto.PathTrafficVO;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import com.walking.api.repository.dao.path.TrafficInPathFavoritesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavePathFavoritesUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;
	private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;
	private final TMapClient tMapClient;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;

	@Transactional
	public void execute(SavePathFavoritesUseCaseIn in) {
		TMapResponseDto tMapPathData =
				getTMapPathData(in.getStartLat(), in.getStartLng(), in.getEndLat(), in.getEndLng());

		SearchPath searchPath = new SearchPath(tMapPathData);
		PathPrimaryVO primaryData = searchPath.extractPrimaryDataByTMap();

		// 신호등 중간값 좌표 추출
		List<Point> traffics = searchPath.extractAllTrafficPoints();

		// LineString 추출
		LineString lineString = searchPath.extractLineString();

		PathTrafficVO pathTrafficVo =
				extractPathTrafficInfoService.execute(EPTIQuery.builder().traffics(traffics).build());

		// 저장
		savePathFavoritesAndTrafficInFavorites(
				in, traffics, pathTrafficVo.getTrafficDirections(), lineString, primaryData);
	}

	// todo 다른 객체로 분리
	@Transactional
	public void savePathFavoritesAndTrafficInFavorites(
			SavePathFavoritesUseCaseIn request,
			List<Point> traffics,
			List<TrafficDirection> trafficDirections,
			LineString lineString,
			PathPrimaryVO primaryData) {
		PathFavoritesEntity savedPathFavorites =
				pathFavoritesRepository.save(
						PathFavoritesEntity.builder()
								.path(lineString)
								.memberFk(MemberEntity.builder().id(request.getMemberId()).build())
								.startPoint(createPoint(request.getStartLng(), request.getStartLat()))
								.endPoint(createPoint(request.getEndLng(), request.getEndLat()))
								.startAlias(request.getStartName())
								.endAlias(request.getEndName())
								.name(request.getName())
								.order(pathFavoritesRepository.findMaxOrder() + 1)
								.totalTime(primaryData.getTotalTime())
								.totalDistance(primaryData.getTotalDistance())
								.untilFirstTrafficTime(primaryData.getUntilTrafficTime())
								.build());

		trafficInPathFavoritesRepository.save(
				TrafficInPathFavoritesEntity.builder()
						.pathFk(savedPathFavorites)
						.trafficPoints(traffics)
						.trafficTypes(trafficDirections)
						.build());
	}

	// todo service 클래스로 분리
	private TMapResponseDto getTMapPathData(
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

	private Point createPoint(double lng, double lat) {
		GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
		return gf.createPoint(new Coordinate(lng, lat));
	}
}
