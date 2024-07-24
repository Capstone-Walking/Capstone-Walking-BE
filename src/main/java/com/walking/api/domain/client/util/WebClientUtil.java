package com.walking.api.domain.client.util;

import com.walking.api.domain.client.config.WebClientConfig;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientUtil {

	private final WebClientConfig webClientConfig;
	CountDownLatch cdl = new CountDownLatch(2);

	public <T> T get(String url, Class<T> responseDtoClass) {
		return webClientConfig
				.webClient()
				.method(HttpMethod.GET)
				.uri(url)
				.retrieve()
				.bodyToMono(responseDtoClass)
				.block();
	}

	public <T, V> T post(String url, V requestDto, Class<T> responseDtoClass) {
		return webClientConfig
				.webClient()
				.method(HttpMethod.POST)
				.uri(url)
				.bodyValue(requestDto)
				.retrieve()
				.bodyToMono(responseDtoClass)
				.block();
	}

	public <T, V> T postWithHeaders(
			String url, V requestDto, Class<T> responseDtoClass, Map<String, String> headers) {

		return webClientConfig
				.webClient()
				.method(HttpMethod.POST)
				.uri(url)
				.headers(httpHeaders -> headers.forEach(httpHeaders::set)) // 헤더를 설정
				.bodyValue(requestDto)
				.retrieve()
				.bodyToMono(responseDtoClass)
				.block();
	}
}
