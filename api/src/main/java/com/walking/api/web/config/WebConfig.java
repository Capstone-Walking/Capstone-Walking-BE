package com.walking.api.web.config;

import com.walking.api.security.config.CorsConfigurationSourceProperties;
import com.walking.api.web.dto.request.OrderFilterConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final CorsConfigurationSourceProperties corsProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping(corsProperties.getPathPattern())
				// .allowedOriginPatterns(corsProperties.getOriginPattern())
				.allowedOrigins(corsProperties.getOriginPatterns())
				.allowedMethods(corsProperties.getAllowedMethods())
				.allowedHeaders(corsProperties.getAllowedHeaders())
				.allowCredentials(corsProperties.getAllowCredentials())
				.exposedHeaders(corsProperties.getExposedHeaders())
				.maxAge(corsProperties.getMaxAge());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new OrderFilterConverter());
	}
}
