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
public class ViewPointParam {
	/* 화면 좌측 하단 위도 */
	@LatParam private double vblLat;

	/* 화면 좌측 하단 경도 */
	@LngParam private double vblLng;

	/* 화면 우측 상단 위도 */
	@LatParam private double vtrLat;

	/* 화면 우측 상단 경도 */
	@LngParam private double vtrLng;
}
