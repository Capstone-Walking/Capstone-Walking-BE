package com.walking.member.api.client.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TokenPropertiesMapper(
    private val objectMapper: ObjectMapper
) {
    val log: Logger = LoggerFactory.getLogger(TokenPropertiesMapper::class.java)
    fun <T> read(info: String?, clazz: Class<T>): T {
        var properties: T? = null
        try {
            properties = objectMapper.readValue(info, clazz)
        } catch (e: JsonProcessingException) {
            log.error("error read token properties", e)
        }
        return properties!!
    }
}