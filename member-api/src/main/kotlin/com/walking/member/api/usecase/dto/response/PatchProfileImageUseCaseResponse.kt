package com.walking.member.api.usecase.dto.response

data class PatchProfileImageUseCaseResponse(
    val id: Long,
    val nickName: String,
    val profile: String
)