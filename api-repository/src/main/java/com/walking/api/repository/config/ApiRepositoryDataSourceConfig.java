package com.walking.api.repository.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(
		exclude = {
			DataSourceAutoConfiguration.class,
			DataSourceTransactionManagerAutoConfiguration.class,
		})
public class ApiRepositoryDataSourceConfig {

	public static final String DATASOURCE_NAME = ApiRepositoryConfig.BEAN_NAME_PREFIX + "DataSource";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = "api.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
