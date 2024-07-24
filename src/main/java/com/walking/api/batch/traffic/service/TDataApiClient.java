package com.walking.api.batch.traffic.service;

import com.walking.api.batch.client.property.TrafficRequestProperty;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public abstract class TDataApiClient<T> {
	private final String BASE_URL = "http://t-data.seoul.go.kr";
	private final WebClient webClient;
	private final TrafficRequestProperty property;

	public T request() {
		URI uri = makeURI();
		return doRequest(uri);
	}

	private URI makeURI() {
		return UriComponentsBuilder.fromUriString(BASE_URL)
				.path(property.getPath())
				.queryParams(getParameters())
				.encode()
				.build()
				.toUri();
	}

	protected abstract MultiValueMap<String, String> getParameters();

	protected abstract T doRequest(URI uri);

	protected WebClient getWebClient() {
		return this.webClient;
	}

	protected String getApiKey() {
		return this.property.getApiKey();
	}
}
