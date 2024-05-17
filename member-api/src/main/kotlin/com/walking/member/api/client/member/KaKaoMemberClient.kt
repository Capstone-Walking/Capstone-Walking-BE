package com.walking.member.api.client.member

import com.walking.member.api.client.config.property.KaKaoApiProperties
import com.walking.member.api.client.exception.SocialClientException
import com.walking.member.api.client.member.dto.KaKaoMemberData
import com.walking.member.api.client.member.dto.SocialMemberData
import com.walking.member.api.client.token.dto.SocialIdToken
import com.walking.member.api.client.util.HeaderUtils
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Slf4j
@Component
@RequiredArgsConstructor
class KaKaoMemberClient(
    private val restTemplate: RestTemplate,
    private val properties: KaKaoApiProperties
) : SocialMemberClient {
    override fun execute(token: SocialIdToken): SocialMemberData {
        val accessToken: String = token.getToken()

        val headers: HttpHeaders = HeaderUtils.generateBearerHeaders(accessToken)
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
        val response: ResponseEntity<KaKaoMemberData> = restTemplate.exchange(
            properties.uriMeInfo,
            HttpMethod.POST,
            HttpEntity(null, headers),
            KaKaoMemberData::class.java
        )

        val statusCode = response.statusCode
        if (statusCode.is4xxClientError) {
            throw SocialClientException()
        }
        return response.body!!
    }
}