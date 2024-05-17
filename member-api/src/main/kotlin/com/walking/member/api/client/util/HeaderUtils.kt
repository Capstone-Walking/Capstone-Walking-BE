package com.walking.member.api.client.util

import org.springframework.http.HttpHeaders

class HeaderUtils {
    companion object {
        fun generateBearerHeaders(token: String): HttpHeaders {
            val headers = HttpHeaders()
            headers.add("Authorization", "Bearer $token")
            return headers
        }
    }
}