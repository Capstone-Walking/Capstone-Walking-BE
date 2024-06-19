package com.walking.batch.client.property;

import lombok.Getter;

@Getter
public abstract class TrafficRequestProperty {

	private final String apiKey;
	private final String baseUrl;

	public TrafficRequestProperty(String apiKey, String baseUrl) {
		this.apiKey = apiKey;
		this.baseUrl = baseUrl;
	}

	public abstract String getPath();
}
