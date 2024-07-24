package com.walking.api.traffic.dto

data class SearchTrafficsUseCaseIn(
    val vblLng: Double,
    val vblLat: Double,
    val vtrLng: Double,
    val vtrLat: Double
)