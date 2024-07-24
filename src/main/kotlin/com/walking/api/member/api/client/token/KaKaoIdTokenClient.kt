package com.walking.api.member.api.client.token

import SocialIdTokenClient
import com.walking.api.member.api.client.config.property.KaKaoApiProperties
import com.walking.api.member.api.client.exception.SocialClientException
import com.walking.api.member.api.client.token.dto.KaKaoIdTokenData
import com.walking.api.member.api.client.token.dto.SocialIdToken
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Slf4j
@Component
@RequiredArgsConstructor
class KaKaoIdTokenClient(
    private val restTemplate: RestTemplate,
    private val properties: KaKaoApiProperties
) : SocialIdTokenClient {
    override fun execute(code: String): SocialIdToken {
        val headers = HttpHeaders()
        headers.add("Accept", "application/json")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", properties.clientId)
        params.add("redirect_uri", properties.redirectUri)
        params.add("code", code)

        val httpEntity = HttpEntity(params, headers)

        val response: ResponseEntity<KaKaoIdTokenData>
        try {
            response = restTemplate.exchange(
                properties.uriToken,
                HttpMethod.POST,
                httpEntity,
                KaKaoIdTokenData::class.java
            )
        } catch (e: RestClientException) {
            throw SocialClientException()
        }

        val statusCode = response.statusCode
        if (statusCode.is4xxClientError) {
            throw SocialClientException()
        }

        return response.body as SocialIdToken
    }
}