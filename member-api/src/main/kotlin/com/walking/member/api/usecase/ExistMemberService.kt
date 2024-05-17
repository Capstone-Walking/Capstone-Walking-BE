package com.walking.member.api.usecase

import com.walking.member.api.dao.MemberDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExistMemberService(
    private val memberDao: MemberDao
) {
    @Transactional(readOnly = true)
    fun execute(id: Long): Boolean {
        return memberDao.exist(id)
    }
}