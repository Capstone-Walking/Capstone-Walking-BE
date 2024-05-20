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
import com.walking.api.web.dto.response.detail.TrafficDetailInfo;
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

	static double TF_BACK_DOOR_LAT = 35.178501;
	static double TF_BACK_DOOR_LNG = 126.912083;
	static double TF_BACK_THREE_LAT = 35.175841;
	static double TF_BACK_THREE_LNG = 126.912491;
	static double TF_CHANPUNG_LAT = 35.180332;
	static double TF_CHANPUNG_LNG = 126.912123;
	static double TF_CUCU_LAT = 35.176495;
	static double TF_CUCU_LNG = 126.896888;

	static double GONG_SEVEN_LAT = 35.1782;
	static double GONG_SEVEN_LNG = 126.909;
	static double GIL_SUNG_UBU_LNG = 35.178600;
	static double GIL_SUNG_UBU_LAT = 126.912772;

	static double MAC_DONALD_LAT = 35.179374;
	static double MAC_DONALD_LNG = 126.912270;

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<SearchTrafficsResponse>> searchTraffics(
			@Valid OptionalViewPointParam viewPointParam,
			@Valid OptionalTrafficPointParam trafficPointParam) {
		if (!Objects.isNull(trafficPointParam) && trafficPointParam.isPresent()) {
			// todo implement: trafficPointParam를 이용하여 교차로 정보 조회
			log.info("Search traffics trafficPointParam: {}", trafficPointParam.get());
			SearchTrafficsResponse response = getSearchViewTrafficsResponse();
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

	private static SearchTrafficsResponse getSearchViewTrafficsResponse() {
		SearchTrafficsResponse response =
				SearchTrafficsResponse.builder()
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
												.point(
														PointDetail.builder()
																.lat(TF_BACK_DOOR_LAT)
																.lng(TF_BACK_DOOR_LNG)
																.build())
												.color("red")
												.remainTime(10L)
												.redCycle(30L)
												.greenCycle(30L)
												.build(),
										TrafficDetail.builder()
												.id(2L)
												.detail(
														TrafficDetailInfo.builder()
																.trafficId(2L)
																.apiSource("seoul")
																.direction("wt")
																.build())
												.isFavorite(false)
												.viewName("후문3거리")
												.point(
														PointDetail.builder()
																.lat(TF_BACK_THREE_LAT)
																.lng(TF_BACK_THREE_LNG)
																.build())
												.color("green")
												.remainTime(20L)
												.redCycle(30L)
												.greenCycle(30L)
												.build(),
										TrafficDetail.builder()
												.id(3L)
												.detail(
														TrafficDetailInfo.builder()
																.trafficId(3L)
																.apiSource("seoul")
																.direction("sw")
																.build())
												.isFavorite(true)
												.viewName("창평")
												.point(
														PointDetail.builder().lat(TF_CHANPUNG_LAT).lng(TF_CHANPUNG_LNG).build())
												.color("red")
												.remainTime(10L)
												.redCycle(30L)
												.greenCycle(30L)
												.build(),
										TrafficDetail.builder()
												.id(4L)
												.detail(
														TrafficDetailInfo.builder()
																.trafficId(4L)
																.apiSource("seoul")
																.direction("nt")
																.build())
												.isFavorite(false)
												.viewName("쿠쿠")
												.point(PointDetail.builder().lat(TF_CUCU_LAT).lng(TF_CUCU_LNG).build())
												.color("green")
												.remainTime(20L)
												.redCycle(30L)
												.greenCycle(30L)
												.build()))
						.build();

		return response;
	}

	private static SearchTrafficsResponse getSearchTrafficsResponse() {
		SearchTrafficsResponse response =
				SearchTrafficsResponse.builder()
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
												.point(
														PointDetail.builder()
																.lat(TF_BACK_DOOR_LAT)
																.lng(TF_BACK_DOOR_LNG)
																.build())
												.color("red")
												.remainTime(10L)
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
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(1L)
														.apiSource("seoul")
														.direction("nt")
														.build())
										.isFavorite(true)
										.viewName("후문")
										.point(
												PointDetail.builder().lat(TF_BACK_DOOR_LAT).lng(TF_BACK_DOOR_LNG).build())
										.color("red")
										.remainTime(10L)
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
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(1L)
														.apiSource("seoul")
														.direction("nt")
														.build())
										.name("후문")
										.point(
												PointDetail.builder().lat(TF_BACK_DOOR_LAT).lng(TF_BACK_DOOR_LNG).build())
										.createdAt(LocalDateTime.now())
										.build(),
								FavoriteTrafficDetail.builder()
										.id(2L)
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(2L)
														.apiSource("seoul")
														.direction("wt")
														.build())
										.name("후문3거리")
										.point(
												PointDetail.builder().lat(TF_BACK_THREE_LAT).lng(TF_BACK_THREE_LNG).build())
										.createdAt(LocalDateTime.now())
										.build(),
								FavoriteTrafficDetail.builder()
										.id(3L)
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(3L)
														.apiSource("seoul")
														.direction("sw")
														.build())
										.name("창평")
										.point(PointDetail.builder().lat(TF_CHANPUNG_LAT).lng(TF_CHANPUNG_LNG).build())
										.createdAt(LocalDateTime.now())
										.build(),
								FavoriteTrafficDetail.builder()
										.id(4L)
										.detail(
												TrafficDetailInfo.builder()
														.trafficId(4L)
														.apiSource("seoul")
														.direction("nt")
														.build())
										.name("쿠쿠")
										.point(PointDetail.builder().lat(TF_CUCU_LAT).lng(TF_CUCU_LNG).build())
										.createdAt(LocalDateTime.now())
										.build()))
				.build();
	}
}
