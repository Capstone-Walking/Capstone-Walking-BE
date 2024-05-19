package com.walking.member.api.client.unlink

import com.walking.member.api.client.unlink.dto.SocialUnlinkData

fun interface SocialUnlinkClient {

    /**
     * Unlink social account by targetId
     * @param targetId social user id
     */
    fun execute(targetId: String): SocialUnlinkData
}