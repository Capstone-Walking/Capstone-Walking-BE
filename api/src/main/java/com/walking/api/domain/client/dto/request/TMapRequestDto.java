package com.walking.api.domain.client.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TMapRequestDto implements MapRequestDto {

	private Double startX;
	private Double startY;

	private Double endX;
	private Double endY;

	private String startName;

	private String endName;
}
