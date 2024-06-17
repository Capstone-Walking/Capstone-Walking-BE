package com.walking.member.api.service.delegator

import com.walking.data.entity.member.MemberEntity
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

@Service
class CacheAbleMemberProfileUpdateDelegator {
    @CachePut(key = "#entity.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile"])
    fun execute(entity: MemberEntity, imageName: String): MemberEntity {
        return entity.updateProfile(imageName)
    }
}