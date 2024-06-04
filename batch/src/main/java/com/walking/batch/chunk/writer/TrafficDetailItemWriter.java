package com.walking.batch.chunk.writer;

import com.walking.batch.chunk.dto.TrafficDetailDto;
import com.walking.batch.config.ApiDataSourceConfig;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
public class TrafficDetailItemWriter implements ItemWriter<TrafficDetailDto> {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final PlatformTransactionManager txm;

	public TrafficDetailItemWriter(
			JdbcTemplate jdbcTemplate,
			@Qualifier(ApiDataSourceConfig.TXM_NAME) PlatformTransactionManager txm) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		this.txm = txm;
	}

	@Override
	public void write(List<? extends TrafficDetailDto> items) throws Exception {
		log.debug("write 작업 중...");
		DefaultTransactionDefinition transactionDef = new DefaultTransactionDefinition();
		transactionDef.setTimeout(2);
		TransactionStatus status = txm.getTransaction(transactionDef);

		try {
			// items의 id를 가지고 traffic entity id를 조회
			List<Long> trafficIds =
					items.stream().map(item -> item.getItstId()).collect(Collectors.toList());
			SqlParameterSource parameters = new MapSqlParameterSource("trafficIds", trafficIds);

			Map<Long, Map<Long, String>> ids =
					jdbcTemplate.query(
							"SELECT t.id, t.detail ->> '$.id' as trafficId, t.detail ->> '$.direction' as direction  FROM traffic t where t.detail ->> '$.id' IN (:trafficIds)",
							parameters,
							new ResultSetExtractor<Map<Long, Map<Long, String>>>() {
								public Map<Long, Map<Long, String>> extractData(ResultSet rs)
										throws DataAccessException, SQLException {
									Map<Long, Map<Long, String>> map = new HashMap<>();
									while (rs.next()) {
										map.put(
												rs.getLong("id"),
												Map.of(rs.getLong("trafficId"), rs.getString("direction")));
									}
									return map;
								}
							});
			log.debug("bulk insert 를 위한 파라미터 준비 중...");

			List<SqlParameterSource> bulkInsertParams =
					ids.keySet().stream()
							.map(
									id -> {
										Optional<? extends TrafficDetailDto> optionalTrafficDetailDto =
												items.stream()
														.filter(
																item -> item.getItstId().equals(ids.get(id).keySet().toArray()[0]))
														.filter(
																item ->
																		item.getDirection().equals(ids.get(id).values().toArray()[0]))
														.findFirst();
										if (optionalTrafficDetailDto.isPresent()) {
											TrafficDetailDto trafficDetailDto = optionalTrafficDetailDto.get();
											MapSqlParameterSource paramSource = new MapSqlParameterSource();
											paramSource.addValue("trafficId", id);
											paramSource.addValue("direction", trafficDetailDto.getDirection());
											paramSource.addValue("color", trafficDetailDto.getColor());
											paramSource.addValue(
													"timeLeft",
													trafficDetailDto.getTimeLeft() / 10); // 1/10초 단위의 응답 데이터를 1초 단위로 변경
											paramSource.addValue("color_reg_dt", trafficDetailDto.getColorRegDt());
											paramSource.addValue("time_left_reg_dt", trafficDetailDto.getTimeLeftRegDt());
											return Optional.of(paramSource);
										} else {
											return Optional.<MapSqlParameterSource>empty();
										}
									})
							.filter(Optional::isPresent)
							.map(Optional::get)
							.collect(Collectors.toList());

			for (SqlParameterSource bulkInsertParam : bulkInsertParams) {
				log.debug(bulkInsertParam.toString());
			}
			log.debug("bulk insert 를 위한 파라미터 준비 완료.");

			jdbcTemplate.batchUpdate(
					"INSERT INTO traffic_detail (traffic_id, direction, color, time_left, color_reg_dt, time_left_reg_dt)"
							+ " VALUES (:trafficId, :direction, :color, :timeLeft, :color_reg_dt, :time_left_reg_dt)",
					bulkInsertParams.toArray(new SqlParameterSource[0]));

			txm.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			txm.rollback(status);
		}
	}
}
