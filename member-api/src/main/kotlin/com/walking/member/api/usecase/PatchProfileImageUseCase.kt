package com.walking.member.api.usecase

import com.walking.image.service.UploadImageService
import com.walking.member.api.dao.MemberDao
import com.walking.member.api.usecase.dto.response.PatchProfileImageUseCaseResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDate
import kotlin.random.Random

@Service
class PatchProfileImageUseCase(
    private val memberDao: MemberDao,
    private val uploadImageService: UploadImageService
) {
    @Transactional
    fun execute(id: Long, image: File): PatchProfileImageUseCaseResponse {
        val member = memberDao.findById(id) ?: throw IllegalArgumentException("Member not found")
        val imageName = generateImageName()

        uploadImageService.execute(imageName, image).let {
            member.updateProfile(imageName).let { member ->
                memberDao.save(member)
                return PatchProfileImageUseCaseResponse(member.id, member.nickName, imageName)
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