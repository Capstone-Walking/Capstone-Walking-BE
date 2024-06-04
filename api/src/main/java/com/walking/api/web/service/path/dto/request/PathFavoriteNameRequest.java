package com.walking.api.web.service.path.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PathFavoriteNameRequest {

	@NotBlank private String name;

	@NotBlank private String startAlias;
	@NotBlank private String endAlias;
}
