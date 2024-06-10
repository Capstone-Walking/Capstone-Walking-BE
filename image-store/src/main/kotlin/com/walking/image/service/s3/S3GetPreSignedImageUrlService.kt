package com.walking.image.service.s3

import com.walking.image.S3ImageStoreClient
import com.walking.image.service.GetPreSignedImageUrlService
import com.walking.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!local")
@Service
class S3GetPreSignedImageUrlService(
    @Value("\${s3.bucket-name}") val bucket: String,
    private val imageStoreClient: S3ImageStoreClient
) : GetPreSignedImageUrlService {
    override fun execute(image: String): String {
        println("S3GetPreSignedImageUrlService.execute\n")
        println("image: $image")
        ImageArgsGenerator.preSignedUrl(bucket, image).let { args ->
            println("args: $args")
            return imageStoreClient.getPresignedObjectUrl(args) ?: throw Exception("Failed to get pre-signed url")
        }
    }
}