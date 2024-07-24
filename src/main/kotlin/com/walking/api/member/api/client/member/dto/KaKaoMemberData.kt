package com.walking.api.member.api.client.member.dto

data class KaKaoMemberData(val id: String) : SocialMemberData {
    val properties: Properties? = null

    data class Properties(val nickname: String, val profile_image: String)

    override fun getName(): String {
        return properties?.nickname ?: ""
    }

    override fun getId(): Long {
        return id.toLong()
    }

    fun getPicture(): String {
        return properties?.profile_image ?: ""
    }
}