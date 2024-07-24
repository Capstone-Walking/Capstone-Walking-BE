package com.walking.api.member.api.service.kakao

import com.walking.api.member.api.client.support.KaKoIdTokenParser
import com.walking.api.member.api.client.token.KaKaoIdTokenClient
import com.walking.api.member.api.service.kakao.dto.KMSQuery
import com.walking.api.member.api.service.kakao.dto.SocialMemberVO
import org.springframework.stereotype.Service

@Service
class PostKaKaoMemberService(private val kaKaoIdTokenClient: KaKaoIdTokenClient, private val kaKoIdTokenParser: KaKoIdTokenParser) {

    companion object {
        private const val SUBJECT = "KAKAO"
    }
    fun execute(query: KMSQuery): SocialMemberVO {
        val token = kaKaoIdTokenClient.execute(query.code)

        val idTokenProperties = kaKoIdTokenParser.parse(token.getToken())
        val nickname = idTokenProperties.nickname
        val certificationId = idTokenProperties.sub
        val profile = idTokenProperties.picture

        return SocialMemberVO(
            nickname,
            profile,
            certificationId,
            SUBJECT
        )
    }
}