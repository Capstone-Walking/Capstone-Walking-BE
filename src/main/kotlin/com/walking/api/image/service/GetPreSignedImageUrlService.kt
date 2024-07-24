package com.walking.api.image.service

fun interface GetPreSignedImageUrlService {
    fun execute(image: String): String
}