package com.walking.api.domain.client.dto.response.detail;

import lombok.Data;

@Data
public class PropertyDetails {
	private Integer totalDistance;
	private Integer totalTime;
	private Integer index;
	private Integer pointIndex;
	private String name;
	private String description;
	private String direction;
	private String nearPoiName;
	private String nearPoiX;
	private String nearPoiY;
	private String intersectionName;
	private String facilityType;
	private String facilityName;
	private Integer turnType;
	private String pointType;
	private Integer distance;
	private Integer time;
	private Integer roadType;
	private Integer categoryRoadType;
	private Integer lineIndex;
}
