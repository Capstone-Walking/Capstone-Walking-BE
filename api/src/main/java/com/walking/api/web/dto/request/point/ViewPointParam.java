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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ViewPointParam {
	/* 화면 좌측 하단 위도 */
	@LatParam private float vblLat;

	/* 화면 좌측 하단 경도 */
	@LngParam private float vblLng;

	/* 화면 우측 상단 위도 */
	@LatParam private float vtrLat;

	/* 화면 우측 상단 경도 */
	@LngParam private float vtrLng;
}
