package com.walking.member.api.client.token

import SocialIdTokenClient
import com.walking.member.api.client.exception.SocialClientException
import com.walking.member.api.client.token.dto.SocialIdToken
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.springframework.retry.RecoveryCallback
import org.springframework.retry.RetryCallback
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component

@Slf4j
@Component
@RequiredArgsConstructor
class RetryAbleKaKaoTokenClient(
    private val retryTemplate: RetryTemplate,
    private val kaKaoIdTokenClient: KaKaoIdTokenClient
) : SocialIdTokenClient {
    val log: Logger = org.slf4j.LoggerFactory.getLogger(RetryAbleKaKaoTokenClient::class.java)

    override fun execute(code: String): SocialIdToken {
        return retryTemplate.execute(
            RetryCallback<SocialIdToken, SocialClientException> { retryContext ->
                log.error("something wrong at KaKao api")
                log.error(
                    "KaKao get token retry count: {}",
                    retryContext.retryCount
                )
                kaKaoIdTokenClient.execute(code)
            },
            // todo 실패 기록
            RecoveryCallback<SocialIdToken> {
                log.error("fail get token KaKao api")
                null
            }
        )
    }
}