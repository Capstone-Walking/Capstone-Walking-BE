package com.walking.api.member.api.usecase

import com.walking.api.member.api.dao.MemberDao
import com.walking.api.member.api.dto.GetMemberTokenDetailUseCaseIn
import com.walking.api.member.api.dto.GetMemberTokenDetailUseCaseOut
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberTokenDetailUseCase(private val memberRepository: MemberDao) {
    @Transactional
    fun execute(useCaseIn: GetMemberTokenDetailUseCaseIn): GetMemberTokenDetailUseCaseOut {
        val member = memberRepository.findById(
            useCaseIn.id
        ) ?: throw IllegalArgumentException("Member not found")
        return GetMemberTokenDetailUseCaseOut(member.id)
    }
}