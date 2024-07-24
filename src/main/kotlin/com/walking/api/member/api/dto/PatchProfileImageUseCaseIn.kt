package com.walking.api.member.api.dto

import java.io.File

data class PatchProfileImageUseCaseIn(
    val id: Long,
    val image: File
)