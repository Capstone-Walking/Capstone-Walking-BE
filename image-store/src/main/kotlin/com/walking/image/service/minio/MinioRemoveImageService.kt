package com.walking.image.service.minio

import com.walking.image.MinioImageStoreClient
import com.walking.image.service.RemoveImageService
import com.walking.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("minio")
@Service
class MinioRemoveImageService(
    @Value("\${minio.bucket-name}") val bucket: String,
    private val imageStoreClient: MinioImageStoreClient
) : RemoveImageService {
    override fun execute(image: String): Boolean {
        try {
            ImageArgsGenerator.remove(bucket, image).let { args ->
                imageStoreClient.removeObject(args)
                return true
            }
        } catch (e: Exception) {
            return false
        }
    }
}