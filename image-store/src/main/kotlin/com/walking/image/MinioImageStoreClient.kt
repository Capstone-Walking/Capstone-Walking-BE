package com.walking.image

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MinioImageStoreClient(
    private val minIoClient: MinioClient
) : ImageStoreClient {

    val log: Logger = LoggerFactory.getLogger(MinioImageStoreClient::class.java)

    override fun getPresignedObjectUrl(fileName: ImageGetPresignedObjectUrlArgs): String? {
        GetPresignedObjectUrlArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .method(Method.valueOf(fileName.method))
            .build()
            .let { args ->
                try {
                    return minIoClient.getPresignedObjectUrl(args)
                } catch (e: Exception) {
                    log.debug("Failed to get presigned url for object: ${fileName.`object`}")
                    return null
                }
            }
    }

    override fun removeObject(fileName: ImageRemoveObjectArgs): Boolean {
        RemoveObjectArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .build()
            .let { args ->
                try {
                    minIoClient.removeObject(args)
                    return true
                } catch (e: Exception) {
                    log.debug("Failed to remove object: ${fileName.`object`}")
                    return false
                }
            }
    }

    override fun putObject(fileName: ImagePutObjectArgs): ImageWriteResponse? {
        PutObjectArgs.builder()
            .bucket(fileName.bucket)
            .`object`(fileName.`object`)
            .stream(fileName.stream, fileName.objectSize, fileName.partSize)
            .contentType(fileName.contentType)
            .build()
            .let { args ->
                try {
                    minIoClient.putObject(args).let { owr ->
                        return ImageWriteResponse(
                            owr.bucket(),
                            owr.region(),
                            owr.`object`(),
                            owr.etag(),
                            owr.versionId()
                        )
                    }
                } catch (e: Exception) {
                    log.debug("Failed to put object: ${fileName.`object`}")
                    return null
                }
            }
    }
}