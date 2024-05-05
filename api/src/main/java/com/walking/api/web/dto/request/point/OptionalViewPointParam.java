package com.walking.api.web.dto.request.point;

import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalViewPointParam {

	private ViewPointParam viewPointParam;

	public Optional<ViewPointParam> get() {
		return Optional.ofNullable(viewPointParam);
	}

	public boolean isPresent() {
		return Objects.isNull(viewPointParam);
	}
}
