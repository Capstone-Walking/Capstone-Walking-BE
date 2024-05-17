package com.walking.image.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [ImageStoreConfig.BASE_PACKAGE])
class ImageStoreConfig {
    companion object {
        const val BASE_PACKAGE = "com.walking.image"
        const val SERVICE_NAME = "walking"
        const val MODULE_NAME = "image-store"
        const val BEAN_NAME_PREFIX = "imageStore"
        const val PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME
    }
}