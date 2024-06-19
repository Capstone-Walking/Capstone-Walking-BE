package com.walking.api.domain.path.dto;

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
public class UpdateRoutePathNameUseCaseIn {
	private Long memberId;
	private Long pathId;
	private String name;
	private String startAlias;
	private String endAlias;
}
