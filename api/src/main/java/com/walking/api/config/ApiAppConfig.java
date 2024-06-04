package com.walking.api.config;

import com.walking.member.api.config.MemberApiConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MemberApiConfig.class})
@Configuration
@ComponentScan(basePackages = ApiAppConfig.BASE_PACKAGE)
public class ApiAppConfig {

	public static final String BASE_PACKAGE = "com.walking.api";
	public static final String SERVICE_NAME = "walking";
	public static final String MODULE_NAME = "api";
	public static final String BEAN_NAME_PREFIX = "api";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
