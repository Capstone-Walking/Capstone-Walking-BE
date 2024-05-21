package com.walking.api.service.dto;

import com.walking.data.entity.traffic.TrafficEntity;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/** 예측한 결과를 나타내는 클래스 */
@Getter
@AllArgsConstructor
@ToString
public class PredictData {

	private TrafficEntity traffic;
	private Float redCycle;
	private Float greenCycle;

	public PredictData(TrafficEntity traffic) {
		this.traffic = traffic;
		redCycle = null;
		greenCycle = null;
	}

	/**
	 * 빨간불의 사이클이 계산 되었는지 반환합니다.
	 *
	 * @return redCycle 이 존재하면 true
	 */
	public boolean isPredictedRedCycle() {
		return Objects.nonNull(getRedCycle());
	}

	/**
	 * 초록불의 사이클이 계산 되었는지 반환합니다.
	 *
	 * @return greenCycle 이 존재하면 true
	 */
	public boolean isPredictedGreenCycle() {
		return Objects.nonNull(getGreenCycle());
	}

	/**
	 * 예측이 끝났는지 여부를 반환합니다.
	 *
	 * @return 예측이 끝났으면 true, 아니면 false
	 */
	public boolean isComplete() {
		return isPredictedRedCycle() && isPredictedGreenCycle();
	}

	public void updateRedCycle(Optional<Float> redCycle) {
		this.redCycle = redCycle.orElse(null);
	}

	public void updateGreenCycle(Optional<Float> greenCycle) {
		this.greenCycle = greenCycle.orElse(null);
	}
}
