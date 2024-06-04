import com.walking.member.api.client.token.dto.SocialIdToken

fun interface SocialIdTokenClient {

    /**
     * Get social id token by auth code
     * @param code social auth code
     */
    fun execute(code: String): SocialIdToken
}