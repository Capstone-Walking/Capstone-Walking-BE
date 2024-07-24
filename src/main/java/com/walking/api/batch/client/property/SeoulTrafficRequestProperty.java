package com.walking.api.batch.client.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SeoulTrafficRequestProperty extends TrafficRequestProperty {

	SeoulTrafficRequestProperty(
			@Value("${api.traffic.seoul.apiKey}") String apiKey,
			@Value("${api.traffic.seoul.baseUrl}") String baseUrl) {
		super(apiKey, baseUrl);
	}

	@Override
	public String getPath() {
		return getBaseUrl();
	}
}
