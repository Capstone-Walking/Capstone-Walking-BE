package com.walking.traffic.usecase.dto

data class BrowsePedestrianLightsUseCaseIn(
    val blLng: Double,
    val blLat: Double,
    val trLng: Double,
    val trLat: Double,
)