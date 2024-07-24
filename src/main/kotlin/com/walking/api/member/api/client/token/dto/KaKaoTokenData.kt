package com.walking.api.member.api.client.token.dto

data class KaKaoTokenData(val id: String) : SocialToken {
    override fun getInfo(): String {
        return id
    }
}