package com.walking.api.member.api.client.config

import com.walking.api.member.api.client.config.interceptor.LogClientHttpRequestInterceptor
import com.walking.api.member.api.client.config.interceptor.RetryPolicyClientHttpRequestInterceptor
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
    private val retryPolicyClientHttpRequestInterceptor: RetryPolicyClientHttpRequestInterceptor,
    private val logClientHttpRequestInterceptor: com.walking.api.member.api.client.config.interceptor.LogClientHttpRequestInterceptor
) {
    @Bean
    fun restTemplate(
        @Value("\${client.timeout.connect}") connectTimeout: Int,
        @Value("\${client.timeout.read}") readTimeout: Int,
        @Value("\${client.pool.max-connect}") maxConnectPool: Int,
        @Value("\${client.pool.max-connect-per-route}") maxPerRouteConnectPool: Int
    ): RestTemplate {
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setReadTimeout(readTimeout)
        factory.setConnectTimeout(connectTimeout)
        val httpClient: HttpClient = HttpClientBuilder.create()
            .setMaxConnTotal(maxConnectPool)
            .setMaxConnPerRoute(maxPerRouteConnectPool)
            .build()
        factory.setHttpClient(httpClient)

        val restTemplate = RestTemplate(BufferingClientHttpRequestFactory(factory))
        var interceptors: MutableList<ClientHttpRequestInterceptor?> =
            restTemplate.getInterceptors()
        if (interceptors.isEmpty()) {
            interceptors = ArrayList()
        }
        interceptors.add(retryPolicyClientHttpRequestInterceptor)
        interceptors.add(logClientHttpRequestInterceptor)
        restTemplate.setInterceptors(interceptors)
        return restTemplate
    }
}