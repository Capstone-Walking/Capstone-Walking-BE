package com.walking.api.batch.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
public class BatchConfig {
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.batch")
	public BatchProperties batchProperties() {
		return new BatchProperties();
	}

	@Bean
	public JobLauncherApplicationRunner jobLauncherApplicationRunner(
			JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository) {
		JobLauncherApplicationRunner runner =
				new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
		String jobNames = batchProperties().getJob().getNames();
		if (StringUtils.hasText(jobNames)) {
			runner.setJobNames(jobNames);
		}
		return runner;
	}

	@Bean
	BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
			DataSource dataSource, BatchProperties properties) {
		return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
	}
}
