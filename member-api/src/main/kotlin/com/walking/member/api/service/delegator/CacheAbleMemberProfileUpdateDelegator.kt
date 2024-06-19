package com.walking.member.api.service.delegator

import com.walking.data.entity.member.MemberEntity
import org.springframework.stereotype.Service

@Service
class CacheAbleMemberProfileUpdateDelegator {
    fun execute(entity: MemberEntity, imageName: String): MemberEntity {
        return entity.updateProfile(imageName)
    }
}