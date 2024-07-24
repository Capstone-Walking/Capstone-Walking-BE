package com.walking.api.member.api.service.delegator

import com.walking.api.data.entity.member.MemberEntity
import org.springframework.stereotype.Service

@Service
class CacheAbleMemberProfileUpdateDelegator {
    fun execute(entity: com.walking.api.data.entity.member.MemberEntity, imageName: String): com.walking.api.data.entity.member.MemberEntity {
        return entity.updateProfile(imageName)
    }
}