package com.walking.member.api.config

import com.walking.api.repository.config.ApiRepositoryConfig
import com.walking.image.config.ImageStoreConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    value = [ApiRepositoryConfig::class, ImageStoreConfig::class]
)
@Configuration
@ComponentScan(basePackages = [MemberApiConfig.BASE_PACKAGE])
class MemberApiConfig {
    companion object {
        const val BASE_PACKAGE = "com.walking.member.api"
        const val SERVICE_NAME = "walking"
        const val MODULE_NAME = "member-api"
        const val BEAN_NAME_PREFIX = "memberApi"
        const val PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME
    }
}