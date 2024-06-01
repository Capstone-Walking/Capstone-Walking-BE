package com.walking.api.service.path;

import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.api.service.dto.PathTrafficData;
import com.walking.api.util.JsonParser;
import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
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
			List<TrafficEntity> closetTrafficByLocation =
					trafficRepository.findClosetTrafficByLocation(
							traffics.get(i).getX(), traffics.get(i).getY());
			pathTrafficData.getAllTraffics().addAll(closetTrafficByLocation);

			log.info("closetTrafficByLocation : {}", closetTrafficByLocation);
			TrafficDirection direction =
					TrafficDirection.findDirection(
							closetTrafficByLocation.get(0).getPoint(), traffics.get(i));
			pathTrafficData.getTrafficDirections().add(direction);
			TrafficEntity crossTraffic = getCrossTraffic(closetTrafficByLocation, direction);
			pathTrafficData.getTrafficsInPath().add(crossTraffic);
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

			TrafficEntity crossTraffic =
					getCrossTraffic(closetTrafficByLocation, pathTrafficData.getTrafficDirections().get(i));
			pathTrafficData.getTrafficsInPath().add(crossTraffic);
		}

		return pathTrafficData;
	}

	private TrafficEntity getCrossTraffic(
			List<TrafficEntity> ClosetTraffic, TrafficDirection trafficDirection) {
		for (TrafficEntity trafficEntity : ClosetTraffic) {
			if (JsonParser.getValue(trafficEntity.getDetail(), "direction")
					.equals(trafficDirection.name())) {
				return trafficEntity;
			}
		}
		throw new IllegalStateException("없는 방향입니다.");
	}
}
