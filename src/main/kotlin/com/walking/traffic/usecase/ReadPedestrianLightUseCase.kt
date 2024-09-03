package com.walking.traffic.usecase

import com.walking.traffic.data.repository.PedestrianLightRepository
import com.walking.traffic.usecase.dto.BrowsePedestrianLightsUseCaseOut
import com.walking.traffic.usecase.dto.ReadPedestrianLightsUseCaseIn
import com.walking.traffic.usecase.model.PredictStatus
import com.walking.traffic.usecase.service.SeoulLeftTimePedestrianLightService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ReadPedestrianLightUseCase(
    private val pedestrianLightRepository: PedestrianLightRepository,
    private val seoulLeftTimePedestrianLightService: SeoulLeftTimePedestrianLightService,
) {

    @Transactional
    fun execute(useCaseIn: ReadPedestrianLightsUseCaseIn): BrowsePedestrianLightsUseCaseOut {
        val traffic = pedestrianLightRepository.findById(useCaseIn.id).getOrNull() ?: return BrowsePedestrianLightsUseCaseOut(emptyList())

        val pedestrianTrafficLights =
            seoulLeftTimePedestrianLightService.execute(listOf(traffic))

        pedestrianTrafficLights.filter {
            it.greenCycle.status == PredictStatus.SUCCESS
        }.forEach {
            pedestrianLightRepository.updateGreenCycle(it.id, it.greenCycle.cycle, it.greenCycle.updateAt)
        }

        pedestrianTrafficLights.filter {
            it.redCycle.status == PredictStatus.SUCCESS
        }.forEach {
            pedestrianLightRepository.updateRedCycle(it.id, it.redCycle.cycle, it.redCycle.updateAt)
        }

        return BrowsePedestrianLightsUseCaseOut(pedestrianTrafficLights)
    }
}