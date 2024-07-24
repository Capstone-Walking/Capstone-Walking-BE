package com.walking.api.domain.path.dto;

import com.walking.api.traffic.dto.detail.PointDetail;
import com.walking.api.traffic.dto.detail.TrafficDetail;
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
public class CalculatePathFavoritesTimeUseCaseOut {
	private LocalDateTime nowTime;
	private Integer totalTime;
	private Integer trafficCount;
	private List<LocalDateTime> departureTimes;
	private Integer timeToFirstTraffic;
	private Integer totalDistance;
	private PointDetail startPoint;
	private PointDetail endPoint;
	private List<TrafficDetail> traffics;
	private List<Long> trafficIdsInPath;
	private List<PointDetail> paths;
}
