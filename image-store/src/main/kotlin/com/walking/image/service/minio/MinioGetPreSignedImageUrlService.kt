package com.walking.image.service.minio

import com.walking.image.MinioImageStoreClient
import com.walking.image.service.GetPreSignedImageUrlService
import com.walking.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("local")
@Service
class MinioGetPreSignedImageUrlService(
    @Value("\${minio.bucket-name}") val bucket: String,
    private val imageStoreClient: MinioImageStoreClient
) : GetPreSignedImageUrlService {
    override fun execute(image: String): String {
        ImageArgsGenerator.preSignedUrl(bucket, image).let { args ->
            return imageStoreClient.getPresignedObjectUrl(args) ?: throw Exception("Failed to get pre-signed url")
        }
    }
}