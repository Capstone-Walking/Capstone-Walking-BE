package com.walking.api.batch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Primary
@Component
public class DelegatedBatchConfigurer extends JpaBatchConfigurer {
	public PlatformTransactionManager transactionManager;

	public DelegatedBatchConfigurer(
			BatchProperties properties,
			DataSource dataSource,
			TransactionManagerCustomizers transactionManagerCustomizers,
			EntityManagerFactory entityManagerFactory,
			PlatformTransactionManager transactionManager) {
		super(properties, dataSource, transactionManagerCustomizers, entityManagerFactory);
		this.transactionManager = transactionManager;
	}

	@Override
	protected PlatformTransactionManager createTransactionManager() {
		return transactionManager;
	}
}
