package com.walking.batch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.batch.config.BatchConfig;
import com.walking.batch.config.BatchDataSourceConfig;
import com.walking.batch.config.BatchFactoryConfig;
import com.walking.batch.config.BatchJobConfig;
import com.walking.batch.config.BatchLaunchConfig;
import com.walking.batch.config.BatchPropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles(profiles = {"test"})
@DataJpaTest(
		excludeAutoConfiguration = {
			DataSourceAutoConfiguration.class,
			DataSourceTransactionManagerAutoConfiguration.class,
			HibernateJpaAutoConfiguration.class,
		})
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(
		classes = {
			BatchConfig.class,
			BatchDataSourceConfig.class,
			BatchFactoryConfig.class,
			BatchJobConfig.class,
			BatchLaunchConfig.class,
			BatchLaunchConfig.class,
			BatchPropertyConfig.class,
			ObjectMapper.class,
			TrafficColorApiService.class
		})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
abstract class RepositoryTest {}
