package com.walking.traffic.usecase.model.left

import com.walking.traffic.data.entity.TrafficColor
import java.time.LocalDateTime

data class PedestrianLeftTimeInfo(
    val id: String,
    val color: TrafficColor,
    val leftTime: Double,
    val createdAt: LocalDateTime,
)