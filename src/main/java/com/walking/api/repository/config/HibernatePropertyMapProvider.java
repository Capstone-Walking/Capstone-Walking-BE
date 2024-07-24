package com.walking.api.repository.config;

import java.util.Map;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.stereotype.Component;

@Component
public class HibernatePropertyMapProvider {

	private final HibernateProperties hibernateProperties;
	private final JpaProperties jpaProperties;

	public HibernatePropertyMapProvider(
			HibernateProperties hibernateProperties, JpaProperties jpaProperties) {
		this.hibernateProperties = hibernateProperties;
		this.jpaProperties = jpaProperties;
	}

	public Map<String, Object> get() {
		return hibernateProperties.determineHibernateProperties(
				jpaProperties.getProperties(), new HibernateSettings());
	}
}
