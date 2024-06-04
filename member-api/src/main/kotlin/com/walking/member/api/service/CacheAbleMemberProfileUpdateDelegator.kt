package com.walking.member.api.service

import com.walking.data.entity.member.MemberEntity
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

@Service
class CacheAbleMemberProfileUpdateDelegator {
    @CachePut(key = "#entity.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile-url"])
    fun execute(entity: MemberEntity, imageName: String): MemberEntity {
        return entity.updateProfile(imageName)
    }
}