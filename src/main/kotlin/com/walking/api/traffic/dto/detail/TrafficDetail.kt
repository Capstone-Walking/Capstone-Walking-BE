package com.walking.api.traffic.dto.detail

data class TrafficDetail(
    val id: Long,
    val color: String,
    val timeLeft: Float,
    val point: PointDetail,
    val redCycle: Float,
    val greenCycle: Float,
    val detail: TrafficDetailInfo,
    val isFavorite: Boolean,
    val viewName: String
)