package com.walking.api.web.dto.request.point;

import com.walking.api.web.dto.request.validator.LatParam;
import com.walking.api.web.dto.request.validator.LngParam;
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
public class NowPointParam {

	/* 현재 위도 */
	@LatParam private double nowLat;

	/* 현재 경도 */
	@LngParam private double nowLng;
}
