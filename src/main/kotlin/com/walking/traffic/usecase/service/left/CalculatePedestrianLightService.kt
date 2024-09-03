package com.walking.traffic.usecase.service.left

import com.walking.traffic.data.entity.PedestrianLightEntity
import com.walking.traffic.usecase.model.PedestrianTrafficLight

interface CalculatePedestrianLightService<T> {
    fun execute(sources: List<PedestrianLightEntity>, data: T): List<PedestrianTrafficLight>
}