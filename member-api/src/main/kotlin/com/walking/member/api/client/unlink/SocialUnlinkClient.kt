package com.walking.member.api.client.unlink

import com.walking.member.api.client.unlink.dto.SocialUnlinkData

fun interface SocialUnlinkClient {

    fun execute(targetId: String): SocialUnlinkData
}