package com.walking.api.service;

import com.walking.api.repository.traffic.TrafficDetailRepository;
import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.api.service.dto.PredictedData;
import com.walking.api.service.dto.request.CyclePredictionRequestDto;
import com.walking.api.util.OffsetDateTimeCalculator;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 데이터베이스에 기록된 잔여시간 및 상태 정보를 바탕으로 신호 사이클을 예측합니다. */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficCyclePredictServiceImpl {

	private final TrafficRepository trafficRepository;
	private final TrafficDetailRepository trafficDetailRepository;

	@Value("${walking.batch.schedular.interval}")
	private int schedularInterval;

	@Value("${walking.predict.maximumSearchCount}")
	private int MAXIMUM_SEARCH_COUNT;

	/**
	 * 파라미터로 전달 받은 신호등 리스트와, 데이터를 가져오는 간격을 가지고 예측을 수행합니다.
	 *
	 * @param dto 예측 파라미터
	 * @return 예측 결과
	 */
	@Transactional(readOnly = true)
	public Map<TrafficEntity, PredictedData> execute(CyclePredictionRequestDto dto) {
		List<Long> ids = dto.getTrafficIds();
		List<TrafficEntity> traffics = trafficRepository.findByIds(ids);

		// 예측 정보를 담고 반환될 변수(리턴값)
		Map<TrafficEntity, PredictedData> result = new HashMap<>();
		for (TrafficEntity traffic : traffics) {
			result.put(traffic, new PredictedData(traffic));
		}

		return doPredict(dto, result);
	}

	/**
	 * 최신 순으로 interval 개 만큼 씩 데이터를 가지고 와서 계산을 수행 예를 들어, interval이 5인 경우 한 번 예측을 시도할 때마다 5개의 데이터를 가져와
	 * 예측을 수행합니다.
	 *
	 * @param result key가 신호등, value가 예측 데이터인 Map
	 */
	private Map<TrafficEntity, PredictedData> doPredict(
			CyclePredictionRequestDto dto, Map<TrafficEntity, PredictedData> result) {
		int searchCount = 0;
		int start = 0;
		int end = start + dto.getDataInterval();
		int dataInterval = dto.getDataInterval();
		List<TrafficEntity> unpredictedList = getUnpredictedList(result); // 예측이 끝나지 않은 신호등 리스트

		while (!unpredictedList.isEmpty()) {

			if (isNotExceedSearchCount(searchCount++)) {
				log.debug("수행횟수 " + MAXIMUM_SEARCH_COUNT + "를 초과하여 예측을 중단합니다.");
				break;
			}

			List<TrafficDetailEntity> recentlyData =
					trafficDetailRepository.findRecentlyData(unpredictedList, start, end);

			Map<TrafficEntity, List<TrafficDetailEntity>> separatedData = separateByTraffic(recentlyData);
			logging(separatedData);

			// 여기서 예측이 불가능한 신호등인지 구분되고 루프가 종료됩니다.
			if (separatedData.isEmpty()) {
				break;
			}

			for (TrafficEntity traffic : separatedData.keySet()) {
				predict(separatedData.get(traffic), result.get(traffic));
			}

			unpredictedList = getUnpredictedList(result);
			start = end;
			end += dataInterval;
		}

		return result;
	}

	/**
	 * 최대 예측 수행 범위에 벗어나지 않았는지 검증합니다.
	 *
	 * @param searchCount 현재까지 수행한 예측 횟수
	 * @return 예측 횟수를 초과하지 않으면 true, 그렇지 않으면 false
	 */
	private boolean isNotExceedSearchCount(int searchCount) {
		return searchCount >= MAXIMUM_SEARCH_COUNT;
	}

	/**
	 * 가져온 신호등 정보를 신호등을 기준으로 데이터를 분리합니다.
	 *
	 * @param recentlyData 데이터
	 * @return 신호등을 key 로 갖는 Map
	 */
	private Map<TrafficEntity, List<TrafficDetailEntity>> separateByTraffic(
			List<TrafficDetailEntity> recentlyData) {
		Map<TrafficEntity, List<TrafficDetailEntity>> separatedData = new HashMap<>();

		for (TrafficDetailEntity recentlyDatum : recentlyData) {
			List<TrafficDetailEntity> group =
					separatedData.computeIfAbsent(recentlyDatum.getTraffic(), data -> new ArrayList<>());

			group.add(recentlyDatum);
		}

		return separatedData;
	}

	/**
	 * 예측을 수행한 결과를 읽어보고 예측이 아직 끝나지 않은 신호등 리스트를 반환합니다.
	 *
	 * @param result 예측을 수행한 결과
	 * @return 신호등 리스트
	 */
	private List<TrafficEntity> getUnpredictedList(Map<TrafficEntity, PredictedData> result) {
		List<TrafficEntity> unpredictedList = new ArrayList<>();
		for (TrafficEntity traffic : result.keySet()) {
			if (!result.get(traffic).isPredictCycleSuccessful()) {
				unpredictedList.add(traffic);
			}
		}

		return unpredictedList;
	}

	/**
	 * 최근 데이터와 예측하고 있는 정보를 가지고 신호등 사이클을 계산합니다.
	 *
	 * @param data 예측하고자 하는 신호등의 최근 데이터
	 * @param predictedData 예측된 정보
	 * @return 인자로 전달 받은 predictData 에 예측 가능한 값을 채워 반환합니다.
	 */
	// R -> G, G -> R 따로 찾지말고 한 번 순회할 때 모두 찾아내면 좋겠다
	private PredictedData predict(List<TrafficDetailEntity> data, PredictedData predictedData) {
		if (!predictedData.isPredictedGreenCycle()) {
			predictedData.updateGreenCycle(predictGreenCycle(data));
		}
		if (!predictedData.isPredictedRedCycle()) {
			predictedData.updateRedCycle(predictRedCycle(data));
		}

		return predictedData;
	}

	/**
	 * 신호등의 빨간불에 대해서 사이클을 계산합니다.
	 *
	 * @param data 계산하고자 하는 신호등의 데이터 리스트
	 * @return 빨간불의 사이클
	 */
	private Optional<Float> predictRedCycle(List<TrafficDetailEntity> data) {
		Optional<Float> redCycle = Optional.empty();

		Iterator<TrafficDetailEntity> iterator = data.iterator();
		TrafficDetailEntity afterData = iterator.next();

		while (iterator.hasNext()) {
			TrafficDetailEntity before = iterator.next();
			// G -> R 인 패턴을 찾는다.
			if (isGreenToRedPattern(before, afterData) && checkNoMissingData(afterData, before)) {
				// 시간을 계산한다.
				redCycle = calculateCycle(afterData, before);
				log.debug(
						"신호등 ["
								+ afterData.getId()
								+ "]의 패턴: "
								+ before.getColor()
								+ " -> "
								+ afterData.getColor()
								+ " 을 찾았습니다.");
				break;
			}
			afterData = before;
		}
		return redCycle;
	}

	/**
	 * 연속된 두 traffic_detail 레코드 사이에 누락된 데이터가 존재하는지 검증합니다.
	 *
	 * @param afterData 이후 데이터
	 * @param before 이전 데이터
	 * @return 누락된 데이터가 없으면 True, 있으면 False
	 */
	private boolean checkNoMissingData(TrafficDetailEntity afterData, TrafficDetailEntity before) {
		int bias = 10;
		float differenceInSeconds =
				OffsetDateTimeCalculator.getDifferenceInSeconds(
						before.getTimeLeftRegDt(), afterData.getTimeLeftRegDt());

		return differenceInSeconds > 0 && differenceInSeconds < schedularInterval + bias;
	}

	private boolean isGreenToRedPattern(TrafficDetailEntity before, TrafficDetailEntity afterData) {
		return before.getColor().isGreen() && afterData.getColor().isRed();
	}

	/**
	 * 신호등의 초록불에 대해서 사이클을 계산합니다.
	 *
	 * @param data 계산하고자 하는 신호등의 데이터 리스트
	 * @return 초록불의 사이클
	 */
	private Optional<Float> predictGreenCycle(List<TrafficDetailEntity> data) {
		Optional<Float> greenCycle = Optional.empty();

		Iterator<TrafficDetailEntity> iterator = data.iterator();
		TrafficDetailEntity afterData = iterator.next();

		while (iterator.hasNext()) {
			TrafficDetailEntity before = iterator.next();
			// R -> G 인 패턴을 찾는다.
			if (isRedToGreenPattern(before, afterData) && checkNoMissingData(afterData, before)) {
				// 시간을 계산한다.
				greenCycle = calculateCycle(afterData, before);
				log.debug(
						"신호등 ["
								+ afterData.getId()
								+ "]의 패턴: "
								+ before.getColor()
								+ " -> "
								+ afterData.getColor()
								+ " 을 찾았습니다.");
				break;
			}
			afterData = before;
		}
		return greenCycle;
	}

	private boolean isRedToGreenPattern(TrafficDetailEntity before, TrafficDetailEntity afterData) {
		return before.getColor().isRed() && afterData.getColor().isGreen();
	}

	private Optional<Float> calculateCycle(
			TrafficDetailEntity afterData, TrafficDetailEntity before) {
		return Optional.of(afterData.getTimeLeft() + schedularInterval - before.getTimeLeft());
	}

	/**
	 * 가져온 데이터가 무엇인지 로그로 출력합니다.
	 *
	 * @param separatedData 신호등 단위로 구분된 데이터 리스트
	 */
	private void logging(Map<TrafficEntity, List<TrafficDetailEntity>> separatedData) {
		log.debug(
				"==================== 총 "
						+ separatedData.size()
						+ "개의 신호등에 대하여 가져온 데이터 ====================");
		for (TrafficEntity traffic : separatedData.keySet()) {
			log.debug("key == " + traffic.getId());
			List<TrafficDetailEntity> data = separatedData.get(traffic);
			for (TrafficDetailEntity datum : data) {
				log.debug(datum.toString());
			}
		}
		log.debug("============================== 가져온 데이터 끝 ==============================");
	}
}
