package com.walking.member.api.client.config

import com.walking.member.api.client.config.listener.ClientListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
@EnableRetry
class RetryTemplateConfig(
    private val clientListener: ClientListener
) {
    @Bean
    fun retryTemplate(
        @Value("\${client.retry.maxAttempts}") maxAttempts: Int,
        @Value("\${client.retry.backOffPeriod}") backOffPeriod: Int
    ): RetryTemplate {
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = backOffPeriod.toLong()

        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = maxAttempts

        val retryTemplate = RetryTemplate()
        retryTemplate.setBackOffPolicy(backOffPolicy)
        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.registerListener(clientListener)
        return retryTemplate
    }
}