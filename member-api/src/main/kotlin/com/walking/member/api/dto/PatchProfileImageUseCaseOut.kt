package com.walking.member.api.dto

data class PatchProfileImageUseCaseOut(
    val id: Long,
    val nickName: String,
    val profile: String
)