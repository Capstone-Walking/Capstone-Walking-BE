package com.walking.api.member.api.client.token.dto

data class KaKaoIdTokenData(val id_token: String) : SocialIdToken {
    override fun getToken(): String {
        return id_token
    }
}