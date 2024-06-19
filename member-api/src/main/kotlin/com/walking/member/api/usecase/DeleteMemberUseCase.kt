package com.walking.member.api.usecase

import com.walking.api.repository.config.ApiRepositoryJpaConfig
import com.walking.data.entity.member.MemberEntity
import com.walking.image.service.RemoveImageService
import com.walking.member.api.client.unlink.SocialUnlinkClientManager
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.dto.DeleteMemberUseCaseIn
import com.walking.member.api.dto.DeleteMemberUseCaseOut
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteMemberUseCase(
    private val memberRepository: MemberDao,
    private val removeImageService: RemoveImageService,
    private val unlinkClientManager: SocialUnlinkClientManager
) {
    @Transactional(value = ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME)
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

    private fun withdrawMember(member: MemberEntity): MemberEntity {
        member.withDrawn()
        return memberRepository.save(member)
    }
}