package com.walking.api.web.dto.request;

import lombok.Getter;

@Getter
public enum OrderFilter {
	NAME("name"),
	CREATEDAT("createdAt");

	private String field;

	OrderFilter(String field) {
		this.field = field;
	}

	public static OrderFilter ofRequest(String source) {
		return OrderFilter.valueOf(source.toUpperCase());
	}
}
