package com.walking.image

interface ImageStoreClient {

    fun getPresignedObjectUrl(fileName: ImageGetPresignedObjectUrlArgs): String

    fun removeObject(fileName: ImageRemoveObjectArgs)

    fun putObject(fileName: ImagePutObjectArgs): ImageWriteResponse
}