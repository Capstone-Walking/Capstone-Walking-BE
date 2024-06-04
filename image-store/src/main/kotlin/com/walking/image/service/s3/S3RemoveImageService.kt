package com.walking.image.service.s3

import com.walking.image.S3ImageStoreClient
import com.walking.image.service.RemoveImageService
import com.walking.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!local")
@Service
class S3RemoveImageService(
    @Value("\${s3.bucket-name}") val bucket: String,
    private val imageStoreClient: S3ImageStoreClient
) : RemoveImageService {
    override fun execute(image: String): Boolean {
        ImageArgsGenerator.remove(bucket, image).let { args ->
            return imageStoreClient.removeObject(args)
        }
    }
}