package com.walking.api.member.api.dto

data class GetMemberDetailUseCaseOut(
    val id: Long,
    val nickName: String,
    val profile: String,
    val certificationSubject: String,
    val status: String
)