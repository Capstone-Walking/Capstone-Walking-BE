package com.walking.api.batch.traffic.service;

import com.walking.api.batch.client.property.SeoulTrafficTimeLeftRequestProperty;
import com.walking.api.batch.traffic.service.dto.TrafficTimeLeftVO;
import java.net.URI;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TrafficTimeLeftApiServiceV2 extends TDataApiClient<Iterable<TrafficTimeLeftVO>> {

	public TrafficTimeLeftApiServiceV2(
			WebClient webClient, SeoulTrafficTimeLeftRequestProperty timeLeftRequestProperty) {
		super(webClient, timeLeftRequestProperty);
	}

	@Override
	protected MultiValueMap<String, String> getParameters() {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.put("apiKey", Arrays.asList(super.getApiKey()));
		return parameters;
	}

	@Override
	protected Iterable<TrafficTimeLeftVO> doRequest(URI uri) {
		return getWebClient()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToFlux(TrafficTimeLeftVO.class)
				.collectList()
				.block();
	}
}
