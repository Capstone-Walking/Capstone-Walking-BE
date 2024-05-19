package com.walking.image.service.minio

import com.walking.image.ImageWriteResponse
import com.walking.image.MinioImageStoreClient
import com.walking.image.service.UploadImageService
import com.walking.image.util.ImageArgsGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File

@Profile("minio")
@Service
class MinioUploadImageService(
    @Value("\${minio.bucket-name}") val bucket: String,
    private val imageStoreClient: MinioImageStoreClient
) : UploadImageService {
    override fun execute(name: String, file: File): ImageWriteResponse {
        ImageArgsGenerator.putImage(bucket, name, file).let { args ->
            return imageStoreClient.putObject(args) ?: throw Exception("Failed to upload image")
        }
    }
}