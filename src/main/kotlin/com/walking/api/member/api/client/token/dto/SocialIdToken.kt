package com.walking.api.member.api.client.token.dto

fun interface SocialIdToken {
    fun getToken(): String
}