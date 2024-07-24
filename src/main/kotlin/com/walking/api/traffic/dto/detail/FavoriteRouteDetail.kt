package com.walking.api.traffic.dto.detail

import java.time.LocalDateTime
data class FavoriteRouteDetail(
    val id: Long,
    val name: String,
    val startPoint: PointDetail,
    val endPoint: PointDetail,
    val createdAt: LocalDateTime,
    val startAlias: String,
    val endAlias: String,
    val order: Long
)