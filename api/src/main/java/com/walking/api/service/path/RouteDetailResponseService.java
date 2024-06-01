package com.walking.api.service.path;

import com.walking.api.converter.TrafficDetailConverter;
import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.api.service.TrafficIntegrationPredictService;
import com.walking.api.service.dto.PathPrimaryData;
import com.walking.api.service.dto.PathTrafficData;
import com.walking.api.service.dto.PredictedData;
import com.walking.api.service.dto.request.IntegrationPredictRequestDto;
import com.walking.api.service.dto.response.IntegrationPredictResponseDto;
import com.walking.api.util.JsonParser;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.traffic.TrafficEntity;
import com.walking.data.entity.traffic.constant.TrafficColor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RouteDetailResponseService {

	private final TrafficRepository trafficRepository;
	private final TrafficIntegrationPredictService trafficIntegrationPredictService;

	public RouteDetailResponse execute(
			double startLat,
			double startLng,
			double endLat,
			double endLng,
			List<Point> traffics,
			PathTrafficData pathTrafficData,
			PathPrimaryData primaryData,
			LineString lineString) {
		// case 1 길에 신호등이 없는 경우
		// todo

		// case 2 길에 신호등이 있는 경우
		// 1. 처음 신호등의 위도 경도를 통해 교차로의 신호등 리스트를 찾는다.
		List<TrafficEntity> FirstClosetTraffic =
				trafficRepository.findClosetTrafficByLocation(
						traffics.get(0).getX(), traffics.get(0).getY());

		// 2. 티맵을 통해 확인한 첫번째 신호등이 어떤 방향인지 확인한다.
		TrafficEntity firstTraffic = getFirstTraffic(pathTrafficData, FirstClosetTraffic);

		// 첫 신호등에 대해
		IntegrationPredictResponseDto predictResponse =
				trafficIntegrationPredictService.execute(
						IntegrationPredictRequestDto.builder()
								.trafficIds(
										FirstClosetTraffic.stream()
												.map(TrafficEntity::getId)
												.collect(Collectors.toList()))
								.build());

		Map<Long, PredictedData> firestPredictedDataMap = predictResponse.getPredictedDataMap();

		// 모든 신호등에 대해
		IntegrationPredictResponseDto allPredictResponse =
				trafficIntegrationPredictService.execute(
						IntegrationPredictRequestDto.builder()
								.trafficIds(
										pathTrafficData.getAllTraffics().stream()
												.map(TrafficEntity::getId)
												.collect(Collectors.toList()))
								.build());

		Map<Long, PredictedData> AllPredictedDataMap = allPredictResponse.getPredictedDataMap();

		// 처음 내가 지나가는 신호등의 예측 출발시간 3개
		List<LocalDateTime> departureTimes =
				calDepartureTimes(firestPredictedDataMap, firstTraffic, primaryData.getUntilTrafficTime());

		removeNullInPredictedDataMap(firestPredictedDataMap);

		removeNullInPredictedDataMap(AllPredictedDataMap);

		return RouteDetailResponse.builder()
				.totalTime(primaryData.getTotalTime())
				.trafficCount(traffics.size())
				.departureTimes(departureTimes)
				.timeToFirstTraffic(primaryData.getUntilTrafficTime())
				.totalDistance(primaryData.getTotalDistance())
				.startPoint(PointDetail.builder().lat(startLat).lng(startLng).build())
				.endPoint(PointDetail.builder().lat(endLat).lng(endLng).build())
				.traffics(
						AllPredictedDataMap.keySet().stream()
								.map(
										trafficId -> TrafficDetailConverter.execute(AllPredictedDataMap.get(trafficId)))
								.collect(Collectors.toList()))
				.trafficIdsInPath(
						pathTrafficData.getTrafficsInPath().stream()
								.map(TrafficEntity::getId)
								.collect(Collectors.toList()))
				.paths(convertLineStringToPointDetailList(lineString))
				.build();
	}

	private void removeNullInPredictedDataMap(Map<Long, PredictedData> firestPredictedDataMap) {
		// 내가 지나는 전부를 신호등을 특정한다.
		Iterator<Map.Entry<Long, PredictedData>> iterator =
				firestPredictedDataMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, PredictedData> entry = iterator.next();
			PredictedData predictedData = entry.getValue();
			if (predictedData.getRedCycle() == null) {
				iterator.remove(); // redCycle이 null인 경우, 해당 Entry를 Map에서 제거
			}
		}
	}

	private static TrafficEntity getFirstTraffic(
			PathTrafficData pathTrafficData, List<TrafficEntity> FirstClosetTraffic) {
		TrafficDirection trafficDirection = pathTrafficData.getTrafficDirections().get(0);

		// 해당 방향의 신호등을 특정한다.
		return getCrossTraffic(FirstClosetTraffic, trafficDirection);
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
			Map<Long, PredictedData> predictedDataMap,
			TrafficEntity firstTraffic,
			Integer untilFirstTrafficTime) {

		PredictedData predictedData = predictedDataMap.get(firstTraffic.getId());
		predictedData.isAllPredicted();

		TrafficColor currentColor = predictedData.getCurrentColor();
		Float currentTimeLeft = predictedData.getCurrentTimeLeft();
		Float redCycle = predictedData.getRedCycle();
		Float greenCycle = predictedData.getGreenCycle();

		LocalDateTime now = LocalDateTime.now();

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
