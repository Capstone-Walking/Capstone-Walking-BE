package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.constant.Direction
import com.walking.api.data.entity.traffic.constant.TrafficColor
import java.time.OffsetDateTime

data class TargetTrafficDetailVO(
    val id: Long,
    val trafficId: Long,
    val color: TrafficColor,
    val timeLeft: Float,
    val direction: Direction,
    val colorRegDt: OffsetDateTime,
    val timeLeftRegDt: OffsetDateTime
)