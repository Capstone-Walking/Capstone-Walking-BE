package com.walking.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.walking.api.ApiApp;
import com.walking.api.service.dto.PredictedData;
import com.walking.api.service.dto.request.IntegrationPredictRequestDto;
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
class TrafficIntegrationPredictServiceTest {

	@Autowired TrafficIntegrationPredictService integrationPredictService;

	@ParameterizedTest
	@MethodSource("getTrafficIds")
	void example(List<Long> trafficIds) {
		Map<Long, PredictedData> predictedDataMap =
				integrationPredictService
						.execute(IntegrationPredictRequestDto.builder().trafficIds(trafficIds).build())
						.getPredictedDataMap();

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
