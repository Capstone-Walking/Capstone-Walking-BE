package com.walking.api.domain.traffic.dto;

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
@Builder(toBuilder = true)
public class BrowseTrafficsUseCaseIn {

	private Long trafficId;
	@Builder.Default private Long memberId = -1L;

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
