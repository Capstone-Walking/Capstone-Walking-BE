package com.walking.batch.job;

import com.walking.batch.chunk.dto.TrafficDetailDto;
import com.walking.batch.chunk.reader.TrafficDetailItemReader;
import com.walking.batch.chunk.writer.TrafficDetailItemWriter;
import com.walking.batch.config.ApiDataSourceConfig;
import com.walking.batch.service.TrafficColorApiServiceV2;
import com.walking.batch.service.TrafficTimeLeftApiServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TrafficInfoJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Qualifier(ApiDataSourceConfig.TXM_NAME)
	private final PlatformTransactionManager txm;

	private final JdbcTemplate jdbcTemplate;

	private final TrafficColorApiServiceV2 trafficColorApiService;
	private final TrafficTimeLeftApiServiceV2 trafficTimeLeftApiService;

	@Value("${walking.batch.chunk.size}")
	private int chunkSize;

	@Bean
	public Job trafficDetailJob() {
		return jobBuilderFactory
				.get("trafficDetailJob")
				.incrementer(new RunIdIncrementer())
				.start(trafficDetailStep())
				.build();
	}

	@Bean
	public Step trafficDetailStep() {
		return stepBuilderFactory
				.get("trafficDetailStep")
				.<TrafficDetailDto, TrafficDetailDto>chunk(chunkSize)
				.reader(trafficDetailItemReader())
				.writer(trafficDetailItemWriter())
				.build();
	}

	@Bean
	public ItemReader<TrafficDetailDto> trafficDetailItemReader() {
		return new TrafficDetailItemReader(trafficColorApiService, trafficTimeLeftApiService);
	}

	@Bean
	public ItemWriter<TrafficDetailDto> trafficDetailItemWriter() {
		return new TrafficDetailItemWriter(jdbcTemplate, txm);
	}
}
