package com.walking.api.domain.path.usecase;

import com.walking.api.domain.path.dto.CalculatePathFavoritesTimeUseCaseIn;
import com.walking.api.domain.path.dto.CalculatePathFavoritesTimeUseCaseOut;
import com.walking.api.domain.path.model.SearchPath.PathPrimaryVO;
import com.walking.api.domain.path.service.CalculateRouteDetailService;
import com.walking.api.domain.path.service.ExtractPathTrafficInfoService;
import com.walking.api.domain.path.service.dto.CRDQuery;
import com.walking.api.domain.path.service.dto.EPTIQueryWithDirections;
import com.walking.api.domain.path.service.dto.PathTrafficVO;
import com.walking.api.domain.path.service.dto.RouteDetailVO;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import com.walking.api.repository.dao.path.TrafficInPathFavoritesRepository;
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
public class CalculatePathFavoritesTimeUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;
	private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;

	private final ExtractPathTrafficInfoService extractPathTrafficInfoService;
	private final CalculateRouteDetailService calculateRouteDetailService;

	public CalculatePathFavoritesTimeUseCaseOut execute(CalculatePathFavoritesTimeUseCaseIn in) {
		Optional<PathFavoritesEntity> findPath =
				pathFavoritesRepository.findById(in.getFavoritesPathId());

		PathFavoritesEntity pathFavorites = checkPathFavoritesEntity(findPath);

		Optional<TrafficInPathFavoritesEntity> findTrafficInPathFavorites =
				trafficInPathFavoritesRepository.findById(pathFavorites.getId());

		TrafficInPathFavoritesEntity trafficInPathFavorites =
				checkTrafficInPathFavoritesEntity(findTrafficInPathFavorites);

		List<TrafficDirection> trafficDirections =
				trafficInPathFavorites.getTrafficTypes(); // 내가 지나는 신호등의 방향정보
		List<Point> traffics = trafficInPathFavorites.getTrafficPoints(); // 내가 지나는 신호등 위치의 중간값

		PathTrafficVO pathTrafficVo =
				extractPathTrafficInfoService.execute(
						EPTIQueryWithDirections.builder()
								.traffics(traffics)
								.trafficDirections(trafficDirections)
								.build());

		RouteDetailVO routeDetailVo =
				calculateRouteDetailService.execute(
						CRDQuery.builder()
								.startLat(pathFavorites.getStartPoint().getY())
								.startLng(pathFavorites.getStartPoint().getX())
								.endLat(pathFavorites.getEndPoint().getY())
								.endLng(pathFavorites.getEndPoint().getX())
								.traffics(traffics)
								.pathTrafficVo(pathTrafficVo)
								.primaryData(
										new PathPrimaryVO(
												pathFavorites.getTotalTime(),
												pathFavorites.getUntilFirstTrafficTime(),
												pathFavorites.getTotalDistance()))
								.lineString(pathFavorites.getPath())
								.build());

		return CalculatePathFavoritesTimeUseCaseOut.builder()
				.nowTime(routeDetailVo.getNowTime())
				.totalTime(routeDetailVo.getTotalTime())
				.trafficCount(routeDetailVo.getTrafficCount())
				.departureTimes(routeDetailVo.getDepartureTimes())
				.timeToFirstTraffic(routeDetailVo.getTimeToFirstTraffic())
				.totalDistance(routeDetailVo.getTotalDistance())
				.startPoint(routeDetailVo.getStartPoint())
				.endPoint(routeDetailVo.getEndPoint())
				.traffics(routeDetailVo.getTraffics())
				.trafficIdsInPath(routeDetailVo.getTrafficIdsInPath())
				.paths(routeDetailVo.getPaths())
				.build();
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
