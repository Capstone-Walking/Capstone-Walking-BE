package com.walking.api.image

data class ImageWriteResponse(
    val bucket: String,
    val region: String,
    val `object`: String,
    val etag: String,
    val versionId: String
)