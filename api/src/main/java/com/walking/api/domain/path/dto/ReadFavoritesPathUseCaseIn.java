package com.walking.api.domain.path.dto;

import com.walking.api.web.dto.request.OrderFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReadFavoritesPathUseCaseIn {
	private Long memberId;
	private String name;
	private OrderFilter orderFilter;

	public boolean isOrderFiltered() {
		return orderFilter != null;
	}
}
