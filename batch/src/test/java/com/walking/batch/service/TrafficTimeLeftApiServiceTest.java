package com.walking.batch.service;

import com.walking.batch.BatchMain;
import com.walking.batch.service.dto.TrafficTimeLeftResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ContextConfiguration(classes = {BatchMain.class})
class TrafficTimeLeftApiServiceTest {

	@Autowired private TrafficTimeLeftApiServiceV2 trafficTimeLeftApiService;

	@Test
	void example() {
		Iterable<TrafficTimeLeftResponseDto> result = trafficTimeLeftApiService.request();
	}
}
