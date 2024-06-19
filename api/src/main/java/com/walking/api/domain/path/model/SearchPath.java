package com.walking.api.domain.path.model;

import com.walking.api.domain.client.dto.response.TMapResponseDto;
import com.walking.api.domain.client.dto.response.detail.FeatureDetail;
import com.walking.api.domain.client.dto.response.detail.GeometryDetail;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.*;

@AllArgsConstructor
public class SearchPath {

	private final TMapResponseDto tMapPathData;

	@Data
	@AllArgsConstructor
	public static class PathPrimaryVO {
		private Integer totalTime;
		private Integer untilTrafficTime;
		private Integer totalDistance;
	}

	public PathPrimaryVO extractPrimaryDataByTMap() {
		return new PathPrimaryVO(
				calculateTotalTime(tMapPathData),
				calculateUntilFirstTraffic(tMapPathData),
				tMapPathData.getFeatureDetails().get(0).getPropertyDetails().getTotalDistance());
	}

	public List<Point> extractAllTrafficPoints() {
		List<Point> points = new ArrayList<>();
		// TMapResponseDto 객체에서 Feature 리스트를 반복하며 각 Feature의 Geometry를 검사합니다.
		for (FeatureDetail featureDetail : tMapPathData.getFeatureDetails()) {
			GeometryDetail geometryDetail = featureDetail.getGeometryDetail();

			// 신호등은 LineString으로 출발점과 도착점을 가진다.
			if (featureDetail.getPropertyDetails().getFacilityType().equals("15")
					&& "LineString".equals(geometryDetail.getType())) {
				List<List<Double>> coordinates = (List<List<Double>>) geometryDetail.getCoordinates();
				// Point 타입의 좌표 처리
				points.add(
						createPoint(
								(coordinates.get(0).get(0) + coordinates.get(1).get(0)) / 2,
								(coordinates.get(0).get(1) + coordinates.get(1).get(1)) / 2));
			}
		}
		return points;
	}

	private Integer calculateTotalTime(TMapResponseDto tMapPathData) {
		return tMapPathData.getFeatureDetails().get(0).getPropertyDetails().getTotalTime();
	}

	private Integer calculateUntilFirstTraffic(TMapResponseDto tMapPathData) {
		Integer untilFirstTraffic = 0;

		for (int i = 1; i < tMapPathData.getFeatureDetails().size(); i++) {
			FeatureDetail nowFeatureDetail = tMapPathData.getFeatureDetails().get(i);
			if (nowFeatureDetail.getPropertyDetails().getTime() == null) {
				continue;
			}
			untilFirstTraffic += nowFeatureDetail.getPropertyDetails().getTime();
			if (nowFeatureDetail.getPropertyDetails().getFacilityType().equals("15")) { // 신호등일경우

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

		for (FeatureDetail featureDetail : tMapPathData.getFeatureDetails()) {
			if ("LineString".equals(featureDetail.getGeometryDetail().getType())) {
				List<?> points = featureDetail.getGeometryDetail().getCoordinates();
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
