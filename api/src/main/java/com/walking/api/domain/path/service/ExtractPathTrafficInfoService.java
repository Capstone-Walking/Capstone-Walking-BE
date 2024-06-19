package com.walking.api.domain.path.service;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.path.service.dto.EPTIQuery;
import com.walking.api.domain.path.service.dto.EPTIQueryWithDirections;
import com.walking.api.domain.path.service.dto.PathTrafficVO;
import com.walking.api.repository.dao.traffic.TrafficRepository;
import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(value = TRANSACTION_MANAGER_NAME)
@Slf4j
public class ExtractPathTrafficInfoService {

	private final TrafficRepository trafficRepository;

	// 신호등 좌표를 기준으로 db의 교차로의 신호등 조회
	public PathTrafficVO execute(EPTIQuery query) {
		List<Point> traffics = query.getTraffics();
		PathTrafficVO pathTrafficVo = new PathTrafficVO();
		for (int i = 0; i < traffics.size(); i++) {
			Optional<TrafficEntity> closestTraffic =
					trafficRepository.findClosestTraffic(traffics.get(i).getX(), traffics.get(i).getY());

			closestTraffic.ifPresent(
					trafficEntity -> pathTrafficVo.getTrafficsInPath().add(trafficEntity));
		}
		return pathTrafficVo;
	}

	public PathTrafficVO execute(EPTIQueryWithDirections query) {
		List<Point> traffics = query.getTraffics();
		List<TrafficDirection> trafficDirections = query.getTrafficDirections();
		PathTrafficVO pathTrafficVo = new PathTrafficVO();
		pathTrafficVo.setTrafficDirections(trafficDirections);
		for (int i = 0; i < traffics.size(); i++) {
			List<TrafficEntity> closetTrafficByLocation =
					trafficRepository.findClosetTrafficByLocation(
							traffics.get(i).getX(), traffics.get(i).getY());
			pathTrafficVo.getAllTraffics().addAll(closetTrafficByLocation);
		}
		return pathTrafficVo;
	}
}
