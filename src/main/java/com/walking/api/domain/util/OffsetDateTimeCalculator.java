package com.walking.api.domain.util;

import java.time.Duration;
import java.time.OffsetDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class OffsetDateTimeCalculator {

	public static float getDifferenceInSeconds(OffsetDateTime start, OffsetDateTime end) {
		// 두 OffsetDateTime 객체 간의 차이를 Duration으로 계산
		Duration duration = Duration.between(start, end);

		// Duration을 초 단위로 변환하고 float 값으로 반환
		long seconds = duration.getSeconds();
		int nanoSeconds = duration.getNano();

		// 초 단위와 나노초 단위를 float 값으로 합산하여 반환
		return seconds + nanoSeconds / 1_000_000_000.0f;
	}
}
