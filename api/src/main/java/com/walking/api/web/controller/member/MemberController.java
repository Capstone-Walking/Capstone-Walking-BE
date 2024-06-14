package com.walking.api.web.controller.member;

import com.walking.api.security.authentication.authority.Roles;
import com.walking.api.security.authentication.token.TokenUserDetails;
import com.walking.api.security.token.AuthToken;
import com.walking.api.security.token.TokenGenerator;
import com.walking.api.security.token.TokenResolver;
import com.walking.api.web.dto.request.member.PatchProfileBody;
import com.walking.api.web.dto.request.member.PostMemberBody;
import com.walking.api.web.dto.request.member.RefreshMemberAuthTokenBody;
import com.walking.api.web.dto.response.member.DeleteMemberResponse;
import com.walking.api.web.dto.response.member.GetMemberResponse;
import com.walking.api.web.dto.response.member.MemberTokenResponse;
import com.walking.api.web.dto.response.member.PatchProfileResponse;
import com.walking.api.web.dto.response.member.PostMemberResponse;
import com.walking.api.web.support.ApiResponse;
import com.walking.api.web.support.ApiResponseGenerator;
import com.walking.api.web.support.MessageCode;
import com.walking.member.api.usecase.DeleteMemberUseCase;
import com.walking.member.api.usecase.GetMemberDetailUseCase;
import com.walking.member.api.usecase.GetMemberTokenDetailUseCase;
import com.walking.member.api.usecase.PatchProfileImageUseCase;
import com.walking.member.api.usecase.PostMemberUseCase;
import com.walking.member.api.usecase.dto.response.DeleteMemberUseCaseResponse;
import com.walking.member.api.usecase.dto.response.GetMemberDetailUseCaseResponse;
import com.walking.member.api.usecase.dto.response.GetMemberTokenDetailUseCaseResponse;
import com.walking.member.api.usecase.dto.response.PatchProfileImageUseCaseResponse;
import com.walking.member.api.usecase.dto.response.PostMemberUseCaseResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final TokenGenerator tokenGenerator;
	private final TokenResolver tokenResolver;

	private final PostMemberUseCase postMemberUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;
	private final GetMemberDetailUseCase getMemberDetailUseCase;
	private final GetMemberTokenDetailUseCase getMemberTokenDetailUseCase;
	private final PatchProfileImageUseCase patchProfileImageUseCase;

	@PostMapping()
	public ApiResponse<ApiResponse.SuccessBody<PostMemberResponse>> postMember(
			@Valid @RequestBody PostMemberBody postMemberBody) {
		PostMemberUseCaseResponse useCaseResponse = postMemberUseCase.execute(postMemberBody.getCode());
		AuthToken authToken =
				tokenGenerator.generateAuthToken(useCaseResponse.getId(), List.of(Roles.ROLE_USER));
		PostMemberResponse response =
				PostMemberResponse.builder()
						.id(useCaseResponse.getId())
						.nickname(useCaseResponse.getNickname())
						.profile(useCaseResponse.getProfile())
						.accessToken(authToken.getAccessToken())
						.refreshToken(authToken.getRefreshToken())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@DeleteMapping()
	public ApiResponse<ApiResponse.SuccessBody<DeleteMemberResponse>> deleteMember(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		DeleteMemberUseCaseResponse useCaseResponse = deleteMemberUseCase.execute(memberId);
		DeleteMemberResponse response =
				DeleteMemberResponse.builder()
						.id(useCaseResponse.getId())
						.deletedAt(useCaseResponse.getDeletedAt())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<GetMemberResponse>> getMember(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		Long memberId = Long.valueOf(userDetails.getUsername());
		GetMemberDetailUseCaseResponse useCaseResponse = getMemberDetailUseCase.execute(memberId);
		GetMemberResponse response =
				GetMemberResponse.builder()
						.id(useCaseResponse.getId())
						.nickname(useCaseResponse.getNickName())
						.profile(useCaseResponse.getProfile())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/token")
	public ApiResponse<ApiResponse.SuccessBody<MemberTokenResponse>> refreshMemberAuthToken(
			@Valid @RequestBody RefreshMemberAuthTokenBody memberAuthTokenBody) {
		Long memberId =
				tokenResolver
						.resolveId(memberAuthTokenBody.getRefreshToken())
						.orElseThrow(() -> new IllegalArgumentException("Invalid token"));
		GetMemberTokenDetailUseCaseResponse useCaseResponse =
				getMemberTokenDetailUseCase.execute(memberId);
		AuthToken authToken =
				tokenGenerator.generateAuthToken(useCaseResponse.getId(), List.of(Roles.ROLE_USER));
		MemberTokenResponse response =
				MemberTokenResponse.builder()
						.accessToken(authToken.getAccessToken())
						.refreshToken(authToken.getRefreshToken())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PatchMapping("/profile")
	public ApiResponse<ApiResponse.SuccessBody<PatchProfileResponse>> patchProfileImage(
			@AuthenticationPrincipal TokenUserDetails userDetails, PatchProfileBody patchProfileBody)
			throws IOException {
		Long memberId = Long.valueOf(userDetails.getUsername());
		String suffix = patchProfileBody.getProfile().getOriginalFilename().split("\\.")[1];
		File tempFile = File.createTempFile("temp_", "." + suffix);
		patchProfileBody.getProfile().transferTo(tempFile);
		PatchProfileImageUseCaseResponse useCaseResponse =
				patchProfileImageUseCase.execute(memberId, tempFile);
		tempFile.deleteOnExit();
		PatchProfileResponse response =
				PatchProfileResponse.builder()
						.id(useCaseResponse.getId())
						.nickname(useCaseResponse.getNickName())
						.profile(useCaseResponse.getProfile())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}
}
