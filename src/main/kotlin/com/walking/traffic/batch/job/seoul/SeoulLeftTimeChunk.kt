package com.walking.traffic.batch.job.seoul

import java.time.LocalDateTime

data class SeoulLeftTimeChunk(
    val itstId: Long,
    val direction: String,
    val color: String,
    val leftTime: Double,
    val colorRegDt: LocalDateTime,
    val leftTimeRegDt: LocalDateTime,
)