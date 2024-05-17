package com.walking.member.api.usecase.dto.response

import java.time.LocalDateTime

data class DeleteMemberUseCaseResponse(
    val id: Long,
    val deletedAt: LocalDateTime
)