package com.walking.api.domain.traffic.service.model;

import com.walking.api.data.entity.traffic.TrafficEntity;
import com.walking.api.data.entity.traffic.constant.TrafficColor;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/** 예측한 결과를 나타내는 클래스 */
@Getter
@AllArgsConstructor
@ToString
public class PredictedTraffic {

	private TrafficEntity traffic;
	private Float redCycle;
	private Float greenCycle;
	private TrafficColor currentColor; // 현재 신호 색상
	private Float currentTimeLeft; // 현재 신호 잔여시간

	public PredictedTraffic(TrafficEntity traffic) {
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
		return getRedCycle().isPresent();
	}

	/**
	 * 초록불의 사이클이 계산 되었는지 반환합니다.
	 *
	 * @return greenCycle 이 존재하면 true
	 */
	public boolean isPredictedGreenCycle() {
		return getGreenCycle().isPresent();
	}

	/**
	 * 사이클 예측이 끝났는지 여부를 반환합니다.
	 *
	 * @return 예측이 끝났으면 true, 아니면 false
	 */
	public boolean isPredictCycleSuccessful() {
		return isPredictedRedCycle() && isPredictedGreenCycle();
	}

	/**
	 * 사이클, 현재 신호 색상 및 잔여시간에 대해 모두 정상적으로 예측이 되었는지 판단합니다.
	 *
	 * @return 모두 예측되면 true, 그렇지 않으면 false
	 */
	public boolean isAllPredicted() {
		return isPredictedGreenCycle()
				&& Objects.nonNull(currentColor)
				&& currentTimeLeft < 600
				&& currentTimeLeft > 0;
	}

	// R -> G, G -> R 따로 찾지말고 한 번 순회할 때 모두 찾아내면 좋겠다
	public void predictCycle(RecentTrafficDetails recentTrafficDetails) {
		if (!this.isPredictedGreenCycle()) {
			this.updateGreenCycle(recentTrafficDetails.predictGreenCycle());
		}
		if (!this.isPredictedRedCycle()) {
			this.updateRedCycle(recentTrafficDetails.predictRedCycle());
		}
	}

	private void updateRedCycle(Optional<Float> redCycle) {
		if (redCycle.isEmpty() || redCycle.get() < 0 || redCycle.get() > 1000) {
			this.redCycle = null;
			return;
		}
		this.redCycle = redCycle.orElse(null);
	}

	private void updateGreenCycle(Optional<Float> greenCycle) {
		if (greenCycle.isEmpty() || greenCycle.get() < 0 || greenCycle.get() > 1000) {
			this.greenCycle = null;
			return;
		}
		this.greenCycle = greenCycle.orElse(null);
	}

	public void updateCurrentColor(TrafficColor color) {
		this.currentColor = color;
	}

	public void updateCurrentTimeLeft(Float timeLeft) {
		this.currentTimeLeft = timeLeft;
	}

	/**
	 * 색상에 따른 사이클을 반환합니다.
	 *
	 * @return 파라미터로 전달받은 색상의 사이클
	 */
	public Float getCycleByColor(TrafficColor color) {
		if (color.isGreen()) {
			return greenCycle;
		}
		if (color.isRed()) {
			return redCycle;
		}
		return -1f;
	}

	public Optional<Float> getRedCycle() {
		return Optional.ofNullable(redCycle);
	}

	public Optional<Float> getGreenCycle() {
		return Optional.ofNullable(greenCycle);
	}

	public Optional<TrafficColor> getCurrentColor() {
		return Optional.ofNullable(currentColor);
	}

	public Optional<Float> getCurrentTimeLeft() {
		return Optional.ofNullable(currentTimeLeft);
	}

	public String getCurrentColorDescription() {
		if (this.getCurrentColor().isPresent()) {
			return this.getCurrentColor().get().toString();
		}
		return "";
	}
}
