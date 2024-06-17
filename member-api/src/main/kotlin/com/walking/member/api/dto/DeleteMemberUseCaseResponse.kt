package com.walking.member.api.dto

import java.time.LocalDateTime

data class DeleteMemberUseCaseResponse(
    val id: Long,
    val deletedAt: LocalDateTime
)