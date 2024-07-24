package com.walking.api.member.api.client.config.interceptor

import org.slf4j.Logger
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@Component
class LogClientHttpRequestInterceptor : ClientHttpRequestInterceptor {
    val log: Logger = org.slf4j.LoggerFactory.getLogger(com.walking.api.member.api.client.config.interceptor.LogClientHttpRequestInterceptor::class.java)

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val uuid = clientHttpRequestUUID()
        doLogRequest(uuid, request, body)
        val response = execution.execute(request, body)
        doLogResponse(uuid, response)
        return response
    }

    private fun clientHttpRequestUUID(): String {
        return (Math.random() * 1000000).toInt().toString()
    }

    private fun doLogRequest(sessionNumber: String, req: HttpRequest, body: ByteArray) {
        log.info(
            "[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
            sessionNumber,
            req.uri,
            req.method,
            req.headers,
            String(body, StandardCharsets.UTF_8)
        )
    }

    @Throws(IOException::class)
    private fun doLogResponse(sessionNumber: String, res: ClientHttpResponse) {
        val body = BufferedReader(InputStreamReader(res.body, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"))
        log.info(
            "[{}] Status: {}, Headers:{}, Body:{} ",
            sessionNumber,
            res.statusCode,
            res.headers,
            body
        )
    }
}