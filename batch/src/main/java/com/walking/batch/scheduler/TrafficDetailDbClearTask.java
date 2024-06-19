package com.walking.batch.scheduler;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** TrafficDetail 테이블의 데이터를 초기화 합니다. */
@Component
@Slf4j
@RequiredArgsConstructor
public class TrafficDetailDbClearTask {

	private final JdbcTemplate jdbcTemplate;

	@Scheduled(cron = "0 0 3 * * ?") // 매일 03시에 실행
	@Transactional
	public void clearDatabase() {
		jdbcTemplate.execute("TRUNCATE TABLE api.traffic_detail");
		log.debug("Database cleared! [" + LocalDateTime.now() + "]");
	}
}
