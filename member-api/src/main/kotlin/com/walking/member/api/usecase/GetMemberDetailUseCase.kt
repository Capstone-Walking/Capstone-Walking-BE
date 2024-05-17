package com.walking.member.api.usecase

import com.walking.image.service.GetPreSignedImageUrlService
import com.walking.member.api.dao.MemberDao

import com.walking.member.api.usecase.dto.response.GetMemberDetailUseCaseResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberDetailUseCase(
    private val memberRepository: MemberDao,
    private val getPreSignedImageUrlService: GetPreSignedImageUrlService
) {
    @Transactional
    fun execute(id: Long): GetMemberDetailUseCaseResponse {
        val member = memberRepository.findById(id) ?: throw IllegalArgumentException("Member not found")
        val id = member.id
        val nickName = member.nickName
        val certificationSubject = member.certificationSubject.name
        val status = member.status.name
        val profile = getPreSignedImageUrlService.execute(member.profile)

        return GetMemberDetailUseCaseResponse(
            id,
            nickName,
            profile,
            certificationSubject,
            status
        )
    }
}