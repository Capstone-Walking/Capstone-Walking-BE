package com.walking.api.web.dto.request;

import org.springframework.core.convert.converter.Converter;

public class OrderFilterConverter implements Converter<String, OrderFilter> {

	@Override
	public OrderFilter convert(String source) {
		return OrderFilter.ofRequest(source);
	}
}
