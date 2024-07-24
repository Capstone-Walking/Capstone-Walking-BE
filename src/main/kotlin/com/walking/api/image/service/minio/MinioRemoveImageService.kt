package com.walking.api.image.service.minio

import com.walking.api.image.MinioImageStoreClient
import com.walking.api.image.service.RemoveImageService
import com.walking.api.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("local")
@Service
class MinioRemoveImageService(
    @Value("\${minio.bucket-name}") val bucket: String,
    private val imageStoreClient: MinioImageStoreClient
) : RemoveImageService {
    override fun execute(image: String): Boolean {
        ImageArgsGenerator.remove(bucket, image).let { args ->
            return imageStoreClient.removeObject(args)
        }
    }
}