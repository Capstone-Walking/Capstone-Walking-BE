package com.walking.member.api.client.util

import org.springframework.http.HttpHeaders

fun HttpHeaders.addBearerHeader(token: String) {
    this.add("Authorization", "Bearer $token")
}

fun HttpHeaders.addKakaoHeader(adminKey: String) {
    this.add("Authorization", "KakaoAK $adminKey")
}

class HeadersFunction