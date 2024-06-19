package com.walking.batch.client.config;

import static com.walking.batch.config.BatchConfig.BEAN_NAME_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class BatchWebClientConfig {

	public static final String WEB_CLIENT = BEAN_NAME_PREFIX + "WebClient";

	private final WebClient.Builder builder;

	@Bean(name = WEB_CLIENT)
	public WebClient webClient() {
		return builder
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
				.build();
	}
}
