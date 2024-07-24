package com.walking.api.member.api.dto

data class PatchProfileImageUseCaseOut(
    val id: Long,
    val nickName: String,
    val profile: String
)