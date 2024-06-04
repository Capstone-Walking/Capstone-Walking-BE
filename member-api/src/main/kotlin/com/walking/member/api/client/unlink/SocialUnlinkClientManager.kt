package com.walking.member.api.client.unlink

import com.walking.member.api.client.unlink.dto.SocialUnlinkData
import org.springframework.stereotype.Component

@Component
class SocialUnlinkClientManager(
    private val socialUnlinkClients: List<SocialUnlinkClient>
) {
    fun execute(socialType: String, targetId: String): SocialUnlinkData {
        socialUnlinkClients.forEach { client ->
            if (client.supports(socialType)) {
                return client.execute(targetId)
            }
        }
        throw IllegalArgumentException("Unsupported social type: $socialType")
    }
}