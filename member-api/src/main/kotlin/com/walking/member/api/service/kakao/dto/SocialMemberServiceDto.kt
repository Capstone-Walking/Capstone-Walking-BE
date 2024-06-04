package com.walking.member.api.service.kakao.dto

data class SocialMemberServiceDto(
    val nickName: String,
    val profile: String,
    val certificationId: String,
    val certificationSubject: String
)