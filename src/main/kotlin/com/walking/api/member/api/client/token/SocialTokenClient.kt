package com.walking.api.member.api.client.token

import com.walking.api.member.api.client.token.dto.SocialIdToken
import com.walking.api.member.api.client.token.dto.SocialToken

fun interface SocialTokenClient {

    /**
     * Get social token by social id token
     * @param token social id token
     */
    fun execute(token: SocialIdToken): SocialToken
}