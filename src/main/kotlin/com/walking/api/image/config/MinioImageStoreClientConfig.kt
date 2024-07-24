package com.walking.api.image.config

import com.walking.api.image.MinioImageStoreClient
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent

@Profile("local")
@Configuration
class MinioImageStoreClientConfig(
    @Value("\${minio.url}") val url: String,
    @Value("\${minio.access-key}") val accessKey: String,
    @Value("\${minio.secret-key}") val secretKey: String,
    @Value("\${minio.bucket-name}") val bucket: String
) : ApplicationListener<ContextRefreshedEvent> {

    val log: Logger = LoggerFactory.getLogger(MinioImageStoreClientConfig::class.java)

    private var client: MinioClient? = null

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        client?.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucket)
                .build()
        )?.let { exist ->
            if (!exist) {
                client?.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build()
                )
                log.info("Create bucket $bucket")
            }
            log.info("Bucket $bucket already exists")
        }
    }

    @Bean
    fun minioImageStoreClient(): MinioImageStoreClient {
        MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build().let { client ->
                this.client = client
                return MinioImageStoreClient(client)
            }
    }
}