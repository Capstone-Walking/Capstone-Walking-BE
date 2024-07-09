package com.walking.batch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchFactoryConfig {
	@Bean
	public JobBuilderFactory jobBuilderFactory(BatchConfigurer configurer) throws Exception {
		return new JobBuilderFactory(configurer.getJobRepository());
	}

	@Bean
	public StepBuilderFactory stepBuilderFactory(BatchConfigurer configurer) throws Exception {
		return new StepBuilderFactory(
				configurer.getJobRepository(), configurer.getTransactionManager());
	}
}
