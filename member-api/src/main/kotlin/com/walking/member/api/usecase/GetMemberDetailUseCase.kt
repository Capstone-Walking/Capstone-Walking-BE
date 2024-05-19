package com.walking.member.api.usecase

import com.walking.data.entity.member.MemberEntity
import com.walking.image.service.GetPreSignedImageUrlService
import com.walking.member.api.dao.MemberDao

import com.walking.member.api.usecase.dto.response.GetMemberDetailUseCaseResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberDetailUseCase(
    private val memberRepository: MemberDao,
    private val getPreSignedImageUrlService: GetPreSignedImageUrlService
) {
    val log: Logger = LoggerFactory.getLogger(GetMemberDetailUseCase::class.java)

    @Transactional
    fun execute(id: Long): GetMemberDetailUseCaseResponse {
        val member = memberRepository.findById(id) ?: throw IllegalArgumentException("Member not found")
        val id = member.id
        val nickName = member.nickName
        val certificationSubject = member.certificationSubject.name
        val status = member.status.name
        val profile = getProfile(member)

        return GetMemberDetailUseCaseResponse(
            id,
            nickName,
            profile,
            certificationSubject,
            status
        )
    }

    private fun getProfile(member: MemberEntity): String {
        return try {
            getPreSignedImageUrlService.execute(member.profile)
        } catch (e: Exception) {
            log.debug("Failed to get profile image: ${e.message}")
            "" // todo fix 기본 이미지
        }
    }
}