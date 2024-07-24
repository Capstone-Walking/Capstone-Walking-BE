package com.walking.api.domain.path.service;

import com.walking.api.data.entity.path.TrafficDirection;
import com.walking.api.data.entity.traffic.TrafficEntity;
import com.walking.api.data.entity.traffic.constant.TrafficColor;
import com.walking.api.domain.path.model.SearchPath.PathPrimaryVO;
import com.walking.api.domain.path.service.dto.CRDQuery;
import com.walking.api.domain.path.service.dto.PathTrafficVO;
import com.walking.api.domain.path.service.dto.RouteDetailVO;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.domain.traffic.service.TrafficPredictService;
import com.walking.api.domain.traffic.service.dto.TPQuery;
import com.walking.api.domain.traffic.service.dto.TPVO;
import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import com.walking.api.domain.util.JsonParser;
import com.walking.api.web.dto.support.TrafficDetailConverter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CalculateRouteDetailService {

	private final TrafficPredictService trafficPredictService;

	public RouteDetailVO execute(CRDQuery query) {
		double startLat = query.getStartLat();
		double startLng = query.getStartLng();
		double endLat = query.getEndLat();
		double endLng = query.getEndLng();
		List<Point> traffics = query.getTraffics();
		PathTrafficVO pathTrafficVo = query.getPathTrafficVo();
		PathPrimaryVO primaryData = query.getPrimaryData();
		LineString lineString = query.getLineString();

		// case 1 길에 신호등이 없는 경우
		if (traffics.isEmpty()) {
			return RouteDetailVO.builder()
					.nowTime(LocalDateTime.now())
					.totalTime(primaryData.getTotalTime())
					.trafficCount(0)
					.departureTimes(new ArrayList<>())
					.timeToFirstTraffic(primaryData.getUntilTrafficTime())
					.totalDistance(primaryData.getTotalDistance())
					.startPoint(PointDetail.builder().lat(startLat).lng(startLng).build())
					.endPoint(PointDetail.builder().lat(endLat).lng(endLng).build())
					.traffics(new ArrayList<>())
					.trafficIdsInPath(new ArrayList<>())
					.paths(convertLineStringToPointDetailList(lineString))
					.build();
		}

		// case 2 길에 신호등이 있는 경우
		// 1. 처음 신호등의 위도 경도를 통해 교차로의 신호등 리스트를 찾는다.
		if (pathTrafficVo.getTrafficsInPath().isEmpty()) {
			return RouteDetailVO.builder()
					.nowTime(LocalDateTime.now())
					.totalTime(primaryData.getTotalTime())
					.trafficCount(0)
					.departureTimes(new ArrayList<>())
					.timeToFirstTraffic(primaryData.getUntilTrafficTime())
					.totalDistance(primaryData.getTotalDistance())
					.startPoint(PointDetail.builder().lat(startLat).lng(startLng).build())
					.endPoint(PointDetail.builder().lat(endLat).lng(endLng).build())
					.traffics(new ArrayList<>())
					.trafficIdsInPath(new ArrayList<>())
					.paths(convertLineStringToPointDetailList(lineString))
					.build();
		}

		// 2. 티맵을 통해 확인한 첫번째 신호등이 어떤 방향인지 확인한다.
		// TrafficEntity firstTraffic = getFirstTraffic(pathTrafficData, FirstClosetTraffic);
		TrafficEntity firstTraffic = pathTrafficVo.getTrafficsInPath().get(0);
		// 첫 신호등에 대해
		List<Long> firstTrafficId = new ArrayList<>();
		firstTrafficId.add(firstTraffic.getId());

		TPVO predictResponse =
				trafficPredictService.execute(TPQuery.builder().trafficIds(firstTrafficId).build());

		Map<Long, PredictedTraffic> firestPredictedDataMap = predictResponse.getPredictedData();

		// 모든 신호등에 대해
		TPVO allPredictResponse =
				trafficPredictService.execute(
						TPQuery.builder()
								.trafficIds(
										pathTrafficVo.getTrafficsInPath().stream()
												.map(TrafficEntity::getId)
												.collect(Collectors.toList()))
								.build());

		Map<Long, PredictedTraffic> AllPredictedDataMap = allPredictResponse.getPredictedData();

		LocalDateTime now = LocalDateTime.now();
		// 처음 내가 지나가는 신호등의 예측 출발시간 3개
		List<LocalDateTime> departureTimes =
				calDepartureTimes(
						firestPredictedDataMap, firstTraffic, primaryData.getUntilTrafficTime(), now);

		removeNullInPredictedDataMap(firestPredictedDataMap);

		removeNullInPredictedDataMap(AllPredictedDataMap);

		return RouteDetailVO.builder()
				.nowTime(now)
				.totalTime(primaryData.getTotalTime())
				.trafficCount(traffics.size())
				.departureTimes(departureTimes)
				.timeToFirstTraffic(primaryData.getUntilTrafficTime())
				.totalDistance(primaryData.getTotalDistance())
				.startPoint(PointDetail.builder().lat(startLat).lng(startLng).build())
				.endPoint(PointDetail.builder().lat(endLat).lng(endLng).build())
				.traffics(TrafficDetailConverter.execute(new ArrayList<>(AllPredictedDataMap.values())))
				.trafficIdsInPath(
						pathTrafficVo.getTrafficsInPath().stream()
								.map(TrafficEntity::getId)
								.collect(Collectors.toList()))
				.paths(convertLineStringToPointDetailList(lineString))
				.build();
	}

	private void removeNullInPredictedDataMap(Map<Long, PredictedTraffic> firestPredictedDataMap) {
		// 내가 지나는 전부를 신호등을 특정한다.
		Iterator<Map.Entry<Long, PredictedTraffic>> iterator =
				firestPredictedDataMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, PredictedTraffic> entry = iterator.next();
			PredictedTraffic predictedTraffic = entry.getValue();
			if (predictedTraffic.getRedCycle().isEmpty()) {
				iterator.remove(); // redCycle이 null인 경우, 해당 Entry를 Map에서 제거
			}
		}
	}

	public static List<PointDetail> convertLineStringToPointDetailList(LineString lineString) {
		List<PointDetail> points = new ArrayList<>();
		for (Coordinate coord : lineString.getCoordinates()) {
			points.add(
					PointDetail.builder().lat(coord.y).lng(coord.x).build()); // y is latitude, x is longitude
		}
		return points;
	}

	private List<LocalDateTime> calDepartureTimes(
			Map<Long, PredictedTraffic> predictedDataMap,
			TrafficEntity firstTraffic,
			Integer untilFirstTrafficTime,
			LocalDateTime now) {

		PredictedTraffic predictedTraffic = predictedDataMap.get(firstTraffic.getId());
		predictedTraffic.isAllPredicted();

		TrafficColor currentColor;
		Float currentTimeLeft;
		Float redCycle;
		Float greenCycle;

		try {
			currentColor = predictedTraffic.getCurrentColor().orElseThrow();
			currentTimeLeft = predictedTraffic.getCurrentTimeLeft().orElseThrow();
			redCycle = predictedTraffic.getRedCycle().orElseThrow();
			greenCycle = predictedTraffic.getGreenCycle().orElseThrow();
		} catch (NoSuchElementException e) {
			return new ArrayList<>();
		}

		LocalDateTime firstGreenTime =
				getFirstGreenTime(currentColor, currentTimeLeft, redCycle, greenCycle, now);

		Duration diff = Duration.between(firstGreenTime, now);
		long diffSeconds = diff.toSeconds();

		// diffSeconds -> 첫 파란불을 만나는 타이밍
		// untilFirstTrafficTime 첫 신호등까지 가는 시간이 첫 파란불을 만날 타이밍보다 클경우
		while (diffSeconds <= untilFirstTrafficTime) {
			diffSeconds += redCycle.longValue() + greenCycle.longValue();
		}
		// 내가 가는 시간보다 diffSeconds는 더 커야한다. 이때 빨간불, 파란불 사이클의 합만큼을 untilFirstTrafficTime보다 클때까지 더해준다.

		List<LocalDateTime> departureTimes = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			departureTimes.add(
					now.plusSeconds(diffSeconds + (redCycle.longValue() + greenCycle.longValue()) * i));
		}

		return departureTimes;
	}

	private LocalDateTime getFirstGreenTime(
			TrafficColor currentColor,
			Float currentTimeLeft,
			Float redCycle,
			Float greenCycle,
			LocalDateTime now) {

		if (currentColor.isRed()) {
			return now.plusSeconds(currentTimeLeft.longValue());

		} else {
			return now.plusSeconds(currentTimeLeft.longValue() + redCycle.longValue());
		}
	}

	private static TrafficEntity getCrossTraffic(
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
