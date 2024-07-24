package com.walking.api.data.entity.path;

import org.locationtech.jts.geom.Point;

public enum TrafficDirection {
	nt,
	st,
	et,
	wt,
	ne,
	se,
	sw,
	nw;

	public static TrafficDirection findDirection(Point crossPoint, Point trafficPoint) {

		// 라디안으로 변환
		double dLat = Math.toRadians(trafficPoint.getY() - crossPoint.getY());
		double dLon = Math.toRadians(trafficPoint.getX() - crossPoint.getX());

		// y, x 성분 계산
		double y = Math.sin(dLon) * Math.cos(Math.toRadians(trafficPoint.getY()));
		double x =
				Math.cos(Math.toRadians(crossPoint.getY())) * Math.sin(Math.toRadians(trafficPoint.getY()))
						- Math.sin(Math.toRadians(crossPoint.getY()))
								* Math.cos(Math.toRadians(trafficPoint.getY()))
								* Math.cos(dLon);

		// 방위각 계산
		double bearing = Math.toDegrees(Math.atan2(y, x));
		bearing = (bearing + 360) % 360;

		// 8방위로 매핑
		if (bearing < 22.5) return nt;
		else if (bearing < 67.5) return ne;
		else if (bearing < 112.5) return et;
		else if (bearing < 157.5) return se;
		else if (bearing < 202.5) return st;
		else if (bearing < 247.5) return sw;
		else if (bearing < 292.5) return wt;
		else if (bearing < 337.5) return nw;
		else return nt;
	}
}
