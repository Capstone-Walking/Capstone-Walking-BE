package com.walking.api.member.api.usecase

import com.walking.api.image.service.GetPreSignedImageUrlService
import com.walking.api.member.api.dao.MemberDao
import com.walking.api.member.api.dto.GetMemberDetailUseCaseIn
import com.walking.api.member.api.dto.GetMemberDetailUseCaseOut

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMemberDetailUseCase(
    private val memberRepository: MemberDao,
    private val getPreSignedImageUrlService: GetPreSignedImageUrlService
) {
    val log: Logger = LoggerFactory.getLogger(GetMemberDetailUseCase::class.java)

    @Transactional
    @Cacheable(key = "#useCaseIn.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile"])
    fun execute(useCaseIn: GetMemberDetailUseCaseIn): GetMemberDetailUseCaseOut {
        val member = memberRepository.findById(useCaseIn.id) ?: throw IllegalArgumentException("Member not found")
        val id = member.id
        val nickName = member.nickName
        val certificationSubject = member.certificationSubject.name
        val status = member.status.name
        var profile = getProfile(member.profile)

        return GetMemberDetailUseCaseOut(
            id,
            nickName,
            profile,
            certificationSubject,
            status
        )
    }

    private fun getProfile(profile: String): String {
        if (profile.startsWith("http")) {
            return profile
        }
        return getPreSignedImageUrlService.execute(profile)
    }
}