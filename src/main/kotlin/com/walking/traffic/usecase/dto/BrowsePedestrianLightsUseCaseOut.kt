package com.walking.traffic.usecase.dto

import com.walking.traffic.usecase.model.PedestrianTrafficLight

data class BrowsePedestrianLightsUseCaseOut(
    val pedestrianTrafficLights: List<PedestrianTrafficLight>,
)