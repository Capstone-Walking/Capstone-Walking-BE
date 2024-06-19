package com.walking.batch.service.v2;

import com.walking.batch.BatchMain;
import com.walking.batch.traffic.service.TrafficColorApiServiceV2;
import com.walking.batch.traffic.service.TrafficTimeLeftApiServiceV2;
import com.walking.batch.traffic.service.dto.TrafficColorVO;
import com.walking.batch.traffic.service.dto.TrafficTimeLeftVO;
import com.walking.data.entity.traffic.TrafficEntity;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest(classes = {BatchMain.class})
@Transactional
class TrafficColorApiServiceV2Test {

	@Autowired TrafficColorApiServiceV2 colorApiServiceV2;
	@Autowired TrafficTimeLeftApiServiceV2 timeLeftApiServiceV2;
	@Autowired EntityManager em;

	@Test
	void example() {
		Iterable<TrafficColorVO> colorResponseDtos = colorApiServiceV2.request();
		Iterable<TrafficTimeLeftVO> timeLeftResponseDtos = timeLeftApiServiceV2.request();
		TrafficEntity trafficEntity = em.find(TrafficEntity.class, 1L);
		System.out.println("이거다 " + trafficEntity);
	}
}
