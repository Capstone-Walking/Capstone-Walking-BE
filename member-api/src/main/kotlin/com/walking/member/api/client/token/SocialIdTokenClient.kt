import com.walking.member.api.client.token.dto.SocialIdToken

fun interface SocialIdTokenClient {
    fun execute(code: String): SocialIdToken
}