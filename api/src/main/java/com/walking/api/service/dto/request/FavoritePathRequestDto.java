package com.walking.api.service.dto.request;

import com.walking.api.web.dto.request.validator.LatParam;
import com.walking.api.web.dto.request.validator.LngParam;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FavoritePathRequestDto {

	/* 즐겨찾기 경로 이름 */
	@Nullable private String name;

	@NotBlank private String startName;

	/* 시작점 위도 */
	@LatParam private double startLat;

	/* 시작점 경도 */
	@LngParam private double startLng;

	@NotBlank private String endName;

	/* 종료점 위도 */
	@LatParam private double endLat;

	/* 종료점 경도 */
	@LngParam private double endLng;
}
