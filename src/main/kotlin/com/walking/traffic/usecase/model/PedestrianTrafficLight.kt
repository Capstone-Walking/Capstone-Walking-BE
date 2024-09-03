package com.walking.traffic.usecase.model

import java.time.LocalDateTime

data class PedestrianTrafficLight(
    val id: Long,
    val name: String,
    val point: PointDetail,
    val redCycle: CycleDetail,
    val greenCycle: CycleDetail,
    val leftTime: LeftTimeDetail,
    val color: ColorDetail,
)

data class PointDetail(
    val lat: Double,
    val lng: Double,
)

data class CycleDetail(
    val cycle: Double,
    val status: PredictStatus,
    val updateAt: LocalDateTime = LocalDateTime.now(),
)

data class LeftTimeDetail(
    val time: Double,
    val updateAt: LocalDateTime,
)

data class ColorDetail(
    val color: String,
    val updateAt: LocalDateTime,
)