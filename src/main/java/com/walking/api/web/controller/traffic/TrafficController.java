package com.walking.api.web.controller.traffic;

import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.security.authentication.token.TokenUserDetailsService;
import com.walking.api.security.filter.token.AccessTokenResolver;
import com.walking.api.traffic.dto.AddFavoriteTrafficUseCaseIn;
import com.walking.api.traffic.dto.BrowseFavoriteTrafficsUseCaseIn;
import com.walking.api.traffic.dto.BrowseFavoriteTrafficsUseCaseOut;
import com.walking.api.traffic.dto.BrowseTrafficsUseCaseIn;
import com.walking.api.traffic.dto.BrowseTrafficsUseCaseOut;
import com.walking.api.traffic.dto.DeleteFavoriteTrafficUseCaseIn;
import com.walking.api.traffic.dto.SearchTrafficsUseCaseIn;
import com.walking.api.traffic.dto.SearchTrafficsUseCaseOut;
import com.walking.api.traffic.dto.UpdateFavoriteTrafficUseCaseIn;
import com.walking.api.traffic.usecase.AddFavoriteTrafficUseCase;
import com.walking.api.traffic.usecase.BrowseFavoriteTrafficsUseCase;
import com.walking.api.traffic.usecase.DeleteFavoriteTrafficUseCase;
import com.walking.api.traffic.usecase.ReadTrafficsUseCase;
import com.walking.api.traffic.usecase.SearchTrafficsUseCase;
import com.walking.api.traffic.usecase.UpdateFavoriteTrafficUseCase;
import com.walking.api.web.dto.request.point.ViewPointParam;
import com.walking.api.web.dto.request.traffic.FavoriteTrafficBody;
import com.walking.api.web.dto.request.traffic.PatchFavoriteTrafficNameBody;
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
	public ApiResponse<ApiResponse.SuccessBody<SearchTrafficsUseCaseOut>> searchTraffics(
			@Valid ViewPointParam viewPointParam) {
		SearchTrafficsUseCaseIn request =
				new SearchTrafficsUseCaseIn(
						viewPointParam.getVblLng(),
						viewPointParam.getVblLat(),
						viewPointParam.getVtrLng(),
						viewPointParam.getVtrLat());
		SearchTrafficsUseCaseOut response = searchTrafficsUseCase.execute(request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/{trafficId}")
	public ApiResponse<ApiResponse.SuccessBody<BrowseTrafficsUseCaseOut>> readTraffic(
			HttpServletRequest request, @PathVariable Long trafficId) {
		BrowseTrafficsUseCaseIn useCaseRequest = new BrowseTrafficsUseCaseIn(trafficId, -1L);
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		BrowseTrafficsUseCaseOut response = null;
		if (Objects.nonNull(authorization)) {
			String token = AccessTokenResolver.resolve(authorization);
			UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(token);
			Long memberId = Long.valueOf(userDetails.getUsername());
			useCaseRequest = new BrowseTrafficsUseCaseIn(trafficId, memberId);
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
				new AddFavoriteTrafficUseCaseIn(
						memberId, favoriteTrafficBody.getTrafficId(), favoriteTrafficBody.getTrafficAlias()));
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/favorite")
	public ApiResponse<ApiResponse.SuccessBody<BrowseFavoriteTrafficsUseCaseOut>>
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

		BrowseFavoriteTrafficsUseCaseIn useCaseRequest = new BrowseFavoriteTrafficsUseCaseIn(memberId);
		BrowseFavoriteTrafficsUseCaseOut response =
				browseFavoriteTrafficsUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> updateFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Min(1) @PathVariable Long trafficId,
			@Valid @RequestBody PatchFavoriteTrafficNameBody patchFavoriteTrafficNameBody) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		updateFavoriteTrafficUseCase.execute(
				new UpdateFavoriteTrafficUseCaseIn(
						memberId, trafficId, patchFavoriteTrafficNameBody.getTrafficAlias()));
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping("/favorite/{trafficId}")
	public ApiResponse<ApiResponse.Success> deleteFavoriteTraffic(
			@AuthenticationPrincipal TokenUserDetails userDetails, @Min(1) @PathVariable Long trafficId) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		deleteFavoriteTrafficUseCase.execute(new DeleteFavoriteTrafficUseCaseIn(memberId, trafficId));
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}
}
