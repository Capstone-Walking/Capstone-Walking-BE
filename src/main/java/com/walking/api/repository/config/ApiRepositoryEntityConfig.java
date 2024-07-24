package com.walking.api.repository.config;

import com.walking.api.config.ApiAppConfig;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class ApiRepositoryEntityConfig {

	private final HibernatePropertyMapProvider hibernatePropertyMapProvider;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource,
			EntityManagerFactoryBuilder builder,
			ConfigurableListableBeanFactory beanFactory) {

		LocalContainerEntityManagerFactoryBean build =
				builder
						.dataSource(dataSource)
						.properties(hibernatePropertyMapProvider.get())
						.persistenceUnit("walking-api")
						.packages(ApiAppConfig.BASE_PACKAGE)
						.build();
		build
				.getJpaPropertyMap()
				.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));

		return build;
	}
}
