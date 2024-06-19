package com.walking.member.api.service.kakao.dto

data class SocialMemberVO(
    val nickName: String,
    var profile: String,
    val certificationId: String,
    val certificationSubject: String
)