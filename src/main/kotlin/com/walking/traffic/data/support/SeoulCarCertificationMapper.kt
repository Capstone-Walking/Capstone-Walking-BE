package com.walking.traffic.data.support

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

data class SeoulCarCertification(
    val itstId: String,
    val direction: String,
)

@Component
class SeoulCarCertificationMapper(
    private val objectMapper: ObjectMapper,
) {
    fun toJson(seoulCarCertification: SeoulCarCertification): String {
        return objectMapper.writeValueAsString(seoulCarCertification)
    }

    fun toSeoulCertification(json: String): SeoulCarCertification {
        return objectMapper.readValue(json, SeoulCarCertification::class.java)
    }
}