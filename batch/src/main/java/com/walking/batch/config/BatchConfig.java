package com.walking.batch.config;

import com.walking.data.DataConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = BatchConfig.BASE_PACKAGE)
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@Import(value = {DataConfig.class})
public class BatchConfig {

	public static final String BASE_PACKAGE = "com.walking.batch";
	public static final String SERVICE_NAME = "walking";
	public static final String MODULE_NAME = "batch";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
