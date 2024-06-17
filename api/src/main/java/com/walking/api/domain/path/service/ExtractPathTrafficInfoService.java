package com.walking.api.domain.path.service;

import com.walking.api.domain.path.dto.PathTrafficData;
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
@Transactional(readOnly = true)
@Slf4j
public class ExtractPathTrafficInfoService {

	private final TrafficRepository trafficRepository;

	// 신호등 좌표를 기준으로 db의 교차로의 신호등 조회
	public PathTrafficData execute(List<Point> traffics) {
		PathTrafficData pathTrafficData = new PathTrafficData();
		for (int i = 0; i < traffics.size(); i++) {
			Optional<TrafficEntity> closestTraffic =
					trafficRepository.findClosestTraffic(traffics.get(i).getX(), traffics.get(i).getY());

			closestTraffic.ifPresent(
					trafficEntity -> pathTrafficData.getTrafficsInPath().add(trafficEntity));
		}
		return pathTrafficData;
	}

	public PathTrafficData execute(List<Point> traffics, List<TrafficDirection> trafficDirections) {
		PathTrafficData pathTrafficData = new PathTrafficData();
		pathTrafficData.setTrafficDirections(trafficDirections);
		for (int i = 0; i < traffics.size(); i++) {
			List<TrafficEntity> closetTrafficByLocation =
					trafficRepository.findClosetTrafficByLocation(
							traffics.get(i).getX(), traffics.get(i).getY());
			pathTrafficData.getAllTraffics().addAll(closetTrafficByLocation);
		}
		return pathTrafficData;
	}
}
