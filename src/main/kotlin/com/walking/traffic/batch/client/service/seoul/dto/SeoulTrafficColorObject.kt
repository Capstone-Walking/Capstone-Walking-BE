package com.walking.traffic.batch.client.service.seoul.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SeoulTrafficColorObject(
    val itstId: Long,
    val ntPdsgStatNm: String?,
    val etPdsgStatNm: String?,
    val stPdsgStatNm: String?,
    val wtPdsgStatNm: String?,
    val nePdsgStatNm: String?,
    val sePdsgStatNm: String?,
    val swPdsgStatNm: String?,
    val nwPdsgStatNm: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val regDt: LocalDateTime,
)