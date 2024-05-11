package com.walking.api.service;

import com.walking.api.predictor.PredictDatumPredictor;
import com.walking.api.predictor.dto.PredictData;
import com.walking.api.predictor.dto.PredictDatum;
import com.walking.api.repository.traffic.TrafficDetailRepository;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 데이터베이스에 기록된 잔여시간 및 상태 정보를 바탕으로 신호 사이클을 예측합니다. */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficCyclePredictService {

	private final TrafficDetailRepository trafficDetailRepository;

	/**
	 * 최신 순으로 interval 개 만큼 씩 데이터를 가지고 와서 계산을 수행합니다. 예를 들어, interval이 5인 경우 한 번 예측을 시도할 때마다 각 신호등에 대한
	 * 5개의 데이터를 가져와 예측을 수행합니다.
	 *
	 * @param traffics 신호 주기를 예측하고자 하는 신호등 리스트
	 * @param interval 예측을 위해 가져올 데이터의 크기
	 */
	@Transactional(readOnly = true)
	public Map<TrafficEntity, PredictDatum> execute(List<TrafficEntity> traffics, int interval) {
		int start = 0;
		int end = start + interval;

		// 예측 정보를 담고 반환될 변수(리턴값)
		Map<TrafficEntity, PredictDatum> originData =
				traffics.stream()
						.map(PredictDatum::new)
						.collect(Collectors.toMap(PredictDatum::getTraffic, predictDatum -> predictDatum));

		// 예측이 끝나지 않은 신호등 리스트
		PredictData predictData = PredictData.builder().predictData(originData).build();

		while (!predictData.isEmpty()) {
			List<TrafficDetailEntity> recentlyData =
					trafficDetailRepository.getRecentlyData(predictData.getTraffics(), start, end);

			Map<TrafficEntity, List<TrafficDetailEntity>> separatedData =
					recentlyData.stream().collect(Collectors.groupingBy(TrafficDetailEntity::getTraffic));
			logging(separatedData);

			// 여기서 예측이 불가능한 신호등인지 구분되고 루프가 종료됩니다.
			if (separatedData.isEmpty()) {
				break;
			}

			separatedData.forEach(
					(traffic, data) -> {
						PredictDatum predictDatum = originData.get(traffic);
						predictDatum
								.predict(data)
								.ifNotPredictedApplyAndLoad(
										PredictDatum::isPredictedRedCycle,
										PredictDatumPredictor::predictRedCycle,
										PredictDatum::loadRedCycle)
								.ifNotPredictedApplyAndLoad(
										PredictDatum::isPredictedGreenCycle,
										PredictDatumPredictor::predictGreenCycle,
										PredictDatum::loadGreenCycle);
					});

			predictData.refresh(originData);
			start = end;
			end += interval;
		}

		return originData;
	}

	/**
	 * 가져온 데이터가 무엇인지 로그로 출력합니다.
	 *
	 * @param separatedData 신호등 단위로 구분된 데이터 리스트
	 */
	private void logging(Map<TrafficEntity, List<TrafficDetailEntity>> separatedData) {
		log.debug("==================== 가져온 데이터 ====================");
		separatedData
				.keySet()
				.forEach(
						traffic -> {
							log.debug("key == " + traffic.getId());
							List<TrafficDetailEntity> data = separatedData.get(traffic);
							for (TrafficDetailEntity datum : data) {
								log.debug(datum.toString());
							}
						});
		log.debug("==================== 가져온 데이터 끝 ====================");
	}
}
