package com.walking.member.api.dto

data class PostMemberUseCaseResponse(
    val id: Long,
    val nickname: String,
    val profile: String
)