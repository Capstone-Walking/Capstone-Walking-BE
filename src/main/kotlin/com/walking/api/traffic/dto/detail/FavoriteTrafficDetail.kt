package com.walking.api.traffic.dto.detail

import java.time.LocalDateTime

data class FavoriteTrafficDetail(
    val id: Long,
    val detail: TrafficDetailInfo,
    val name: String,
    val point: PointDetail,
    val createdAt: LocalDateTime
)