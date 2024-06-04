package com.walking.api.web.dto.response;

import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RouteDetailResponse {

	public LocalDateTime nowTime;
	public Integer totalTime;
	public Integer trafficCount;
	public List<LocalDateTime> departureTimes;
	public Integer timeToFirstTraffic;
	public Integer totalDistance;
	public PointDetail startPoint;
	public PointDetail endPoint;
	public List<TrafficDetail> traffics;
	public List<Long> trafficIdsInPath;
	public List<PointDetail> paths;
}
