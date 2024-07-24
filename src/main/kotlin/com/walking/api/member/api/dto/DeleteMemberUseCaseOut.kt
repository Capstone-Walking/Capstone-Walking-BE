package com.walking.api.member.api.dto

import java.time.LocalDateTime

data class DeleteMemberUseCaseOut(
    val id: Long,
    val deletedAt: LocalDateTime
)