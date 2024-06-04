package com.walking.image.service

fun interface GetPreSignedImageUrlService {
    fun execute(image: String): String
}