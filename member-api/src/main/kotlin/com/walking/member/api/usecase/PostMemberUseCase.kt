package com.walking.member.api.usecase

import com.walking.data.entity.member.MemberEntity
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.service.kakao.dto.SocialMemberServiceDto
import com.walking.member.api.dto.PostMemberUseCaseResponse
import com.walking.member.api.service.kakao.PostKaKaoMemberService
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
    fun execute(code: String): PostMemberUseCaseResponse {
        val socialMember = createKaKaoMemberService.execute(code)

        memberRepository.findByCertificationId(socialMember.certificationId)
            ?.let { member ->
                return PostMemberUseCaseResponse(member.id, member.nickName, member.profile)
            }

        val newMember = createMemberEntity(socialMember)
        memberRepository.save(newMember).let { member ->
            return PostMemberUseCaseResponse(member.id, member.nickName, member.profile)
        }
    }

    private fun createMemberEntity(socialMember: SocialMemberServiceDto): MemberEntity {
        Random().nextInt(defaultProfiles.size).let { index ->
            socialMember.profile = defaultProfiles[index]
        }

        return MemberEntity(
            socialMember.nickName,
            socialMember.profile,
            socialMember.certificationId
        )
    }
}