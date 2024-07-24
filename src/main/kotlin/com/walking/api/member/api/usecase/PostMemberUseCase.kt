package com.walking.api.member.api.usecase

import com.walking.api.data.entity.member.MemberEntity
import com.walking.api.member.api.dao.MemberDao
import com.walking.api.member.api.dto.PostMemberUseCaseIn
import com.walking.api.member.api.dto.PostMemberUseCaseOut
import com.walking.api.member.api.service.kakao.PostKaKaoMemberService
import com.walking.api.member.api.service.kakao.dto.KMSQuery
import com.walking.api.member.api.service.kakao.dto.SocialMemberVO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Random

@Service
class PostMemberUseCase(
    @Value("\${walking.default.profiles}") private val defaultProfiles: List<String>,
    private val createKaKaoMemberService: PostKaKaoMemberService,
    private val memberRepository: MemberDao
) {
    @Transactional
    fun execute(useCaseIn: PostMemberUseCaseIn): PostMemberUseCaseOut {
        val socialMember = createKaKaoMemberService.execute(KMSQuery(useCaseIn.code))

        memberRepository.findByCertificationId(socialMember.certificationId)
            ?.let { member ->
                return PostMemberUseCaseOut(member.id, member.nickName, member.profile)
            }

        val newMember = createMemberEntity(socialMember)
        memberRepository.save(newMember).let { member ->
            return PostMemberUseCaseOut(member.id, member.nickName, member.profile)
        }
    }

    private fun createMemberEntity(socialMember: SocialMemberVO): com.walking.api.data.entity.member.MemberEntity {
        Random().nextInt(defaultProfiles.size).let { index ->
            socialMember.profile = defaultProfiles[index]
        }

        return com.walking.api.data.entity.member.MemberEntity(
            socialMember.nickName,
            socialMember.profile,
            socialMember.certificationId
        )
    }
}