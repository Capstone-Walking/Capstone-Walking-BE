package com.walking.traffic.batch.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource

@EnableScheduling
@Configuration
@EnableBatchProcessing
class BatchConfig {
    @Bean
    fun batchProperties(): BatchProperties {
        return BatchProperties()
    }

    @Bean
    fun batchDataSourceInitializer(
        dataSource: DataSource, properties: BatchProperties,
    ): BatchDataSourceScriptDatabaseInitializer {
        return BatchDataSourceScriptDatabaseInitializer(dataSource, properties.jdbc)
    }
}