package com.walking.batch.config;

import com.walking.data.DataConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

@Configuration
@ComponentScan(basePackages = BatchConfig.BASE_PACKAGE)
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@EnableScheduling
@Import(value = {DataConfig.class})
public class BatchConfig {

	public static final String BASE_PACKAGE = "com.walking.batch";
	public static final String SERVICE_NAME = "walking";
	public static final String MODULE_NAME = "batch";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;

	@Bean
	@ConditionalOnMissingBean
	public JobLauncherApplicationRunner jobLauncherApplicationRunner(
			JobLauncher jobLauncher,
			JobExplorer jobExplorer,
			JobRepository jobRepository,
			BatchProperties properties) {
		JobLauncherApplicationRunner runner =
				new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
		String jobNames = properties.getJob().getNames();
		if (StringUtils.hasText(jobNames)) {
			runner.setJobNames(jobNames);
		}
		return runner;
	}
}
