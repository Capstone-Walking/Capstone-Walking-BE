package com.walking.api.service;

import com.walking.api.ApiApp;
import com.walking.api.domain.traffic.service.TrafficPredictService;
import com.walking.api.domain.traffic.service.dto.TrafficPredictServiceRequest;
import com.walking.api.domain.traffic.service.model.PredictedData;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ApiApp.class)
@Slf4j
class TrafficPredictServiceTest {

	@Autowired TrafficPredictService integrationPredictService;

	@ParameterizedTest
	@MethodSource("getTrafficIds")
	void example(List<Long> trafficIds) {
		Map<Long, PredictedData> predictedDataMap =
				integrationPredictService
						.execute(TrafficPredictServiceRequest.builder().trafficIds(trafficIds).build())
						.getPredictedData();

		for (Long trafficId : predictedDataMap.keySet()) {
			log.debug(trafficId + "의 결과는 " + predictedDataMap.get(trafficId));
		}
	}

	static Stream<Arguments> getTrafficIds() {
		List<Long> trafficIds01 = Arrays.asList(3L, 39L, 4106L, 15L, 16L, 4121L, 43L, 50L, 51L, 52L);
		List<Long> trafficIds02 =
				Arrays.asList(139L, 4240L, 4267L, 175L, 176L, 177L, 180L, 182L, 183L, 4279L);
		List<Long> invalidIds = Arrays.asList(1L, 2L, 4L, 9999L);

		return Stream.of(
				Arguments.arguments(trafficIds01),
				Arguments.arguments(trafficIds02),
				Arguments.arguments(invalidIds));
	}
}
