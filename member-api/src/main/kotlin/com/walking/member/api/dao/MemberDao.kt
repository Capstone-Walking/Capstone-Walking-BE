package com.walking.member.api.dao

import com.walking.api.repository.dao.member.MemberRepository
import com.walking.data.entity.member.MemberEntity
import org.springframework.stereotype.Repository

@Repository
class MemberDao(private val memberRepository: MemberRepository) {

    fun save(memberEntity: MemberEntity): MemberEntity {
        return memberRepository.save(memberEntity)
    }

    fun findByCertificationId(certificationId: String): MemberEntity? {
        return memberRepository.findByCertificationIdAndDeletedFalse(certificationId).let {
            if (it.isPresent) {
                it.get()
            } else {
                null
            }
        }
    }

    fun findById(id: Long): MemberEntity? {
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