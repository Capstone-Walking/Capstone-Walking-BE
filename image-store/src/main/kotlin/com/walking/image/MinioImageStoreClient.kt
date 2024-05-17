package com.walking.image

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method

class MinioImageStoreClient(
    private val minIoClient: MinioClient
) : ImageStoreClient {

    override fun getPresignedObjectUrl(fileName: ImageGetPresignedObjectUrlArgs): String {
        GetPresignedObjectUrlArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .method(Method.valueOf(fileName.method))
            .build()
            .let { args ->
                return minIoClient.getPresignedObjectUrl(args)
            }
    }

    override fun removeObject(fileName: ImageRemoveObjectArgs) {
        RemoveObjectArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .build()
            .let { args ->
                minIoClient.removeObject(args)
            }
    }

    override fun putObject(fileName: ImagePutObjectArgs): ImageWriteResponse {
        PutObjectArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .stream(fileName.stream, fileName.objectSize, fileName.partSize)
            .contentType(fileName.contentType)
            .build()
            .let { args ->
                minIoClient.putObject(args).let { owr ->
                    return ImageWriteResponse(
                        owr.bucket(),
                        owr.region(),
                        owr.`object`(),
                        owr.etag(),
                        owr.versionId()
                    )
                }
            }
    }
}