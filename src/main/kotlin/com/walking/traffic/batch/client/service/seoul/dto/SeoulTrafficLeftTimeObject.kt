package com.walking.traffic.batch.client.service.seoul.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SeoulTrafficLeftTimeObject(
    val itstId: Long,
    val ntPdsgRmdrCs: Double?,
    val etPdsgRmdrCs: Double?,
    val stPdsgRmdrCs: Double?,
    val wtPdsgRmdrCs: Double?,
    val nePdsgRmdrCs: Double?,
    val sePdsgRmdrCs: Double?,
    val swPdsgRmdrCs: Double?,
    val nwPdsgRmdrCs: Double?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val regDt: LocalDateTime,
)