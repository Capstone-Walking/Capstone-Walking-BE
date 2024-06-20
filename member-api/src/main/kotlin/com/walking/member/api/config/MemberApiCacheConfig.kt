package com.walking.member.api.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class MemberApiCacheConfig {

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .initialCapacity(16)
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
    }

    @Bean(value = [MemberApiConfig.BEAN_NAME_PREFIX + "CacheManager"])
    fun memberApiCacheManager(): CacheManager {
        CaffeineCacheManager().let { cacheManager ->
            cacheManager.setCaffeine(caffeineConfig())
            cacheManager.cacheNames = listOf("member-profile")
            return cacheManager
        }
    }
}