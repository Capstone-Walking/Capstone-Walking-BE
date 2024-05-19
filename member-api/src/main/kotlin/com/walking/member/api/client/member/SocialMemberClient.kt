package com.walking.member.api.client.member

import com.walking.member.api.client.member.dto.SocialMemberData

fun interface SocialMemberClient {
    fun execute(userId: String): SocialMemberData
}