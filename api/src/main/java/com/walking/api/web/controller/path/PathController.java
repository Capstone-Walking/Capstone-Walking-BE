package com.walking.api.web.controller.path;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.web.dto.request.OrderFilter;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import com.walking.api.web.dto.request.path.PatchFavoritePathNameBody;
import com.walking.api.web.dto.request.point.RoutePointParam;
import com.walking.api.web.dto.response.BrowseFavoriteRouteResponse;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.api.web.dto.response.detail.FavoriteRouteDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.api.web.dto.response.detail.TrafficDetailInfo;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/paths")
@RequiredArgsConstructor
public class PathController {

	static double GONG_SEVEN_LAT = 35.1782;
	static double GONG_SEVEN_LNG = 126.909;
	static double BACK_DOOR_LAT = 35.178501;
	static double BACK_DOOR_LNG = 126.912083;
	static double GIL_SUNG_UBU_LNG = 35.178600;
	static double GIL_SUNG_UBU_LAT = 126.912772;

	static double MAC_DONALD_LAT = 35.179374;
	static double MAC_DONALD_LNG = 126.912270;

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
			@Valid @RequestBody FavoritePathBody favoritePathBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route request: {}", favoritePathBody);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteRouteResponse>> browseFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam(required = true, defaultValue = "createdAt") OrderFilter filter,
			@RequestParam(required = false) Optional<String> name) {
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		if (name.isPresent()) {
			// todo implement: name 기준 검색
			log.info("Favorite route browse request: name={}", name.get());
			BrowseFavoriteRouteResponse response = getSearchFavoriteRouteResponse();
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}

		// todo implement: filter 기준 정렬
		log.info("Favorite route browse request: filter={}", filter);
		BrowseFavoriteRouteResponse response = getFilterFavoriteRouteResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.SuccessBody<RouteDetailResponse>> detailFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route detail request: favoriteId={}", favoriteId);
		RouteDetailResponse response = getSampleRouteDetailResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId,
			@Valid @RequestBody PatchFavoritePathNameBody pathNameBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route update request: favoriteId={}, body={}", favoriteId, pathNameBody);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route delete request: favoriteId={}", favoriteId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private static RouteDetailResponse getSampleRouteDetailResponse() {
		return RouteDetailResponse.builder()
				.totalTime(100)
				.trafficCount(10)
				.startPoint(PointDetail.builder().lat(GONG_SEVEN_LAT).lng(GONG_SEVEN_LNG).build())
				.endPoint(PointDetail.builder().lat(GIL_SUNG_UBU_LAT).lng(GIL_SUNG_UBU_LNG).build())
				.traffics(
						List.of(
								TrafficDetail.builder()
										.id(1L)
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(1L)
														.apiSource("seoul")
														.direction("nt")
														.build())
										.isFavorite(true)
										.viewName("후문")
										.point(PointDetail.builder().lat(BACK_DOOR_LAT).lng(BACK_DOOR_LNG).build())
										.color("red")
										.timeLeft(10f)
										.redCycle(30f)
										.greenCycle(30f)
										.build()))
				.paths(
						List.of(
								PointDetail.builder().lat(GONG_SEVEN_LAT).lng(GONG_SEVEN_LNG).build(),
								PointDetail.builder().lat(BACK_DOOR_LAT).lng(BACK_DOOR_LNG).build(),
								PointDetail.builder().lat(GIL_SUNG_UBU_LAT).lng(GIL_SUNG_UBU_LNG).build()))
				.build();
	}

	private static BrowseFavoriteRouteResponse getSearchFavoriteRouteResponse() {
		return BrowseFavoriteRouteResponse.builder()
				.favoriteRoutes(
						List.of(
								FavoriteRouteDetail.builder()
										.id(1L)
										.name("공7-길성우부")
										.startPoint(
												PointDetail.builder().lat(GONG_SEVEN_LAT).lng(GONG_SEVEN_LNG).build())
										.endPoint(
												PointDetail.builder().lat(GIL_SUNG_UBU_LAT).lng(GIL_SUNG_UBU_LNG).build())
										.createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
										.build()))
				.build();
	}

	private static BrowseFavoriteRouteResponse getFilterFavoriteRouteResponse() {
		return BrowseFavoriteRouteResponse.builder()
				.favoriteRoutes(
						List.of(
								FavoriteRouteDetail.builder()
										.id(1L)
										.name("공7-길성우부")
										.startPoint(
												PointDetail.builder().lat(GONG_SEVEN_LAT).lng(GONG_SEVEN_LNG).build())
										.endPoint(
												PointDetail.builder().lat(GIL_SUNG_UBU_LAT).lng(GIL_SUNG_UBU_LNG).build())
										.createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
										.build(),
								FavoriteRouteDetail.builder()
										.id(2L)
										.name("공7-맥도날드")
										.startPoint(
												PointDetail.builder().lat(GONG_SEVEN_LAT).lng(GONG_SEVEN_LNG).build())
										.endPoint(PointDetail.builder().lat(MAC_DONALD_LAT).lng(MAC_DONALD_LNG).build())
										.createdAt(LocalDateTime.of(2021, 1, 2, 0, 0))
										.build()))
				.build();
	}
}
