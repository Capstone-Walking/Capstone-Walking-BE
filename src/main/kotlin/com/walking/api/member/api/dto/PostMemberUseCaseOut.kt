package com.walking.api.member.api.dto

data class PostMemberUseCaseOut(
    val id: Long,
    val nickname: String,
    val profile: String
)