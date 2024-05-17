package com.walking.member.api.client.token.dto

data class KaKaoIdTokenData(private val id_token: String) : SocialIdToken {
    override fun getToken(): String {
        return id_token
    }
}