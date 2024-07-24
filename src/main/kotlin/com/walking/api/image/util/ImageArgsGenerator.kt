package com.walking.api.image.util

import com.walking.api.image.ImageGetPresignedObjectUrlArgs
import com.walking.api.image.ImagePutObjectArgs
import com.walking.api.image.ImageRemoveObjectArgs
import io.minio.http.Method
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

class ImageArgsGenerator {
    companion object {
        fun preSignedUrl(bucket: String, image: String): ImageGetPresignedObjectUrlArgs {
            return ImageGetPresignedObjectUrlArgs(bucket, image, Method.GET.toString())
        }

        fun putImage(bucket: String, name: String, image: File, contentType: String = "image/jpg"): ImagePutObjectArgs {
            return ImagePutObjectArgs(bucket, name, BufferedInputStream(FileInputStream(image)), image.length(), -1, contentType)
        }

        fun remove(bucket: String, image: String): ImageRemoveObjectArgs {
            return ImageRemoveObjectArgs(bucket, image)
        }
    }
}