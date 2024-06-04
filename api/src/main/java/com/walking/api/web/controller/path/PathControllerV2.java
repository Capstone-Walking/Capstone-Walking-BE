package com.walking.api.web.controller.path;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.service.dto.request.FavoritePathRequestDto;
import com.walking.api.service.dto.request.PathFavoriteNameRequest;
import com.walking.api.service.dto.response.ReadFavoritesPathResponse;
import com.walking.api.service.path.*;
import com.walking.api.web.dto.request.OrderFilter;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import com.walking.api.web.dto.request.path.PatchFavoritePathNameBody;
import com.walking.api.web.dto.request.point.RoutePointParam;
import com.walking.api.web.dto.response.BrowseFavoriteRouteResponse;
import com.walking.api.web.dto.response.RouteDetailResponse;
import com.walking.api.web.dto.response.detail.FavoriteRouteDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v2/paths")
@RequiredArgsConstructor
public class PathControllerV2 {

	private final CalculatePathTimeService calculatePathTimeService;
	private final SavePathFavoritesService savePathFavoritesService;
	private final CalculatePathFavoritesTimeService calculatePathFavoritesTimeService;
	private final ReadFavoritesPathService readFavoritesPathService;
	private final UpdateRoutePathNameService updateRoutePathNameService;
	private final DeleteFavoriteRouteService deleteFavoriteRouteService;

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
		RouteDetailResponse response =
				calculatePathTimeService.execute(
						routePointParam.getStartLat(),
						routePointParam.getStartLng(),
						routePointParam.getEndLat(),
						routePointParam.getEndLng());

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/favorite")
	public ApiResponse<ApiResponse.Success> addFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody FavoritePathBody favoritePathBody) {

		Long memberId = Long.valueOf(userDetails.getUsername());

		log.info("Favorite route request: {}", favoritePathBody);
		savePathFavoritesService.execute(
				FavoritePathRequestDto.builder()
						.name(favoritePathBody.getName())
						.startLat(favoritePathBody.getStartLat())
						.startLng(favoritePathBody.getStartLng())
						.endLat(favoritePathBody.getEndLat())
						.endLng(favoritePathBody.getEndLng())
						.startName(favoritePathBody.getStartName())
						.endName(favoritePathBody.getEndName())
						.build(),
				memberId);

		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteRouteResponse>> browseFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam(required = true, defaultValue = "createdAt") OrderFilter filter,
			@RequestParam(required = false) Optional<String> name) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		if (name.isPresent()) {
			// todo implement: name 기준 검색
			log.info("Favorite route browse request: name={}", name.get());
			List<ReadFavoritesPathResponse> favoritesPaths =
					readFavoritesPathService.execute(memberId, name.get());

			BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}

		// todo implement: filter 기준 정렬
		List<ReadFavoritesPathResponse> favoritesPaths =
				readFavoritesPathService.execute(memberId, filter);
		log.info("Favorite route browse request: filter={}", filter);
		BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.SuccessBody<RouteDetailResponse>> detailFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {
		log.info("Favorite route detail request: favoriteId={}", favoriteId);

		RouteDetailResponse response = calculatePathFavoritesTimeService.execute(favoriteId);

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId,
			@Valid @RequestBody PatchFavoritePathNameBody pathNameBody) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		updateRoutePathNameService.execute(
				memberId,
				favoriteId,
				PathFavoriteNameRequest.builder()
						.name(pathNameBody.getName())
						.startAlias(pathNameBody.getStartAlias())
						.endAlias(pathNameBody.getEndAlias())
						.build());
		log.info("Favorite route update request: favoriteId={}, body={}", favoriteId, pathNameBody);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {

		Long memberId = Long.valueOf(userDetails.getUsername());
		deleteFavoriteRouteService.execute(memberId, favoriteId);

		log.info("Favorite route delete request: favoriteId={}", favoriteId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private BrowseFavoriteRouteResponse getFavoriteRouteResponse(
			List<ReadFavoritesPathResponse> favoritesPaths) {
		List<FavoriteRouteDetail> favoritePoints =
				favoritesPaths.stream()
						.map(
								path ->
										FavoriteRouteDetail.builder()
												.id(path.getId())
												.name(path.getName())
												.startPoint(
														PointDetail.builder()
																.lat(path.getStartPoint().getX())
																.lng(path.getStartPoint().getY())
																.build())
												.endPoint(
														PointDetail.builder()
																.lat(path.getEndPoint().getX())
																.lng(path.getEndPoint().getY())
																.build())
												.createdAt(path.getCreatedAt())
												.startAlias(path.getStartAlias())
												.endAlias(path.getEndAlias())
												.order(path.getOrder())
												.build())
						.collect(Collectors.toList());

		// BrowseFavoriteRouteResponse 객체 생성 및 반환
		return BrowseFavoriteRouteResponse.builder().favoriteRoutes(favoritePoints).build();
	}
}
