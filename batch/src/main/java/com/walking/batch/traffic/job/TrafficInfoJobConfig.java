package com.walking.batch.traffic.job;

import static com.walking.batch.config.BatchDataSourceConfig.TRANSACTION_MANAGER_NAME;

import com.walking.batch.traffic.service.TrafficColorApiServiceV2;
import com.walking.batch.traffic.service.TrafficTimeLeftApiServiceV2;
import com.walking.batch.traffic.step.dto.TrafficDetailDto;
import com.walking.batch.traffic.step.reader.TrafficDetailItemReader;
import com.walking.batch.traffic.step.writer.TrafficDetailItemWriter;
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
@Slf4j
public class TrafficInfoJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final PlatformTransactionManager txm;

	private final JdbcTemplate jdbcTemplate;

	private final TrafficColorApiServiceV2 trafficColorApiService;
	private final TrafficTimeLeftApiServiceV2 trafficTimeLeftApiService;

	private final int chunkSize;

	public TrafficInfoJobConfig(
			@Qualifier(TRANSACTION_MANAGER_NAME) PlatformTransactionManager txm,
			@Value("${spring.batch.chunk.size}") int chunkSize,
			JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory,
			JdbcTemplate jdbcTemplate,
			TrafficColorApiServiceV2 trafficColorApiService,
			TrafficTimeLeftApiServiceV2 trafficTimeLeftApiService) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.txm = txm;
		this.jdbcTemplate = jdbcTemplate;
		this.trafficColorApiService = trafficColorApiService;
		this.trafficTimeLeftApiService = trafficTimeLeftApiService;
		this.chunkSize = chunkSize;
	}

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
