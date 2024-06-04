package com.walking.api.repository.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = ApiRepositoryConfig.BASE_PACKAGE)
public class ApiRepositoryConfig {

	public static final String BASE_PACKAGE = "com.walking.api.repository";
	public static final String SERVICE_NAME = "walking";
	public static final String MODULE_NAME = "api-repository";
	public static final String BEAN_NAME_PREFIX = "apiRepository";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
