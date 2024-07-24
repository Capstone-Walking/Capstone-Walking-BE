package com.walking.api.image

import java.io.InputStream

data class ImageGetPresignedObjectUrlArgs(
    val bucket: String,
    val `object`: String,
    val method: String
)

data class ImagePutObjectArgs(
    val bucket: String,
    val `object`: String,
    val stream: InputStream,
    val objectSize: Long,
    val partSize: Long,
    val contentType: String = "image/jpg"
)

data class ImageRemoveObjectArgs(
    val bucket: String,
    val `object`: String
)