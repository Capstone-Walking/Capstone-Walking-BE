package com.walking.api.web.controller.traffic;

import com.walking.api.domain.traffic.dto.AddFavoriteTrafficUseCaseRequest;
import com.walking.api.domain.traffic.dto.BrowseFavoriteTrafficsUseCaseRequest;
import com.walking.api.domain.traffic.dto.BrowseTrafficsUseCaseRequest;
import com.walking.api.domain.traffic.dto.BrowseTrafficsUseCaseResponse;
import com.walking.api.domain.traffic.dto.DeleteFavoriteTrafficUseCaseRequest;
import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseRequest;
import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseResponse;
import com.walking.api.domain.traffic.dto.UpdateFavoriteTrafficUseCaseRequest;
import com.walking.api.domain.traffic.usecase.AddFavoriteTrafficUseCase;
import com.walking.api.domain.traffic.usecase.BrowseFavoriteTrafficsUseCase;
import com.walking.api.domain.traffic.usecase.DeleteFavoriteTrafficUseCase;
import com.walking.api.domain.traffic.usecase.ReadTrafficsUseCase;
import com.walking.api.domain.traffic.usecase.SearchTrafficsUseCase;
import com.walking.api.domain.traffic.usecase.UpdateFavoriteTrafficUseCase;
import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.security.authentication.token.TokenUserDetailsService;
import com.walking.api.security.filter.token.AccessTokenResolver;
import com.walking.api.web.dto.request.point.ViewPointParam;
import com.walking.api.web.dto.request.traffic.FavoriteTrafficBody;
import com.walking.api.web.dto.request.traffic.PatchFavoriteTrafficNameBody;
import com.walking.api.web.dto.response.BrowseFavoriteTrafficsResponse;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

	private final TokenUserDetailsService tokenUserDetailsService;

	private final SearchTrafficsUseCase searchTrafficsUseCase;
	private final ReadTrafficsUseCase readTrafficsUseCase;

	private final AddFavoriteTrafficUseCase addFavoriteTrafficUseCase;
	private final BrowseFavoriteTrafficsUseCase browseFavoriteTrafficsUseCase;
	private final DeleteFavoriteTrafficUseCase deleteFavoriteTrafficUseCase;
	private final UpdateFavoriteTrafficUseCase updateFavoriteTrafficUseCase;

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<SearchTrafficsUseCaseResponse>> searchTraffics(
			@Valid ViewPointParam viewPointParam) {
		SearchTrafficsUseCaseRequest request =
				SearchTrafficsUseCaseRequest.builder()
						.vblLng(viewPointParam.getVblLng())
						.vblLat(viewPointParam.getVblLat())
						.vtrLng(viewPointParam.getVtrLng())
						.vtrLat(viewPointParam.getVtrLat())
						.build();
		SearchTrafficsUseCaseResponse response = searchTrafficsUseCase.execute(request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/{trafficId}")
	public ApiResponse<ApiResponse.SuccessBody<BrowseTrafficsUseCaseResponse>> readTraffic(
			HttpServletRequest request, @PathVariable Long trafficId) {
		BrowseTrafficsUseCaseRequest useCaseRequest =
				BrowseTrafficsUseCaseRequest.builder().trafficId(trafficId).build();
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		BrowseTrafficsUseCaseResponse response = null;
		if (Objects.nonNull(authorization)) {
			String token = AccessTokenResolver.resolve(authorization);
			UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(token);
			Long memberId = Long.valueOf(userDetails.getUsername());
			useCaseRequest.setMemberId(memberId);
		}
		response = readTrafficsUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/favorite")
	public ApiResponse<ApiResponse.Success> addFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody FavoriteTrafficBody favoriteTrafficBody) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		addFavoriteTrafficUseCase.execute(
				AddFavoriteTrafficUseCaseRequest.builder()
						.memberId(memberId)
						.trafficId(favoriteTrafficBody.getTrafficId())
						.trafficAlias(favoriteTrafficBody.getTrafficAlias())
						.build());
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteTrafficsResponse>>
			browseFavoriteTraffics(HttpServletRequest request) {
		Long memberId = null;
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (Objects.nonNull(authorization)) {
			String token = AccessTokenResolver.resolve(authorization);
			UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(token);
			memberId = Long.valueOf(userDetails.getUsername());
		} else {
			throw new IllegalArgumentException("Authorization header is required.");
		}

		BrowseFavoriteTrafficsUseCaseRequest useCaseRequest =
				BrowseFavoriteTrafficsUseCaseRequest.builder().memberId(memberId).build();
		BrowseFavoriteTrafficsResponse response = browseFavoriteTrafficsUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long trafficId,
			@Valid @RequestBody PatchFavoriteTrafficNameBody patchFavoriteTrafficNameBody) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		updateFavoriteTrafficUseCase.execute(
				UpdateFavoriteTrafficUseCaseRequest.builder()
						.memberId(memberId)
						.favoriteTrafficId(trafficId)
						.trafficAlias(patchFavoriteTrafficNameBody.getTrafficAlias())
						.build());
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails, @Min(1) @PathVariable Long trafficId) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		deleteFavoriteTrafficUseCase.execute(
				DeleteFavoriteTrafficUseCaseRequest.builder()
						.memberId(memberId)
						.favoriteTrafficId(trafficId)
						.build());
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}
}
