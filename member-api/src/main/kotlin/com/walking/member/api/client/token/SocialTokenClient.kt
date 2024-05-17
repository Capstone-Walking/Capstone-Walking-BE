package com.walking.member.api.client.token

import com.walking.member.api.client.token.dto.SocialIdToken
import com.walking.member.api.client.token.dto.SocialToken

fun interface SocialTokenClient {

    fun execute(token: SocialIdToken): SocialToken
}