package com.walking.batch.client.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SeoulTrafficTimeLeftRequestProperty extends SeoulTrafficRequestProperty {

	private final String timeLeftPath;

	public SeoulTrafficTimeLeftRequestProperty(
			@Qualifier("seoulTrafficRequestProperty") SeoulTrafficRequestProperty trafficRequestProperty,
			@Value("${api.traffic.seoul.timeLeftPath}") String timeLeftPath) {
		super(trafficRequestProperty.getApiKey(), trafficRequestProperty.getBaseUrl());
		this.timeLeftPath = timeLeftPath;
	}

	@Override
	public String getPath() {
		return this.timeLeftPath;
	}
}
