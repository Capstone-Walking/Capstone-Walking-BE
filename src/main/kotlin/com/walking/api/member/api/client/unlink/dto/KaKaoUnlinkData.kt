package com.walking.api.member.api.client.unlink.dto

data class KaKaoUnlinkData(val id: String) : SocialUnlinkData {
    override fun getUnlinkInfo(): String {
        return id
    }
}