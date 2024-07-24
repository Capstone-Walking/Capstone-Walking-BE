package com.walking.api.traffic.service.dto

import com.walking.api.traffic.service.model.PredictTargetTraffic

data class TPVO(
    val predictedData: Map<Long, PredictTargetTraffic>
)