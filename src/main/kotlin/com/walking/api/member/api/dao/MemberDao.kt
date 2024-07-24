package com.walking.api.member.api.dao

import com.walking.api.repository.dao.member.MemberRepository
import com.walking.api.data.entity.member.MemberEntity
import org.springframework.stereotype.Repository

@Repository
class MemberDao(private val memberRepository: com.walking.api.repository.dao.member.MemberRepository) {

    fun save(memberEntity: com.walking.api.data.entity.member.MemberEntity): com.walking.api.data.entity.member.MemberEntity {
        return memberRepository.save(memberEntity)
    }

    fun findByCertificationId(certificationId: String): com.walking.api.data.entity.member.MemberEntity? {
        return memberRepository.findByCertificationIdAndDeletedFalse(certificationId).let {
            if (it.isPresent) {
                it.get()
            } else {
                null
            }
        }
    }

    fun findById(id: Long): com.walking.api.data.entity.member.MemberEntity? {
        return memberRepository.findByIdAndDeletedFalse(id).let {
            if (it.isPresent) {
                it.get()
            } else {
                null
            }
        }
    }

    fun exist(id: Long): Boolean {
        return memberRepository.existsByIdAndDeletedFalse(id)
    }
}