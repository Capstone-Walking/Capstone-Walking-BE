package com.walking.batch.config;

import static com.walking.batch.config.BatchConfig.BEAN_NAME_PREFIX;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchDataSourceConfig {

	public static final String BASE_PACKAGE = BatchConfig.BASE_PACKAGE;

	// base property prefix for jpa datasource
	private static final String BASE_PROPERTY_PREFIX = BatchConfig.PROPERTY_PREFIX;

	// bean name for jpa datasource configuration
	public static final String ENTITY_BEAN_NAME_PREFIX = BEAN_NAME_PREFIX;
	public static final String TRANSACTION_MANAGER_NAME =
			ENTITY_BEAN_NAME_PREFIX + "TransactionManager";
	public static final String DATASOURCE_NAME = ENTITY_BEAN_NAME_PREFIX + "DataSource";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = "spring.batch.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = TRANSACTION_MANAGER_NAME)
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
			@Qualifier(DATASOURCE_NAME) DataSource dataSource, BatchProperties properties) {
		return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
	}
}
