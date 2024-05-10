package com.walking.api.predictor.dto;

import com.walking.data.entity.traffic.TrafficEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class PredictData {

	private List<PredictDatum> predictData;

	@Builder
	public PredictData(Map<TrafficEntity, PredictDatum> predictData) {
		this.predictData = new ArrayList<>(predictData.values());
	}

	/**
	 * 예측을 수행한 결과를 읽어보고 예측이 아직 끝나지 않은 신호등 리스트를 반환합니다.
	 *
	 * @param prePredictData 예측을 수행한 결과
	 * @return 신호등 리스트
	 */
	public PredictData refresh(Map<TrafficEntity, PredictDatum> prePredictData) {
		return PredictData.builder().predictData(prePredictData).build();
	}

	public boolean isEmpty() {
		return predictData.isEmpty();
	}

	public List<TrafficEntity> getTraffics() {
		return predictData.stream().map(PredictDatum::getTraffic).collect(Collectors.toList());
	}
}
