package com.walking.member.api.dto

data class PostMemberUseCaseOut(
    val id: Long,
    val nickname: String,
    val profile: String
)