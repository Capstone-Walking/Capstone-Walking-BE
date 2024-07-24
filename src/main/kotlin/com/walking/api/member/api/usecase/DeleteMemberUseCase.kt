package com.walking.api.member.api.usecase

import com.walking.api.image.service.RemoveImageService
import com.walking.api.member.api.client.unlink.SocialUnlinkClientManager
import com.walking.api.member.api.dao.MemberDao
import com.walking.api.member.api.dto.DeleteMemberUseCaseIn
import com.walking.api.member.api.dto.DeleteMemberUseCaseOut
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteMemberUseCase(
    private val memberRepository: MemberDao,
    private val removeImageService: RemoveImageService,
    private val unlinkClientManager: SocialUnlinkClientManager
) {
    @Transactional
    @CacheEvict(key = "#useCaseIn.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile"])
    fun execute(useCaseIn: DeleteMemberUseCaseIn): DeleteMemberUseCaseOut {
        val member = memberRepository.findById(useCaseIn.id) ?: throw IllegalArgumentException("Member not found")
        val deletedMember = withdrawMember(member)
        removeImageService.execute(deletedMember.profile)
        unlinkClientManager.execute(
            deletedMember.certificationSubject.name,
            deletedMember.certificationId
        )

        return DeleteMemberUseCaseOut(deletedMember.id, deletedMember.updatedAt)
    }

    private fun withdrawMember(member: com.walking.api.data.entity.member.MemberEntity): com.walking.api.data.entity.member.MemberEntity {
        member.withDrawn()
        return memberRepository.save(member)
    }
}