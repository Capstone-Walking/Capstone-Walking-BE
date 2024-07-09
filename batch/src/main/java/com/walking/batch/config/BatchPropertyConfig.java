package com.walking.batch.config;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchPropertyConfig {
	@Bean
	@ConfigurationProperties(prefix = "spring.batch")
	public BatchProperties batchProperties() {
		return new BatchProperties();
	}
}
