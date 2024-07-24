package com.walking.api.member.api.service

import com.walking.api.member.api.dao.MemberDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExistMemberService(
    private val memberDao: MemberDao
) {
    @Transactional
    fun execute(id: Long): Boolean {
        return memberDao.exist(id)
    }
}