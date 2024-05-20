package com.walking.api.web.dto.request;

import java.util.Objects;
import lombok.Getter;

@Getter
public enum OrderFilter {
	NAME("name"),
	CREATEDAT("createdAt"),
	;

	private String field;

	OrderFilter(String field) {
		this.field = field;
	}

	private static OrderFilter defaultValue() {
		return CREATEDAT;
	}

	/**
	 * 요청에 따라 정렬할 필드를 반환합니다.
	 *
	 * @param source 요청에 따라 정렬할 필드
	 * @return 정렬할 필드, 요청이 없는 경우 생성일자를 반환합니다.
	 */
	public static OrderFilter ofRequest(String source) {
		if (Objects.isNull(source) || source.isEmpty()) {
			return defaultValue();
		}

		try {
			return OrderFilter.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			return defaultValue();
		}
	}
}
