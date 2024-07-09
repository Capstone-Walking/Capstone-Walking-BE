package com.walking.batch.config;

import static com.walking.batch.config.BatchDataSourceConfig.DATASOURCE_NAME;
import static com.walking.batch.config.BatchDataSourceConfig.TRANSACTION_MANAGER_NAME;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class DelegatedBatchConfigurer extends JpaBatchConfigurer {
	public PlatformTransactionManager transactionManager;

	public DelegatedBatchConfigurer(
			BatchProperties properties,
			@Qualifier(DATASOURCE_NAME) DataSource dataSource,
			TransactionManagerCustomizers transactionManagerCustomizers,
			EntityManagerFactory entityManagerFactory,
			@Qualifier(TRANSACTION_MANAGER_NAME) PlatformTransactionManager transactionManager) {
		super(properties, dataSource, transactionManagerCustomizers, entityManagerFactory);
		this.transactionManager = transactionManager;
	}

	@Override
	protected PlatformTransactionManager createTransactionManager() {
		return transactionManager;
	}
}
