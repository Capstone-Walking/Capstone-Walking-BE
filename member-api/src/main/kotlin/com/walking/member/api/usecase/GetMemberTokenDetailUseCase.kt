package com.walking.member.api.usecase

import com.walking.member.api.dao.MemberDao
import com.walking.member.api.usecase.dto.response.GetMemberTokenDetailUseCaseResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberTokenDetailUseCase(private val memberRepository: MemberDao) {
    @Transactional
    fun execute(id: Long): GetMemberTokenDetailUseCaseResponse {
        val member = memberRepository.findById(id) ?: throw IllegalArgumentException("Member not found")
        return GetMemberTokenDetailUseCaseResponse(member.id)
    }
}