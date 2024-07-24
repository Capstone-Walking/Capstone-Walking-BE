package com.walking.api.member.api.usecase

import com.walking.api.image.service.UploadImageService
import com.walking.api.member.api.dao.MemberDao
import com.walking.api.member.api.dto.PatchProfileImageUseCaseIn
import com.walking.api.member.api.dto.PatchProfileImageUseCaseOut
import com.walking.api.member.api.service.delegator.CacheAbleMemberProfileUpdateDelegator
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.random.Random

@Service
class PatchProfileImageUseCase(
    private val memberDao: MemberDao,
    private val uploadImageService: UploadImageService,
    private val memberProfileUpdateDelegator: CacheAbleMemberProfileUpdateDelegator
) {
    @Transactional
    @CacheEvict(key = "#useCaseIn.id", cacheManager = "memberApiCacheManager", cacheNames = ["member-profile"])
    fun execute(useCaseIn: PatchProfileImageUseCaseIn): PatchProfileImageUseCaseOut {
        val member = memberDao.findById(useCaseIn.id) ?: throw IllegalArgumentException("Member not found")
        val dateDir = LocalDate.now().toString()
        val imageName = "$dateDir/${generateImageName()}"

        uploadImageService.execute(imageName, useCaseIn.image).let {
            memberProfileUpdateDelegator.execute(member, imageName).let { member ->
                memberDao.save(member)
                return PatchProfileImageUseCaseOut(member.id, member.nickName, imageName)
            }
        }
    }

    private fun generateImageName(): String {
        val now = LocalDate.now()
        return "${now.year}${now.monthValue}${now.dayOfMonth}_${randomString()}"
    }

    private fun randomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..16)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }
}