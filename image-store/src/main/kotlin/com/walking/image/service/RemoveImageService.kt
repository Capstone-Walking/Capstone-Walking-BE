package com.walking.image.service

fun interface RemoveImageService {
    fun execute(image: String): Boolean
}