package com.walking.api.domain.traffic.service.predictor;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import com.walking.api.domain.traffic.service.model.RecentTrafficDetails;
import com.walking.api.repository.dao.traffic.TrafficDetailRepository;
import com.walking.api.repository.dao.traffic.TrafficRepository;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 데이터베이스에 기록된 잔여시간 및 상태 정보를 바탕으로 신호 사이클을 예측합니다. */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficCyclePredictor {
	private static final int MAXIMUM_SEARCH_COUNT = 5;

	@Value("${walking.predict.dataInterval}")
	private int predictInterval;

	@Value("${walking.batch.schedular.interval:70}")
	private int interval;

	private final TrafficRepository trafficRepository;
	private final TrafficDetailRepository trafficDetailRepository;

	@Transactional(value = TRANSACTION_MANAGER_NAME)
	public Map<Long, PredictedTraffic> execute(List<Long> trafficIds) {
		Map<Long, PredictedTraffic> trafficPredictTargets =
				trafficRepository.findAllInIds(trafficIds).stream()
						.collect(Collectors.toMap(TrafficEntity::getId, PredictedTraffic::new));
		return doPredict(trafficPredictTargets);
	}

	/**
	 * 최신 순으로 interval 개 만큼 씩 데이터를 가지고 와서 계산을 수행<br>
	 * 예를 들어, interval이 5인 경우 한 번 예측을 시도할 때마다 5개의 데이터를 가져와 예측을 수행합니다.
	 */
	private Map<Long, PredictedTraffic> doPredict(Map<Long, PredictedTraffic> trafficPredictTargets) {
		int tryCount = 0;
		int startRowNum = 0;
		int endRowNum = startRowNum + predictInterval;
		List<Long> unpredictedTrafficIds = updateUnpredictedTrafficKeys(trafficPredictTargets);

		while (!unpredictedTrafficIds.isEmpty()) {
			if (isExceedTryCount(tryCount++)) {
				log.error("Break the predict cycle loop because of exceeding search count.");
				break;
			}

			Map<Long, RecentTrafficDetails> mappedUnpredictedTrafficRecentData =
					mappedUnpredictedRecentTrafficDetailsByTrafficId(
							unpredictedTrafficIds, startRowNum, endRowNum);
			if (mappedUnpredictedTrafficRecentData.isEmpty()) {
				log.debug("Break the predict cycle loop because of no recent data.");
				break;
			}

			mappedUnpredictedTrafficRecentData.forEach(
					(id, recentTrafficDetails) -> {
						PredictedTraffic predictTarget = trafficPredictTargets.get(id);
						predictTarget.predictCycle(recentTrafficDetails);
					});

			unpredictedTrafficIds = updateUnpredictedTrafficKeys(trafficPredictTargets);
			startRowNum = endRowNum;
			endRowNum += predictInterval;
		}

		return trafficPredictTargets;
	}

	private boolean isExceedTryCount(int searchCount) {
		return searchCount >= MAXIMUM_SEARCH_COUNT;
	}

	private List<Long> updateUnpredictedTrafficKeys(Map<Long, PredictedTraffic> predictTargets) {
		return predictTargets.entrySet().stream()
				.filter(entry -> !entry.getValue().isPredictCycleSuccessful())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private Map<Long, RecentTrafficDetails> mappedUnpredictedRecentTrafficDetailsByTrafficId(
			List<Long> unpredictedTrafficKeys, int start, int end) {
		return trafficDetailRepository.findAllInIdsBetween(unpredictedTrafficKeys, start, end).stream()
				.collect(Collectors.groupingBy(entity -> entity.getTraffic().getId()))
				.entrySet()
				.stream()
				.collect(
						Collectors.toMap(
								Map.Entry::getKey, entry -> new RecentTrafficDetails(interval, entry.getValue())));
	}
}
