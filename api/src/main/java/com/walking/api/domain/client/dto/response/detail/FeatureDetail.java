package com.walking.api.domain.client.dto.response.detail;

import lombok.Data;

@Data
public class FeatureDetail {
	private String type;
	private GeometryDetail geometryDetail;
	private PropertyDetails propertyDetails;
}
