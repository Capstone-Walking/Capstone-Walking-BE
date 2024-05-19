package com.walking.member.api.client.unlink

import com.walking.member.api.client.config.property.KaKaoApiProperties
import com.walking.member.api.client.exception.SocialClientException
import com.walking.member.api.client.unlink.dto.KaKaoUnlinkData
import com.walking.member.api.client.unlink.dto.SocialUnlinkData
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
class KaKaoUnlinkClient(
    private val restTemplate: RestTemplate,
    private val properties: KaKaoApiProperties
) : SocialUnlinkClient {
    override fun execute(targetId: String): SocialUnlinkData {
        val adminKey: String = properties.adminKey

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
        headers.add(HttpHeaders.AUTHORIZATION, "KakaoAK $adminKey")

        val params: MultiValueMap<String, Any> = LinkedMultiValueMap()
        params.add("target_id_type", "user_id")
        params.add("target_id", targetId.toLong())

        val response: ResponseEntity<KaKaoUnlinkData>
        try {
            response = restTemplate.exchange(
                properties.unlink,
                HttpMethod.POST,
                HttpEntity(params, headers),
                KaKaoUnlinkData::class.java
            )
        } catch (e: RestClientException) {
            throw SocialClientException()
        }

        val statusCode = response.statusCode
        if (statusCode.is4xxClientError) {
            throw SocialClientException()
        }

        return response.body!!
    }

    override fun supports(type: String): Boolean {
        return "KAKAO" == type.toUpperCase()
    }
}