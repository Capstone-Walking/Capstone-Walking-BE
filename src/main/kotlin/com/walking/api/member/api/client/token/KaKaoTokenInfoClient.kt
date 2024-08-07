
import com.walking.api.member.api.client.config.property.KaKaoApiProperties
import com.walking.api.member.api.client.exception.SocialClientException
import com.walking.api.member.api.client.token.SocialTokenClient
import com.walking.api.member.api.client.token.dto.KaKaoTokenData
import com.walking.api.member.api.client.token.dto.SocialIdToken
import com.walking.api.member.api.client.token.dto.SocialToken
import com.walking.api.member.api.client.util.addBearerHeader
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Slf4j
@Component
@RequiredArgsConstructor
class KaKaoTokenInfoClient(
    private val restTemplate: RestTemplate,
    private val properties: KaKaoApiProperties
) : SocialTokenClient {
    override fun execute(token: SocialIdToken): SocialToken {
        val accessToken: String = token.getToken()
        val headers = HttpHeaders()
        headers.addBearerHeader(accessToken)
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")

        val response: ResponseEntity<KaKaoTokenData> = restTemplate.exchange(
            properties.uriTokenInfo,
            HttpMethod.GET,
            HttpEntity(null, headers),
            KaKaoTokenData::class.java
        )

        val statusCode: HttpStatus = response.statusCode
        if (statusCode.is4xxClientError) {
            throw SocialClientException()
        }

        return response.body!!
    }
}