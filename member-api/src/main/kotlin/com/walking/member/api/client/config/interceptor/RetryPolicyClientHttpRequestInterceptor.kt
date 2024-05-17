package com.walking.member.api.client.config.interceptor

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class RetryPolicyClientHttpRequestInterceptor(
    @Value("\${client.max-attempts:3}") private val maxAttempts: Int
) : ClientHttpRequestInterceptor {
    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(maxAttempts))

        return try {
            retryTemplate.execute<ClientHttpResponse, IOException> {
                execution.execute(
                    request,
                    body
                )
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}