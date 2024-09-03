package com.walking.traffic.batch.job.seoul

import com.walking.traffic.batch.client.service.seoul.SeoulColorClient
import com.walking.traffic.batch.client.service.seoul.SeoulLeftTimeClient
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class SeoulLeftTimeJobConfig(
    @Value("\${spring.batch.chunk.size}") val chunkSize: Int,
    private val jobRepository: JobRepository,
    private val txm: PlatformTransactionManager,
    private val jdbcTemplate: JdbcTemplate,
    private val leftTimeClient: SeoulLeftTimeClient,
    private val colorClient: SeoulColorClient,
) {

    @Bean
    fun trafficDetailJob(): Job {
        return JobBuilder("seoulLeftTimeJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(trafficDetailStep())
            .build()
    }

    @Bean
    fun trafficDetailStep(): Step {
        return StepBuilder("seoulLeftTimeStep", jobRepository)
            .chunk<SeoulLeftTimeChunk, SeoulLeftTimeChunk>(chunkSize, txm)
            .reader(trafficDetailItemReader())
            .writer(trafficDetailItemWriter())
            .build()
    }

    @Bean
    fun trafficDetailItemReader(): ItemReader<SeoulLeftTimeChunk> {
        return SeoulLeftTimeItemReader(leftTimeClient, colorClient)
    }

    @Bean
    fun trafficDetailItemWriter(): ItemWriter<SeoulLeftTimeChunk> {
        return SeoulLeftTimeItemWriter(jdbcTemplate, txm)
    }
}