package com.walking.api.traffic.dto.detail

data class TrafficDetailInfo(
    val trafficId: Long,
    val apiSource: String,
    val direction: String
) {
    constructor(trafficId: Int, apiSource: String, direction: String) : this(trafficId.toLong(), apiSource, direction)
}