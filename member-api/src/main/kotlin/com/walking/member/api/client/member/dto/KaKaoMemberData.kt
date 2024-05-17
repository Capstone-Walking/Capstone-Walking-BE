package com.walking.member.api.client.member.dto

data class KaKaoMemberData(private val id: String) : SocialMemberData {
    private val properties: Properties? = null

    private data class Properties(val nickname: String)

    override fun getName(): String {
        return properties?.nickname ?: ""
    }

    override fun getId(): Long {
        return id.toLong()
    }
}