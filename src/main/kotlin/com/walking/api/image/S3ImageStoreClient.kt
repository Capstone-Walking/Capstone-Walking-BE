package com.walking.api.image

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class S3ImageStoreClient(
    private val s3client: AmazonS3Client,
    private val region: String
) : ImageStoreClient {

    val log: Logger = LoggerFactory.getLogger(S3ImageStoreClient::class.java)

    override fun getPresignedObjectUrl(fileName: ImageGetPresignedObjectUrlArgs): String? {
        GeneratePresignedUrlRequest(
            fileName.bucket,
            fileName.`object`,
            HttpMethod.valueOf(fileName.method)
        )
            .let { args ->
                try {
                    s3client.generatePresignedUrl(args)
                        .let { url ->
                            return url.toString()
                        }
                } catch (e: Exception) {
                    log.debug("Failed to get presigned url for object: ${fileName.`object`}\n ${e.message}")
                    return null
                }
            }
    }

    override fun removeObject(fileName: ImageRemoveObjectArgs): Boolean {
        DeleteObjectRequest(fileName.bucket, fileName.`object`)
            .let { args ->
                try {
                    s3client.deleteObject(args)
                    return true
                } catch (e: Exception) {
                    log.debug("Failed to remove object: ${fileName.`object`}\n ${e.message}")
                    return false
                }
            }
    }

    override fun putObject(fileName: ImagePutObjectArgs): ImageWriteResponse? {
        PutObjectRequest(
            fileName.bucket,
            fileName.`object`,
            fileName.stream,
            ObjectMetadata().apply {
                contentType = fileName.contentType
            }
        )
            .let { args ->
                try {
                    s3client.putObject(args).let { owr ->
                        return ImageWriteResponse(
                            fileName.bucket,
                            region,
                            fileName.`object`,
                            owr.eTag ?: "",
                            owr.versionId ?: ""
                        )
                    }
                } catch (e: Exception) {
                    log.debug("Failed to put object: ${fileName.`object`}\n ${e.message}")
                    return null
                }
            }
    }
}