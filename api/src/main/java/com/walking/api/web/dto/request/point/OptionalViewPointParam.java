package com.walking.api.web.dto.request.point;

import java.util.Objects;
import java.util.Optional;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OptionalViewPointParam {

	private ViewPointParam viewPointParam;

	public Optional<ViewPointParam> get() {
		return Optional.ofNullable(viewPointParam);
	}

	public boolean isPresent() {
		return Objects.nonNull(viewPointParam);
	}
}
