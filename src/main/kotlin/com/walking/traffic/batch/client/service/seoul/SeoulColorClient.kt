package com.walking.traffic.batch.client.service.seoul

import com.walking.traffic.batch.client.property.seoul.SeoulColorRequest
import com.walking.traffic.batch.client.service.seoul.dto.SeoulTrafficColorObject
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class SeoulColorClient(
    private val restTemplate: RestTemplate,
    private val colorRequestProperty: SeoulColorRequest,
) {
    fun execute(): List<SeoulTrafficColorObject> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters["apiKey"] = colorRequestProperty.apiKey
        return restTemplate.getForObject(
            UriComponentsBuilder.fromUriString(colorRequestProperty.getUrl().toString())
                .queryParams(parameters)
                .build()
                .toUri(),
            Array<SeoulTrafficColorObject>::class.java
        )!!.toList()
    }
}