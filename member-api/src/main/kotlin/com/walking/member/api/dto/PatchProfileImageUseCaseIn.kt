package com.walking.member.api.dto

import java.io.File

data class PatchProfileImageUseCaseIn(
    val id: Long,
    val image: File
)