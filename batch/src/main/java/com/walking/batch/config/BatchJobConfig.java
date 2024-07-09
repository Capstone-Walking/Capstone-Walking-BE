package com.walking.batch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {

	@Bean
	public JobRegistry jobRegistry() {
		return new MapJobRegistry();
	}

	@Bean
	public JobRepository jobRepository(BatchConfigurer configurer) throws Exception {
		return configurer.getJobRepository();
	}

	@Bean
	public JobExplorer jobExplorer(BatchConfigurer configurer) throws Exception {
		return configurer.getJobExplorer();
	}
}
