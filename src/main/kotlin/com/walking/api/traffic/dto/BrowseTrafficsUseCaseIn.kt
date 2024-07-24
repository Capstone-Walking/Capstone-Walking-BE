package com.walking.api.traffic.dto

data class BrowseTrafficsUseCaseIn(
    val trafficId: Long,
    val memberId: Long ? = -1L
)