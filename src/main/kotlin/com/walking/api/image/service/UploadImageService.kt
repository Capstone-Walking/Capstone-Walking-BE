package com.walking.api.image.service

import com.walking.api.image.ImageWriteResponse
import java.io.File

fun interface UploadImageService {
    fun execute(name: String, file: File): ImageWriteResponse
}