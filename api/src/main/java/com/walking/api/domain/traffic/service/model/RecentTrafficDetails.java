package com.walking.api.domain.traffic.service.model;

import com.walking.api.domain.util.OffsetDateTimeCalculator;
import com.walking.data.entity.traffic.TrafficDetailEntity;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RecentTrafficDetails {

	private final int interval;

	private final List<TrafficDetailEntity> trafficDetails;

	public Optional<Float> predictRedCycle() {
		Optional<Float> redCycle = Optional.empty();

		Iterator<TrafficDetailEntity> iterator = trafficDetails.iterator();
		TrafficDetailEntity afterData = iterator.next();
		while (iterator.hasNext()) {
			TrafficDetailEntity beforeData = iterator.next();
			if (isGreenToRedPattern(beforeData, afterData)
					&& checkMissingDataBetween(beforeData, afterData)) {
				redCycle = calculateCycle(beforeData, afterData);
				break;
			}
			afterData = beforeData;
		}
		return redCycle;
	}

	public Optional<Float> predictGreenCycle() {
		Optional<Float> greenCycle = Optional.empty();

		Iterator<TrafficDetailEntity> iterator = trafficDetails.iterator();
		TrafficDetailEntity afterData = iterator.next();
		while (iterator.hasNext()) {
			TrafficDetailEntity beforeData = iterator.next();
			if (isRedToGreenPattern(beforeData, afterData)
					&& checkMissingDataBetween(beforeData, afterData)) {
				greenCycle = calculateCycle(beforeData, afterData);
				break;
			}
			afterData = beforeData;
		}
		return greenCycle;
	}

	private boolean isGreenToRedPattern(TrafficDetailEntity before, TrafficDetailEntity afterData) {
		return before.getColor().isGreen() && afterData.getColor().isRed();
	}

	private boolean isRedToGreenPattern(TrafficDetailEntity before, TrafficDetailEntity afterData) {
		return before.getColor().isRed() && afterData.getColor().isGreen();
	}

	private boolean checkMissingDataBetween(
			TrafficDetailEntity before, TrafficDetailEntity afterData) {
		int bias = 10;
		float differenceInSeconds =
				OffsetDateTimeCalculator.getDifferenceInSeconds(
						before.getTimeLeftRegDt(), afterData.getTimeLeftRegDt());
		return differenceInSeconds > 0 && differenceInSeconds < interval + bias;
	}

	private Optional<Float> calculateCycle(
			TrafficDetailEntity before, TrafficDetailEntity afterData) {
		return Optional.of(afterData.getTimeLeft() + interval - before.getTimeLeft());
	}
}
