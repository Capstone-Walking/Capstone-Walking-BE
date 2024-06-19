package com.walking.image.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.walking.image.S3ImageStoreClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!local")
@Configuration
class S3ImageStoreConfig(
    @Value("\${s3.url}") val url: String,
    @Value("\${s3.access-key}") val accessKey: String,
    @Value("\${s3.secret-key}") val secretKey: String,
    @Value("\${s3.bucket-name}") val bucket: String,
    @Value("\${s3.region}") val region: String
) {

    var log: Logger = LoggerFactory.getLogger(S3ImageStoreConfig::class.java)

    private var client: AmazonS3Client? = null

    @Bean
    fun s3ImageStoreClient(): S3ImageStoreClient {
        AmazonS3Client.builder()
            .withRegion(region)
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(
                        accessKey,
                        secretKey
                    )
                )
            ).build().let { client ->
                this.client = client as AmazonS3Client
                return S3ImageStoreClient(client, region)
            }
    }
}