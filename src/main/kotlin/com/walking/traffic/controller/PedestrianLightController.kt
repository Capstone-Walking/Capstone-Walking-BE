package com.walking.traffic.controller

import com.walking.traffic.usecase.BrowsePedestrianLightsUseCase
import com.walking.traffic.usecase.BrowseUpdatedLast10MinutesPedestrianLightsUseCase
import com.walking.traffic.usecase.ReadPedestrianLightUseCase
import com.walking.traffic.usecase.dto.BrowsePedestrianLightsUseCaseIn
import com.walking.traffic.usecase.dto.ReadPedestrianLightsUseCaseIn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PedestrianLightController(
    private val browsePedestrianLightsUseCase: BrowsePedestrianLightsUseCase,
    private val browseUpdatedLast10MinutesPedestrianLightsUseCase: BrowseUpdatedLast10MinutesPedestrianLightsUseCase,
    private val readPedestrianLightUseCase: ReadPedestrianLightUseCase,
) {

    @GetMapping("?blLng={blLng}&blLat={blLat}&trLng={trLng}&trLat={trLat}")
    fun browsePedestrianLights(
        @RequestParam("blLng") blLng: Double,
        @RequestParam("blLat") blLat: Double,
        @RequestParam("trLng") trLng: Double,
        @RequestParam("trLat") trLat: Double,
    ) = browsePedestrianLightsUseCase.execute(
        BrowsePedestrianLightsUseCaseIn(
            blLng = blLng,
            blLat = blLat,
            trLng = trLng,
            trLat = trLat
        )
    ).let {
        ResponseEntity.ok(it)
    }

    @GetMapping("/latest")
    fun browseUpdatedLast10MinutesPedestrianTrafficLights() = browseUpdatedLast10MinutesPedestrianLightsUseCase.execute().let {
        ResponseEntity.ok(it)
    }

    @GetMapping("/one/{id}")
    fun readPedestrianTrafficLight(@PathVariable("id") id: Long) =
        readPedestrianLightUseCase.execute(
            ReadPedestrianLightsUseCaseIn(id)
        ).let {
            ResponseEntity.ok(it)
        }
}