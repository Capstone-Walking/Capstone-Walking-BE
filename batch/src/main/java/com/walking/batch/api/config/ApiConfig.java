package com.walking.batch.api.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ApiConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource batchApiDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public PlatformTransactionManager batchApiTransactionManager(DataSource batchApiDataSource) {
		return new DataSourceTransactionManager(batchApiDataSource);
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource batchApiDataSource) {
		return new JdbcTemplate(batchApiDataSource);
	}
}
