package com.walking.image

interface ImageStoreClient {

    fun getPresignedObjectUrl(fileName: ImageGetPresignedObjectUrlArgs): String?

    fun removeObject(fileName: ImageRemoveObjectArgs): Boolean

    fun putObject(fileName: ImagePutObjectArgs): ImageWriteResponse?
}