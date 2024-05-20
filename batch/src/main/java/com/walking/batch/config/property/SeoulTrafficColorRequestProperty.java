package com.walking.batch.config.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SeoulTrafficColorRequestProperty extends SeoulTrafficRequestProperty {

	private final String colorPath;

	public SeoulTrafficColorRequestProperty(
			@Qualifier("seoulTrafficRequestProperty") SeoulTrafficRequestProperty trafficRequestProperty,
			@Value("${api.traffic.seoul.colorPath}") String colorPath) {
		super(trafficRequestProperty.getApiKey(), trafficRequestProperty.getBaseUrl());
		this.colorPath = colorPath;
	}

	@Override
	public String getPath() {
		return this.colorPath;
	}
}
