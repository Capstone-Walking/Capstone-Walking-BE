package com.walking.api.web.controller.path;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.web.dto.request.OrderFilter;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import com.walking.api.web.dto.request.path.PatchFavoritePathNameBody;
import com.walking.api.web.dto.request.point.RoutePointParam;
import com.walking.api.web.dto.response.BrowseFavoriteRouteResponse;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.api.web.dto.response.detail.FavoritePointDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.api.web.service.path.ReadFavoritesPathService;
import com.walking.api.web.service.path.dto.response.ReadFavoritesPathResponse;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
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

	private final ReadFavoritesPathService readFavoritesPathService;

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
			@RequestParam(required = false, defaultValue = "order") OrderFilter filter,
			@RequestParam(required = false) Optional<String> name) {
		//		Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 1L;
		log.info("컨트롤러 진입");
		if (name.isPresent()) {

			List<ReadFavoritesPathResponse> favoritesPaths =
					readFavoritesPathService.execute(memberId, name.get());

			log.info("Favorite route browse request: name={}", name.get());
			BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}

		List<ReadFavoritesPathResponse> favoritesPaths =
				readFavoritesPathService.execute(memberId, filter);

		log.info("Favorite route browse request: filter={}", filter);
		BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.SuccessBody<RouteDetailResponse>> detailFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails, @PathVariable Long favoriteId) {
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
			@PathVariable Long favoriteId,
			@Valid @RequestBody PatchFavoritePathNameBody pathNameBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route update request: favoriteId={}, body={}", favoriteId, pathNameBody);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails, @PathVariable Long favoriteId) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite route delete request: favoriteId={}", favoriteId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private BrowseFavoriteRouteResponse getFavoriteRouteResponse(
			List<ReadFavoritesPathResponse> favoritesPaths) {
		// ReadFavoritesPathResponse 리스트를 FavoritePointDetail 리스트로 변환
		List<FavoritePointDetail> favoritePoints =
				favoritesPaths.stream()
						.map(
								path ->
										FavoritePointDetail.builder()
												.id(path.getId())
												.name(path.getName())
												.startPoint(
														new PointDetail(
																path.getStartPoint().getX(), path.getStartPoint().getY()))
												.endPoint(
														new PointDetail(path.getEndPoint().getX(), path.getEndPoint().getY()))
												.createdAt(path.getCreatedAt())
												.startAlias(path.getStartAlias())
												.endAlias(path.getEndAlias())
												.order(path.getOrder())
												.build())
						.collect(Collectors.toList());

		// BrowseFavoriteRouteResponse 객체 생성 및 반환
		return BrowseFavoriteRouteResponse.builder().favoritePaths(favoritePoints).build();
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

	private static BrowseFavoriteRouteResponse getFavoriteRouteResponse() {
		return BrowseFavoriteRouteResponse.builder()
				.favoritePaths(
						List.of(
								FavoritePointDetail.builder()
										.id(1L)
										.name("search")
										.startPoint(PointDetail.builder().lat(37.123).lng(127.123).build())
										.endPoint(PointDetail.builder().lat(37.456).lng(127.456).build())
										.createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
										.build()))
				.build();
	}

	private static BrowseFavoriteRouteResponse getFilterFavoriteRouteResponse() {
		return BrowseFavoriteRouteResponse.builder()
				.favoritePaths(
						List.of(
								FavoritePointDetail.builder()
										.id(1L)
										.name("test1")
										.startPoint(PointDetail.builder().lat(37.123).lng(127.123).build())
										.endPoint(PointDetail.builder().lat(37.456).lng(127.456).build())
										.createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
										.build(),
								FavoritePointDetail.builder()
										.id(2L)
										.name("test2")
										.startPoint(PointDetail.builder().lat(37.123).lng(127.123).build())
										.endPoint(PointDetail.builder().lat(37.456).lng(127.456).build())
										.createdAt(LocalDateTime.of(2021, 1, 2, 0, 0))
										.build()))
				.build();
	}
}
