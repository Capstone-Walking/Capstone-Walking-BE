package com.walking.traffic.usecase

import com.walking.traffic.data.entity.ApiSource
import com.walking.traffic.data.repository.PedestrianLightRepository
import com.walking.traffic.service.GeometryService
import com.walking.traffic.usecase.dto.BrowsePedestrianLightsUseCaseIn
import com.walking.traffic.usecase.dto.BrowsePedestrianLightsUseCaseOut
import com.walking.traffic.usecase.model.PredictStatus
import com.walking.traffic.usecase.service.SeoulLeftTimePedestrianLightService
import org.locationtech.jts.geom.Coordinate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrowsePedestrianLightsUseCase(
    private val geometryService: GeometryService,
    private val pedestrianLightRepository: PedestrianLightRepository,
    private val seoulLeftTimePedestrianLightService: SeoulLeftTimePedestrianLightService,
) {

    @Transactional
    fun execute(useCaseIn: BrowsePedestrianLightsUseCaseIn): BrowsePedestrianLightsUseCaseOut {
        val bound = geometryService.createPolygon(
            arrayOf(
                Coordinate(useCaseIn.blLng, useCaseIn.blLat),
                Coordinate(useCaseIn.blLng, useCaseIn.trLat),
                Coordinate(useCaseIn.trLng, useCaseIn.trLat),
                Coordinate(useCaseIn.trLng, useCaseIn.blLat),
                Coordinate(useCaseIn.blLng, useCaseIn.blLat)
            )
        )

        val trafficInBounds = pedestrianLightRepository.findAllInBounds(bound)

        val sourceMappedTraffic =
            trafficInBounds.groupBy(keySelector = { it.source })

        val pedestrianTrafficLights =
            seoulLeftTimePedestrianLightService.execute(sourceMappedTraffic[ApiSource.SEOUL_CAR]!!)

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