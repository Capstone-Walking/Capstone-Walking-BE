package com.walking.member.api.client.support

import com.walking.member.api.client.config.property.KaKaoApiProperties
import com.walking.member.api.client.config.property.KaKaoIdTokenProperties
import com.walking.member.api.client.exception.SocialIntegrationException
import com.walking.member.api.client.util.TokenPropertiesMapper
import org.springframework.stereotype.Component
import java.util.*

@Component
class KaKoIdTokenParser(
    private val kaKaoApiProperties: KaKaoApiProperties,
    private val tokenPropertiesMapper: TokenPropertiesMapper
) {
    val payloadIndex = 1
    fun parse(idToken: String): KaKaoIdTokenProperties {
        val payload = idToken.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[payloadIndex]
        val source = String(Base64.getDecoder().decode(payload))
        val idProperties: KaKaoIdTokenProperties =
            tokenPropertiesMapper.read(source, KaKaoIdTokenProperties::class.java)
        if (idProperties.iss != kaKaoApiProperties.host) {
            throw SocialIntegrationException()
        }
        if (idProperties.aud != kaKaoApiProperties.clientId) {
            throw SocialIntegrationException()
        }
        return idProperties
    }
}