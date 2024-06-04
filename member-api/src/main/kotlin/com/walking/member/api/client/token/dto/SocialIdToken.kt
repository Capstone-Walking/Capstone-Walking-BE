package com.walking.member.api.client.token.dto

fun interface SocialIdToken {
    fun getToken(): String
}