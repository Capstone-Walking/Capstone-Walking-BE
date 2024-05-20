package com.walking.api.web.client.dto.response.detail;

import lombok.Data;

@Data
public class Feature {
	private String type;
	private Geometry geometry;
	private Properties properties;
}
