package com.walking.member.api.usecase

import com.walking.api.repository.config.ApiRepositoryJpaConfig
import com.walking.data.entity.member.MemberEntity
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.dto.PostMemberUseCaseIn
import com.walking.member.api.service.kakao.dto.SocialMemberVO
import com.walking.member.api.dto.PostMemberUseCaseOut
import com.walking.member.api.service.kakao.PostKaKaoMemberService
import com.walking.member.api.service.kakao.dto.KMSQuery
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Random

@Service
class PostMemberUseCase(
    @Value("\${walking.default.profiles}") private val defaultProfiles: List<String>,
    private val createKaKaoMemberService: PostKaKaoMemberService,
    private val memberRepository: MemberDao
) {
    @Transactional(value = ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME)
    @CacheEvict(key = "#useCaseIn.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile"])
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

    private fun createMemberEntity(socialMember: SocialMemberVO): MemberEntity {
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