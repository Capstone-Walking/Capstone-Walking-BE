package com.walking.api.member.api.client.exception

class SocialIntegrationException(message: String = "소셜 연동 중 오류가 발생했습니다.") :
    RuntimeException(message)