package com.walking.member.api.usecase.dto.response

data class PostMemberUseCaseResponse(
    val id: Long,
    val nickname: String,
    val profile: String
)