package com.walking.api.domain.client.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@Slf4j
public class WebClientConfig {

	DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

	@Bean
	public WebClient webClient() {

		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
		return WebClient.builder()
				.uriBuilderFactory(factory)
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
				.build();
	}

	@Bean
	public ConnectionProvider connectionProvider() {
		return ConnectionProvider.builder("http-pool")
				.maxConnections(100)
				.pendingAcquireTimeout(Duration.ofMillis(0))
				.pendingAcquireMaxCount(-1)
				.maxIdleTime(Duration.ofMillis(1000L))
				.build();
	}
}
