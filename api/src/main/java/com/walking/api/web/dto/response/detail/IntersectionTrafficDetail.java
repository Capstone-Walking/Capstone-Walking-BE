package com.walking.api.web.dto.response.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntersectionTrafficDetail {

	// todo enum으로 변경
	private String direction;
	// todo enum으로 변경
	private Boolean status;
	private Long remainTime;
	private Long redCycle;
	private Long greenCycle;
}
