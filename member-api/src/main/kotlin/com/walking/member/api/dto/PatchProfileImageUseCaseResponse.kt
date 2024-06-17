package com.walking.member.api.dto

data class PatchProfileImageUseCaseResponse(
    val id: Long,
    val nickName: String,
    val profile: String
)