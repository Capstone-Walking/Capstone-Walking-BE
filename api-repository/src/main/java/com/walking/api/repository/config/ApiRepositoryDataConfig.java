package com.walking.api.repository.config;

import com.walking.data.DataConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataConfig.class})
public class ApiRepositoryDataConfig {}
