package com.walking.traffic.usecase.service

import com.walking.traffic.data.entity.PedestrianLightEntity
import com.walking.traffic.usecase.model.PedestrianTrafficLight

interface PedestrianLightService {
    fun execute(sources: List<PedestrianLightEntity>): List<PedestrianTrafficLight>
}