package com.walking.batch.scheduler;

import static com.walking.batch.config.BatchDataSourceConfig.TRANSACTION_MANAGER_NAME;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** TrafficDetail 테이블의 데이터를 초기화 합니다. */
@Component
@Slf4j
@RequiredArgsConstructor
public class TrafficDetailDbClearTask {

	private final JdbcTemplate jdbcTemplate;

	@Scheduled(cron = "0 0 3 * * ?") // 매일 03시에 실행
	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME)
	public void clearDatabase() {
		jdbcTemplate.execute("TRUNCATE TABLE api.traffic_detail");
		log.debug("Database cleared! [" + LocalDateTime.now() + "]");
	}
}
