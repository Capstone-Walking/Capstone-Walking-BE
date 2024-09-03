package com.walking.traffic.batch.client.service.seoul

import com.walking.traffic.batch.client.property.seoul.SeoulLeftTimeRequest
import com.walking.traffic.batch.client.service.seoul.dto.SeoulTrafficLeftTimeObject
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class SeoulLeftTimeClient(
    private val restTemplate: RestTemplate,
    private val leftTimeRequestProperty: SeoulLeftTimeRequest,
) {

    fun execute(): List<SeoulTrafficLeftTimeObject> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters["apiKey"] = leftTimeRequestProperty.apiKey
        return restTemplate.getForObject(
            UriComponentsBuilder.fromUriString(leftTimeRequestProperty.getUrl().toString())
                .queryParams(parameters)
                .build()
                .toUri(),
            Array<SeoulTrafficLeftTimeObject>::class.java
        )!!.toList()
    }
}