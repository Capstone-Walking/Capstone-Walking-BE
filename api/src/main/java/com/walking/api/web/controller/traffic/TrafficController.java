package com.walking.api.web.controller.traffic;

import com.walking.api.web.dto.request.point.TrafficPointParam;
import com.walking.api.web.dto.request.point.ViewPointParam;
import com.walking.api.web.dto.response.SearchTrafficsResponse;
import com.walking.api.web.dto.response.detail.IntersectionDetail;
import com.walking.api.web.dto.response.detail.IntersectionTrafficDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
			@Valid Optional<ViewPointParam> viewPointParam,
			@Valid Optional<TrafficPointParam> trafficPointParam) {
		if (trafficPointParam.isPresent()) {
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

	private static SearchTrafficsResponse getSearchTrafficsResponse() {
		SearchTrafficsResponse response =
				SearchTrafficsResponse.builder()
						.intersections(
								List.of(
										IntersectionDetail.builder()
												.id(1L)
												.name("test1")
												.point(PointDetail.builder().lat(33.123456).lng(124.123456).build())
												.isFavorite(true)
												.alias("alias1")
												.traffics(
														List.of(
																IntersectionTrafficDetail.builder()
																		.direction("nt")
																		.status(true)
																		.remainTime(10L)
																		.redCycle(20L)
																		.greenCycle(30L)
																		.build(),
																IntersectionTrafficDetail.builder()
																		.direction("st")
																		.status(false)
																		.remainTime(20L)
																		.redCycle(30L)
																		.greenCycle(40L)
																		.build()))
												.build()))
						.build();
		return response;
	}
}
