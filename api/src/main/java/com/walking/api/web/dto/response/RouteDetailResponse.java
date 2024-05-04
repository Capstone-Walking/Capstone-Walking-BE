package com.walking.api.web.dto.response;

import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDetailResponse {

	public Long totalTime;
	public Long trafficCount;
	public PointDetail startPoint;
	public PointDetail endPoint;
	public List<TrafficDetail> traffics;
	public List<PointDetail> paths;
}
