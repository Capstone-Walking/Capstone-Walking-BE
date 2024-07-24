package com.walking.api.member.api.client.member

import com.walking.api.member.api.client.member.dto.SocialMemberData

fun interface SocialMemberClient {
    /**
     * Get social member data by userId
     * @param targetId social user id
     */
    fun execute(targetId: String): SocialMemberData
}