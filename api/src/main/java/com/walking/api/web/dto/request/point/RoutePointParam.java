package com.walking.api.web.dto.request.point;

import com.walking.api.web.dto.request.validator.LatParam;
import com.walking.api.web.dto.request.validator.LngParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutePointParam {

	/* 시작점 위도 */
	@LatParam private double startLat;

	/* 시작점 경도 */
	@LngParam private double startLng;

	/* 종료점 위도 */
	@LatParam private double endLat;

	/* 종료점 경도 */
	@LngParam private double endLng;
}
