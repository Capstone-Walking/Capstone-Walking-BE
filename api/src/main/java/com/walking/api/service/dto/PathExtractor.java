package com.walking.api.service.dto;

import com.walking.api.web.client.dto.response.TMapResponseDto;
import com.walking.api.web.client.dto.response.detail.Feature;
import com.walking.api.web.client.dto.response.detail.Geometry;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.*;

@AllArgsConstructor
public class PathExtractor {

	private final TMapResponseDto tMapPathData;

	public PathPrimaryData extractPrimaryDataByTMap() {

		return new PathPrimaryData(
				calculateTotalTime(tMapPathData),
				calculateUntilFirstTraffic(tMapPathData),
				tMapPathData.getFeatures().get(0).getProperties().getTotalDistance());
	}

	public List<Point> extractAllTrafficPoints() {
		List<Point> points = new ArrayList<>();

		// TMapResponseDto 객체에서 Feature 리스트를 반복하며 각 Feature의 Geometry를 검사합니다.
		for (Feature feature : tMapPathData.getFeatures()) {
			Geometry geometry = feature.getGeometry();

			// 신호등은 LineString으로 출발점과 도착점을 가진다.
			if (feature.getProperties().getFacilityType().equals("15")
					&& "LineString".equals(geometry.getType())) {
				List<List<Double>> coordinates = (List<List<Double>>) geometry.getCoordinates();
				// Point 타입의 좌표 처리
				points.add(
						createPoint(
								(coordinates.get(0).get(0) + coordinates.get(1).get(0)) / 2,
								(coordinates.get(0).get(1) + coordinates.get(1).get(1)) / 2));
				// trafficTypes.add(TrafficDirection.findByNumber(feature.getProperties().getTurnType()));
			}
		}

		return points;
	}

	private Integer calculateTotalTime(TMapResponseDto tMapPathData) {

		return tMapPathData.getFeatures().get(0).getProperties().getTotalTime();
	}

	private Integer calculateUntilFirstTraffic(TMapResponseDto tMapPathData) {
		Integer untilFirstTraffic = 0;

		for (int i = 1; i < tMapPathData.getFeatures().size(); i++) {
			Feature nowFeature = tMapPathData.getFeatures().get(i);
			if (nowFeature.getProperties().getTime() == null) {
				continue;
			}
			untilFirstTraffic += nowFeature.getProperties().getTime();
			if (nowFeature.getProperties().getFacilityType().equals("15")) { // 신호등일경우

				return untilFirstTraffic;
			}
		}

		return untilFirstTraffic;
	}

	private Point createPoint(double lng, double lat) {
		GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
		return gf.createPoint(new Coordinate(lng, lat));
	}

	public LineString extractLineString() {
		List<Coordinate> coordinates = new ArrayList<>();
		GeometryFactory geometryFactory = new GeometryFactory();

		for (Feature feature : tMapPathData.getFeatures()) {
			if ("LineString".equals(feature.getGeometry().getType())) {
				List<?> points = feature.getGeometry().getCoordinates();
				for (Object point : points) {
					List<Double> nowPoint = (List<Double>) point;
					coordinates.add(new Coordinate(nowPoint.get(0), nowPoint.get(1)));
				}
			}
		}

		if (coordinates.isEmpty()) {
			return null;
		}

		Coordinate[] coordinatesArray = coordinates.toArray(new Coordinate[0]);
		return geometryFactory.createLineString(coordinatesArray);
	}
}
