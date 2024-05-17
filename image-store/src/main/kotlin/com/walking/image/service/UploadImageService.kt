package com.walking.image.service

import com.walking.image.ImageWriteResponse
import java.io.File

fun interface UploadImageService {
    fun execute(name: String, file: File): ImageWriteResponse
}