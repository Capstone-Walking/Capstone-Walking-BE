package com.walking.api.web.controller.traffic;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.web.dto.request.point.OptionalTrafficPointParam;
import com.walking.api.web.dto.request.point.OptionalViewPointParam;
import com.walking.api.web.dto.request.traffic.FavoriteTrafficBody;
import com.walking.api.web.dto.request.traffic.PatchFavoriteTrafficNameBody;
import com.walking.api.web.dto.response.BrowseFavoriteTrafficsResponse;
import com.walking.api.web.dto.response.BrowseTrafficsResponse;
import com.walking.api.web.dto.response.SearchTrafficsResponse;
import com.walking.api.web.dto.response.detail.FavoriteTrafficDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetail;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/traffics")
@RequiredArgsConstructor
public class TrafficController {

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<SearchTrafficsResponse>> searchTraffics(
			@Valid OptionalViewPointParam viewPointParam,
			@Valid OptionalTrafficPointParam trafficPointParam) {
		if (!Objects.isNull(trafficPointParam) && trafficPointParam.isPresent()) {
			// todo implement: trafficPointParam를 이용하여 교차로 정보 조회
			log.info("Search traffics trafficPointParam: {}", trafficPointParam.get());
			SearchTrafficsResponse response = getSearchTrafficsResponse();
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}

		// todo implement: viewPointParam을 이용하여 교차로 정보 조회
		log.info("Search traffics viewPointParam: {}", viewPointParam.get());
		SearchTrafficsResponse response = getSearchTrafficsResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/{trafficId}")
	public ApiResponse<ApiResponse.SuccessBody<BrowseTrafficsResponse>> browseTraffic(
			@PathVariable Long trafficId) {
		// todo implement
		log.info("Traffic browse trafficId: {}", trafficId);
		BrowseTrafficsResponse response = getBrowseTrafficsResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/favorite")
	public ApiResponse<ApiResponse.Success> addFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody FavoriteTrafficBody favoriteTrafficBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Favorite traffic request: {}", favoriteTrafficBody);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteTrafficsResponse>>
			browseFavoriteTraffics(@AuthenticationPrincipal TokenUserDetails userDetails) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		BrowseFavoriteTrafficsResponse response = getBrowseFavoriteTrafficsResponse();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long trafficId,
			@Valid @RequestBody PatchFavoriteTrafficNameBody patchFavoriteTrafficNameBody) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info(
				"Update favorite traffic request: trafficId={}, body={}",
				trafficId,
				patchFavoriteTrafficNameBody);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails, @Min(1) @PathVariable Long trafficId) {
		// todo implement
		// Long memberId = Long.valueOf(userDetails.getUsername());
		Long memberId = 999L;
		log.info("Delete favorite traffic request: trafficId={}", trafficId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private static SearchTrafficsResponse getSearchTrafficsResponse() {
		SearchTrafficsResponse response =
				SearchTrafficsResponse.builder()
						.traffics(
								List.of(
										TrafficDetail.builder()
												.id(1L)
												.detail("intersection-1-detail")
												.isFavorite(true)
												.viewName("test1")
												.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
												.color("red")
												.timeLeft(10L)
												.redCycle(30L)
												.greenCycle(30L)
												.build(),
										TrafficDetail.builder()
												.id(2L)
												.detail("intersection-2-detail")
												.isFavorite(false)
												.viewName("test2")
												.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
												.color("green")
												.timeLeft(20L)
												.redCycle(30L)
												.greenCycle(30L)
												.build()))
						.build();

		return response;
	}

	private static BrowseTrafficsResponse getBrowseTrafficsResponse() {
		BrowseTrafficsResponse response =
				BrowseTrafficsResponse.builder()
						.traffic(
								TrafficDetail.builder()
										.id(1L)
										.detail("intersection-1-detail")
										.isFavorite(true)
										.viewName("test1")
										.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
										.color("red")
										.timeLeft(10L)
										.redCycle(30L)
										.greenCycle(30L)
										.build())
						.build();

		return response;
	}

	private static BrowseFavoriteTrafficsResponse getBrowseFavoriteTrafficsResponse() {
		return BrowseFavoriteTrafficsResponse.builder()
				.traffics(
						List.of(
								FavoriteTrafficDetail.builder()
										.id(1L)
										.detail("intersection-1-detail")
										.name("test1")
										.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
										.createdAt(LocalDateTime.now())
										.build(),
								FavoriteTrafficDetail.builder()
										.id(2L)
										.detail("intersection-2-detail")
										.name("test2")
										.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
										.createdAt(LocalDateTime.now())
										.build()))
				.build();
	}
}
