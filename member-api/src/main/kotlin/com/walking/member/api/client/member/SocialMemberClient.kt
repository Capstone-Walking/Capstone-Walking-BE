package com.walking.member.api.client.member

import com.walking.member.api.client.member.dto.SocialMemberData
import com.walking.member.api.client.token.dto.SocialIdToken

fun interface SocialMemberClient {
    fun execute(token: SocialIdToken): SocialMemberData
}