package com.walking.api.member.api.client.config.listener

import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.listener.RetryListenerSupport
import org.springframework.stereotype.Component

@Component
class ClientListener : RetryListenerSupport() {
    private val log = org.slf4j.LoggerFactory.getLogger(ClientListener::class.java)

    override fun <T, E : Throwable?> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        log.debug("request to kakao api")
        return super.open(context, callback)
    }

    override fun <T, E : Throwable?> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        log.debug("success get response kakao api")
        super.close(context, callback, throwable)
    }

    override fun <T, E : Throwable?> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        log.error("fail get response kakao api : {}", throwable.message)
        super.onError(context, callback, throwable)
    }
}