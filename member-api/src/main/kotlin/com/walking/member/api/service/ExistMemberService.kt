package com.walking.member.api.service

import com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME
import com.walking.member.api.dao.MemberDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExistMemberService(
    private val memberDao: MemberDao
) {
    @Transactional(value = TRANSACTION_MANAGER_NAME)
    fun execute(id: Long): Boolean {
        return memberDao.exist(id)
    }
}