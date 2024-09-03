package com.walking.traffic.batch.client.property.seoul

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL

@Component
class SeoulColorRequest(
    @Value("\${api.seoul.apiKey}") val apiKey: String,
    @Value("\${api.seoul.baseUrl}") private val baseUrl: String,
    @Value("\${api.seoul.colorPath}") private val trafficColorPath: String,
) {
    fun getUrl(): URL {
        return URL("$baseUrl$trafficColorPath")
    }
}