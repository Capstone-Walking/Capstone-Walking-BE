package com.walking.api.member.api.client.config.property

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KaKaoApiProperties(
    @Value("\${kakao.host}") val host: String,
    @Value("\${kakao.adminKey}") val adminKey: String,
    @Value("\${kakao.uri.token}") val uriToken: String,
    @Value("\${kakao.uri.token_info}") val uriTokenInfo: String,
    @Value("\${kakao.uri.me_info}") val uriMeInfo: String,
    @Value("\${kakao.uri.unlink}") val unlink: String,
    @Value("\${kakao.redirect_uri}") val redirectUri: String,
    @Value("\${kakao.client_id}") val clientId: String
)