package com.walking.member.api.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.List

@Configuration
@EnableCaching
class MemberApiCacheConfig {

    @Bean(value = [MemberApiConfig.BEAN_NAME_PREFIX + "CacheManager"])
    fun memberApiCacheManager(): CacheManager {
        SimpleCacheManager().apply {
            this.setCaches(
                List.of(
                    ConcurrentMapCache("member-profile-url")
                )
            )
            return this
        }
    }
}