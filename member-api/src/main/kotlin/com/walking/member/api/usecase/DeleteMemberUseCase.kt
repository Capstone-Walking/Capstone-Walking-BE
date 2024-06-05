package com.walking.member.api.usecase

import com.walking.data.entity.member.MemberEntity
import com.walking.image.service.RemoveImageService
import com.walking.image.service.minio.MinioRemoveImageService
import com.walking.member.api.client.unlink.SocialUnlinkClientManager
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.usecase.dto.response.DeleteMemberUseCaseResponse
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
    @CacheEvict(key = "#id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile-url"])
    fun execute(id: Long): DeleteMemberUseCaseResponse {
        val member = memberRepository.findById(id) ?: throw IllegalArgumentException("Member not found")
        val deletedMember = withdrawMember(member)
        removeImageService.execute(deletedMember.profile)
        unlinkClientManager.execute(
            deletedMember.certificationSubject.name,
            deletedMember.certificationId
        )

        return DeleteMemberUseCaseResponse(deletedMember.id, deletedMember.updatedAt)
    }

    private fun withdrawMember(member: MemberEntity): MemberEntity {
        member.withDrawn()
        return memberRepository.save(member)
    }
}