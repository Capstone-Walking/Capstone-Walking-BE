package com.walking.traffic.usecase

import com.walking.traffic.data.entity.ApiSource
import com.walking.traffic.data.repository.PedestrianLightRepository
import com.walking.traffic.usecase.dto.BrowsePedestrianLightsUseCaseOut
import com.walking.traffic.usecase.model.PredictStatus
import com.walking.traffic.usecase.service.SeoulLeftTimePedestrianLightService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BrowseUpdatedLast10MinutesPedestrianLightsUseCase(
    private val pedestrianLightRepository: PedestrianLightRepository,
    private val seoulLeftTimePedestrianLightService: SeoulLeftTimePedestrianLightService,
) {

    @Transactional
    fun execute(): BrowsePedestrianLightsUseCaseOut {
        val trafficInBounds = pedestrianLightRepository.findAll()

        val sourceMappedTraffic =
            trafficInBounds.groupBy { it.source }

        val pedestrianTrafficLights =
            seoulLeftTimePedestrianLightService.execute(
                sourceMappedTraffic[ApiSource.SEOUL_CAR]?.toList() ?: emptyList()
            )

        return pedestrianTrafficLights.asSequence().filter {
            it.leftTime.updateAt.isAfter(LocalDateTime.now().minusMinutes(10))
        }.filter {
            it.redCycle.status == PredictStatus.SUCCESS
        }.filter {
            it.greenCycle.status == PredictStatus.SUCCESS
        }.toList()
            .let {
                BrowsePedestrianLightsUseCaseOut(it)
            }
    }
}