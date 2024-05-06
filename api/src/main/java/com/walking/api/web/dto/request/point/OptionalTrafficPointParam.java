package com.walking.api.web.dto.request.point;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
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
public class OptionalTrafficPointParam {

	@Nullable private TrafficPointParam trafficPointParam;

	public Optional<TrafficPointParam> get() {
		return Optional.ofNullable(trafficPointParam);
	}

	public boolean isPresent() {
		return Objects.nonNull(trafficPointParam);
	}
}
