package com.walking.api.web.controller.path;

import com.walking.api.domain.path.dto.CalculatePathTimeUseCaseResponse;
import com.walking.api.domain.path.dto.ReadFavoritesPathUseCaseResponse;
import com.walking.api.domain.path.dto.SavePathFavoritesUseCaseRequest;
import com.walking.api.domain.path.dto.UpdateRoutePathNameUseCaseRequest;
import com.walking.api.domain.path.usecase.CalculatePathFavoritesTimeUseCase;
import com.walking.api.domain.path.usecase.CalculatePathTimeUseCase;
import com.walking.api.domain.path.usecase.DeleteFavoriteRouteUseCase;
import com.walking.api.domain.path.usecase.ReadFavoritesPathUseCase;
import com.walking.api.domain.path.usecase.SavePathFavoritesUseCase;
import com.walking.api.domain.path.usecase.UpdateRoutePathNameUseCase;
import com.walking.api.domain.traffic.dto.detail.FavoriteRouteDetail;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.web.dto.request.OrderFilter;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import com.walking.api.web.dto.request.path.PatchFavoritePathNameBody;
import com.walking.api.web.dto.request.point.RoutePointParam;
import com.walking.api.web.dto.response.route.BrowseFavoriteRouteResponse;
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

	private final CalculatePathTimeUseCase calculatePathTimeUseCase;
	private final SavePathFavoritesUseCase savePathFavoritesUseCase;
	private final CalculatePathFavoritesTimeUseCase calculatePathFavoritesTimeUseCase;
	private final ReadFavoritesPathUseCase readFavoritesPathUseCase;
	private final UpdateRoutePathNameUseCase updateRoutePathNameUseCase;
	private final DeleteFavoriteRouteUseCase deleteFavoriteRouteUseCase;

	@GetMapping("/detail")
	public ApiResponse<ApiResponse.SuccessBody<CalculatePathTimeUseCaseResponse>> detailRoute(
			@Valid RoutePointParam routePointParam) {
		CalculatePathTimeUseCaseResponse response =
				calculatePathTimeUseCase.execute(
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
		savePathFavoritesUseCase.execute(
				SavePathFavoritesUseCaseRequest.builder()
						.memberId(memberId)
						.name(favoritePathBody.getName())
						.startLat(favoritePathBody.getStartLat())
						.startLng(favoritePathBody.getStartLng())
						.endLat(favoritePathBody.getEndLat())
						.endLng(favoritePathBody.getEndLng())
						.startName(favoritePathBody.getStartName())
						.endName(favoritePathBody.getEndName())
						.build());
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteRouteResponse>> browseFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam(required = true, defaultValue = "createdAt") OrderFilter filter,
			@RequestParam(required = false) Optional<String> name) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		if (name.isPresent()) {
			List<ReadFavoritesPathUseCaseResponse> favoritesPaths =
					readFavoritesPathUseCase.execute(memberId, name.get());
			BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}

		// todo implement: filter 기준 정렬
		List<ReadFavoritesPathUseCaseResponse> favoritesPaths =
				readFavoritesPathUseCase.execute(memberId, filter);
		BrowseFavoriteRouteResponse response = getFavoriteRouteResponse(favoritesPaths);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.SuccessBody<CalculatePathTimeUseCaseResponse>> detailFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {
		CalculatePathTimeUseCaseResponse response =
				calculatePathFavoritesTimeUseCase.execute(favoriteId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId,
			@Valid @RequestBody PatchFavoritePathNameBody pathNameBody) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		updateRoutePathNameUseCase.execute(
				UpdateRoutePathNameUseCaseRequest.builder()
						.memberId(memberId)
						.pathId(favoriteId)
						.name(pathNameBody.getName())
						.startAlias(pathNameBody.getStartAlias())
						.endAlias(pathNameBody.getEndAlias())
						.build());
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{favoriteId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteRoute(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long favoriteId) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		deleteFavoriteRouteUseCase.execute(memberId, favoriteId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private BrowseFavoriteRouteResponse getFavoriteRouteResponse(
			List<ReadFavoritesPathUseCaseResponse> favoritesPaths) {
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
