package com.walking.api.service.path;

import com.walking.api.repository.path.PathFavoritesRepository;
import com.walking.api.repository.path.TrafficInPathFavoritesRepository;
import com.walking.api.service.dto.PathExtractor;
import com.walking.api.service.dto.PathPrimaryData;
import com.walking.api.service.dto.PathTrafficData;
import com.walking.api.service.dto.request.FavoritePathRequestDto;
import com.walking.api.web.client.TMapClient;
import com.walking.api.web.client.dto.request.TMapRequestDto;
import com.walking.api.web.client.dto.response.TMapResponseDto;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.path.PathFavoritesEntity;
import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavePathFavoritesService {

	private final PathFavoritesRepository pathFavoritesRepository;
	private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;
	private final TMapClient tMapClient;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;

	public void execute(FavoritePathRequestDto favoritePathRequestDto, Long MemberId) {

		TMapResponseDto tMapPathData =
				getTMapPathData(
						favoritePathRequestDto.getStartLat(),
						favoritePathRequestDto.getStartLng(),
						favoritePathRequestDto.getEndLat(),
						favoritePathRequestDto.getEndLng());

		PathExtractor pathExtractor = new PathExtractor(tMapPathData);
		PathPrimaryData primaryData = pathExtractor.extractPrimaryDataByTMap();

		// 신호등 중간값 좌표 추출
		List<Point> traffics = pathExtractor.extractAllTrafficPoints();

		// LineString 추출
		LineString lineString = pathExtractor.extractLineString();

		PathTrafficData pathTrafficData = extractPathTrafficInfoService.execute(traffics);

		// 저장
		savePathFavoritesAndTrafficInFavorites(
				favoritePathRequestDto,
				MemberId,
				traffics,
				pathTrafficData.getTrafficDirections(),
				lineString,
				primaryData);
	}

	// todo 다른 객체로 분리
	@Transactional(readOnly = false)
	public void savePathFavoritesAndTrafficInFavorites(
			FavoritePathRequestDto favoritePathRequestDto,
			Long MemberId,
			List<Point> traffics,
			List<TrafficDirection> trafficDirections,
			LineString lineString,
			PathPrimaryData primaryData) {
		PathFavoritesEntity savedPathFavorites =
				pathFavoritesRepository.save(
						PathFavoritesEntity.builder()
								.path(lineString)
								.memberFk(MemberEntity.builder().id(MemberId).build())
								.startPoint(
										createPoint(
												favoritePathRequestDto.getStartLng(), favoritePathRequestDto.getStartLat()))
								.endPoint(
										createPoint(
												favoritePathRequestDto.getEndLng(), favoritePathRequestDto.getEndLat()))
								.startAlias(favoritePathRequestDto.getStartName())
								.endAlias(favoritePathRequestDto.getEndName())
								.name(favoritePathRequestDto.getName())
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

	private Point createPoint(double lng, double lat) {
		GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
		return gf.createPoint(new Coordinate(lng, lat));
	}
}
