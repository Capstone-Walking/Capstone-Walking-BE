package com.walking.batch.traffic.service;

import com.walking.batch.client.config.BatchWebClientConfig;
import com.walking.batch.client.property.SeoulTrafficColorRequestProperty;
import com.walking.batch.traffic.service.dto.TrafficColorVO;
import java.net.URI;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TrafficColorApiServiceV2 extends TDataApiClient<Iterable<TrafficColorVO>> {

	public TrafficColorApiServiceV2(
			@Qualifier(BatchWebClientConfig.WEB_CLIENT) WebClient webClient,
			SeoulTrafficColorRequestProperty colorRequestProperty) {
		super(webClient, colorRequestProperty);
	}

	@Override
	protected MultiValueMap<String, String> getParameters() {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.put("apiKey", Arrays.asList(super.getApiKey()));
		return parameters;
	}

	@Override
	protected Iterable<TrafficColorVO> doRequest(URI uri) {
		return getWebClient()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToFlux(TrafficColorVO.class)
				.collectList()
				.block();
	}
}
