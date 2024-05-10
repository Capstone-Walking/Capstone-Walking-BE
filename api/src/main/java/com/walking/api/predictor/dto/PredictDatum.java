package com.walking.api.predictor.dto;

import com.walking.api.predictor.PredictDatumPredictor;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** 예측한 결과를 나타내는 클래스 */
@Slf4j
@Getter
@AllArgsConstructor
@ToString
public class PredictDatum {

	private TrafficEntity traffic;
	@Nullable private Long redCycle;
	@Nullable private Long greenCycle;

	public PredictDatum(TrafficEntity traffic) {
		this.traffic = traffic;
	}

	/**
	 * 빨간불의 사이클을 갱신합니다.
	 *
	 * @param redCycle 빨간불의 사이클
	 * @return PredictDatum 객체를 반환합니다.
	 */
	public PredictDatum loadRedCycle(Long redCycle) {
		this.redCycle = redCycle;
		return this;
	}

	/**
	 * 초록불의 사이클을 갱신합니다.
	 *
	 * @param greenCycle 초록불의 사이클
	 * @return PredictDatum 객체를 반환합니다.
	 */
	public PredictDatum loadGreenCycle(Long greenCycle) {
		this.greenCycle = greenCycle;
		return this;
	}

	public Optional<Long> getRedCycle() {
		return Optional.ofNullable(redCycle);
	}

	public Optional<Long> getGreenCycle() {
		return Optional.ofNullable(greenCycle);
	}

	/**
	 * 빨간불의 사이클이 계산 되었는지 반환합니다.
	 *
	 * @return redCycle 이 존재하면 true
	 */
	public boolean isPredictedRedCycle() {
		return Objects.nonNull(redCycle);
	}

	/**
	 * 초록불의 사이클이 계산 되었는지 반환합니다.
	 *
	 * @return greenCycle 이 존재하면 true
	 */
	public boolean isPredictedGreenCycle() {
		return Objects.nonNull(greenCycle);
	}

	/**
	 * 예측이 끝났는지 여부를 반환합니다.
	 *
	 * @return 예측이 끝났으면 true, 아니면 false
	 */
	public boolean isComplete() {
		return this.isPredictedRedCycle() && this.isPredictedGreenCycle();
	}

	/**
	 * PredictDataPredictor 객체를 통해 예측을 수행합니다.
	 *
	 * @param data 예측하고자 하는 신호등의 최근 데이터
	 * @return PredictDataPredictor 객체를 반환합니다.
	 */
	public PredictDatumPredictor predict(List<TrafficDetailEntity> data) {
		return new PredictDatumPredictor(this, data);
	}
}
