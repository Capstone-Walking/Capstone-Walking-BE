package com.walking.api.web.controller.path;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.web.dto.request.point.FavoritePathBody;
import com.walking.api.web.dto.request.point.RoutePointParam;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/paths")
@RequiredArgsConstructor
public class PathController {

	@GetMapping("/detail")
	public ApiResponse<ApiResponse.SuccessBody<RouteDetailResponse>> detailRoute(
			@Valid RoutePointParam routePointParam) {
		// todo implement
		log.info("Route detail request: {}", routePointParam);
		RouteDetailResponse response = getSampleRouteDetailResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/favorite")
	public ApiResponse<ApiResponse.Success> addFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid FavoritePathBody favoritePathBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route request: {}", favoritePathBody);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	private static RouteDetailResponse getSampleRouteDetailResponse() {
		return RouteDetailResponse.builder()
				.totalTime(100L)
				.trafficCount(10L)
				.startPoint(PointDetail.builder().lat(37.123).lng(127.123).build())
				.endPoint(PointDetail.builder().lat(37.456).lng(127.456).build())
				.traffics(
						List.of(
								TrafficDetail.builder()
										.id(1L)
										.state("RED")
										.remainTime(10L)
										.greenCycle(30L)
										.point(PointDetail.builder().lat(37.123).lng(127.123).build())
										.build(),
								TrafficDetail.builder()
										.id(2L)
										.state("GREEN")
										.remainTime(20L)
										.greenCycle(30L)
										.point(PointDetail.builder().lat(37.456).lng(127.456).build())
										.build()))
				.paths(
						List.of(
								PointDetail.builder().lat(37.123).lng(127.123).build(),
								PointDetail.builder().lat(37.456).lng(127.456).build()))
				.build();
	}
}
