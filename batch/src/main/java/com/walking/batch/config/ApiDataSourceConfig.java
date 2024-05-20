package com.walking.batch.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration(
		exclude = {
			DataSourceAutoConfiguration.class,
			DataSourceTransactionManagerAutoConfiguration.class,
		})
public class ApiDataSourceConfig {

	public static final String DATASOURCE_NAME = BatchConfig.BEAN_NAME_PREFIX + "ApiDataSource";
	public static final String TXM_NAME = BatchConfig.BEAN_NAME_PREFIX + "ApiTxm";

	public static final String JDBC_TEMPLATE_NAME = BatchConfig.BEAN_NAME_PREFIX + "ApiJdbcTemplate";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource apiDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = TXM_NAME)
	public PlatformTransactionManager apiTransactionManager() {
		return new JdbcTransactionManager(apiDataSource());
	}

	@Bean(name = JDBC_TEMPLATE_NAME)
	public JdbcTemplate apiJdbcTemplate() {
		return new JdbcTemplate(apiDataSource());
	}
}
