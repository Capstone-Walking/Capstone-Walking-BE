package com.walking.member.api.service.kakao

import com.walking.member.api.client.support.KaKoIdTokenParser
import com.walking.member.api.client.token.KaKaoIdTokenClient
import com.walking.member.api.service.kakao.dto.SocialMemberServiceDto
import org.springframework.stereotype.Service

@Service
class PostKaKaoMemberService(private val kaKaoIdTokenClient: KaKaoIdTokenClient, private val kaKoIdTokenParser: KaKoIdTokenParser) {

    companion object {
        private const val SUBJECT = "KAKAO"
    }
    fun execute(code: String): SocialMemberServiceDto {
        val token = kaKaoIdTokenClient.execute(code)

        val idTokenProperties = kaKoIdTokenParser.parse(token.getToken())
        val nickname = idTokenProperties.nickname
        val certificationId = idTokenProperties.sub
        val profile = idTokenProperties.picture

        return SocialMemberServiceDto(
            nickname,
            profile,
            certificationId,
            SUBJECT
        )
    }
}