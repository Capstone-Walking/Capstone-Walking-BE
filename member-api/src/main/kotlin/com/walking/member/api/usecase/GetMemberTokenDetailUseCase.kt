package com.walking.member.api.usecase

import com.walking.api.repository.config.ApiRepositoryJpaConfig
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.dto.GetMemberTokenDetailUseCaseIn
import com.walking.member.api.dto.GetMemberTokenDetailUseCaseOut
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberTokenDetailUseCase(private val memberRepository: MemberDao) {
    @Transactional(value = ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME)
    fun execute(useCaseIn: GetMemberTokenDetailUseCaseIn): GetMemberTokenDetailUseCaseOut {
        val member = memberRepository.findById(
            useCaseIn.id
        ) ?: throw IllegalArgumentException("Member not found")
        return GetMemberTokenDetailUseCaseOut(member.id)
    }
}