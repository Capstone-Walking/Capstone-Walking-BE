package com.walking.api.image.service

fun interface RemoveImageService {
    fun execute(image: String): Boolean
}