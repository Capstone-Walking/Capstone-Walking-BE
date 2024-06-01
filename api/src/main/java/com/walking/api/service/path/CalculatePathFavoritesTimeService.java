package com.walking.api.service.path;

import com.walking.api.repository.path.PathFavoritesRepository;
import com.walking.api.repository.path.TrafficInPathFavoritesRepository;
import com.walking.api.service.dto.PathPrimaryData;
import com.walking.api.service.dto.PathTrafficData;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.data.entity.path.PathFavoritesEntity;
import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalculatePathFavoritesTimeService {

	private final PathFavoritesRepository pathFavoritesRepository;
	private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;
	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;
	private final RouteDetailResponseService routeDetailResponseService;

	public RouteDetailResponse execute(Long favoritesPathId) {
		Optional<PathFavoritesEntity> findPath = pathFavoritesRepository.findById(favoritesPathId);

		PathFavoritesEntity pathFavorites = checkPathFavoritesEntity(findPath);

		Optional<TrafficInPathFavoritesEntity> findTrafficInPathFavorites =
				trafficInPathFavoritesRepository.findById(pathFavorites.getId());

		TrafficInPathFavoritesEntity trafficInPathFavorites =
				checkTrafficInPathFavoritesEntity(findTrafficInPathFavorites);

		List<TrafficDirection> trafficDirections =
				trafficInPathFavorites.getTrafficTypes(); // 내가 지나는 신호등의 방향정보
		List<Point> traffics = trafficInPathFavorites.getTrafficPoints(); // 내가 지나는 신호등 위치의 중간값

		PathTrafficData pathTrafficData =
				extractPathTrafficInfoService.execute(traffics, trafficDirections);

		return routeDetailResponseService.execute(
				pathFavorites.getStartPoint().getY(),
				pathFavorites.getStartPoint().getX(),
				pathFavorites.getEndPoint().getY(),
				pathFavorites.getEndPoint().getX(),
				traffics,
				pathTrafficData,
				new PathPrimaryData(
						pathFavorites.getTotalTime(),
						pathFavorites.getUntilFirstTrafficTime(),
						pathFavorites.getTotalDistance()),
				pathFavorites.getPath());
	}

	private TrafficInPathFavoritesEntity checkTrafficInPathFavoritesEntity(
			Optional<TrafficInPathFavoritesEntity> findTrafficInPathFavorites) {
		if (findTrafficInPathFavorites.isEmpty()) {
			throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
		}

		return findTrafficInPathFavorites.get();
	}

	private PathFavoritesEntity checkPathFavoritesEntity(
			Optional<PathFavoritesEntity> pathFavoritesEntity) {

		if (pathFavoritesEntity.isEmpty()) {
			throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
		}

		return pathFavoritesEntity.get();
	}
}
