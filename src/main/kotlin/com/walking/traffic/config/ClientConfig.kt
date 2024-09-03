package com.walking.traffic.config

import org.apache.hc.client5.http.impl.DefaultRedirectStrategy
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        val factory = HttpComponentsClientHttpRequestFactory()
        HttpClientBuilder.create()
            .setRedirectStrategy(DefaultRedirectStrategy())
            .build().let {
                factory.setHttpClient(it)
            }

        return RestTemplate().apply {
            requestFactory = factory
        }
    }
}