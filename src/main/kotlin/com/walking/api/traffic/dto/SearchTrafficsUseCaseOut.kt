package com.walking.api.traffic.dto

import com.walking.api.traffic.dto.detail.TrafficDetail

data class SearchTrafficsUseCaseOut(
    val traffics: List<TrafficDetail>
)