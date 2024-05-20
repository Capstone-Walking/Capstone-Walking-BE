package com.walking.batch.service;

import com.walking.batch.config.property.SeoulTrafficColorRequestProperty;
import com.walking.batch.service.dto.TrafficColorResponseDto;
import java.net.URI;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TrafficColorApiServiceV2 extends TDataApiClient<Iterable<TrafficColorResponseDto>> {

	public TrafficColorApiServiceV2(
			WebClient webClient, SeoulTrafficColorRequestProperty colorRequestProperty) {
		super(webClient, colorRequestProperty);
	}

	@Override
	protected MultiValueMap<String, String> getParameters() {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.put("apiKey", Arrays.asList(super.getApiKey()));
		return parameters;
	}

	@Override
	protected Iterable<TrafficColorResponseDto> doRequest(URI uri) {
		return getWebClient()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToFlux(TrafficColorResponseDto.class)
				.collectList()
				.block();
	}
}
