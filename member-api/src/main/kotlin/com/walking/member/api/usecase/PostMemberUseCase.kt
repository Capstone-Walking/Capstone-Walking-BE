package com.walking.member.api.usecase

import com.walking.data.entity.member.MemberEntity
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.service.kakao.dto.SocialMemberServiceDto
import com.walking.member.api.usecase.dto.response.PostMemberUseCaseResponse
import com.walking.member.api.service.kakao.PostKaKaoMemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Random

@Service
class PostMemberUseCase(
    private val createKaKaoMemberService: PostKaKaoMemberService,
    private val memberRepository: MemberDao
) {

    companion object{
        val RED_PROFILE = "https://d25bhk5us0632l.cloudfront.net/default/profiles/red.png"
        val GREEN_PROFILE = "https://d25bhk5us0632l.cloudfront.net/default/profiles/green.png"
        val YELLOW_PROFILE = "https://d25bhk5us0632l.cloudfront.net/default/profiles/yellow.png"
        val DEFAULT_PROFILES = listOf(RED_PROFILE, GREEN_PROFILE, YELLOW_PROFILE)
    }
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
        Random().nextInt(DEFAULT_PROFILES.size).let { index ->
            socialMember.profile = DEFAULT_PROFILES[index]
        }

        return MemberEntity(
            socialMember.nickName,
            socialMember.profile,
            socialMember.certificationId
        )
    }
}