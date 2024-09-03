package com.walking.traffic.batch.job.seoul

import com.walking.traffic.batch.client.service.seoul.SeoulColorClient
import com.walking.traffic.batch.client.service.seoul.SeoulLeftTimeClient
import com.walking.traffic.batch.client.service.seoul.dto.SeoulTrafficColorObject
import com.walking.traffic.batch.client.service.seoul.dto.SeoulTrafficLeftTimeObject
import org.springframework.batch.item.ItemReader
import java.time.LocalDateTime
import java.util.*

private fun String.toColor(): String {
    return when (this) {
        "stop-And-Remain" -> "RED"
        "protected-Movement-Allowed", "permissive-Movement-Allowed", "permissive-clearance" -> "GREEN"
        "dark", "protected-clearance" -> "DARK"
        else -> "UNKNOWN"
    }
}

class SeoulLeftTimeItemReader(
    private val seoulLeftTimeClient: SeoulLeftTimeClient,
    private val seoulColorClient: SeoulColorClient,
) : ItemReader<SeoulLeftTimeChunk> {

    private var iterator: Iterator<SeoulLeftTimeChunk>? = null

    override fun read(): SeoulLeftTimeChunk? {
        if (Objects.isNull(iterator)) {
            val leftTimeResponses: Iterable<SeoulTrafficLeftTimeObject> =
                seoulLeftTimeClient.execute()
            val leftTimeResponseMap: MutableMap<Long, SeoulTrafficLeftTimeObject> =
                HashMap<Long, SeoulTrafficLeftTimeObject>()

            for (leftTimeResponse in leftTimeResponses) {
                val seoulCarTrafficLeftTime = leftTimeResponseMap[leftTimeResponse.itstId]
                seoulCarTrafficLeftTime?.regDt?.let {
                    if (it.isBefore(leftTimeResponse.regDt)) {
                        leftTimeResponseMap[leftTimeResponse.itstId] = leftTimeResponse
                    }
                } ?: run {
                    leftTimeResponseMap[leftTimeResponse.itstId] = leftTimeResponse
                }
            }

            val colorResponses: Iterable<SeoulTrafficColorObject> = seoulColorClient.execute()
            val colorResponseMap: MutableMap<Long, SeoulTrafficColorObject> =
                HashMap<Long, SeoulTrafficColorObject>()
            for (colorResponse in colorResponses) {
                val seoulCarTrafficColor = colorResponseMap[colorResponse.itstId]
                seoulCarTrafficColor?.regDt?.let {
                    if (it.isBefore(colorResponse.regDt)) {
                        colorResponseMap[colorResponse.itstId] = colorResponse
                    }
                } ?: run {
                    colorResponseMap[colorResponse.itstId] = colorResponse
                }
            }

            val certificationIds =
                if (colorResponseMap.size < leftTimeResponseMap.size) colorResponseMap.keys else leftTimeResponseMap.keys
            val seoulTrafficDetails: MutableList<SeoulLeftTimeChunk> = ArrayList()
            for (certificationId in certificationIds) {
                if (colorResponseMap.containsKey(certificationId) && leftTimeResponseMap.containsKey(certificationId)) {
                    generateActiveDetails(certificationId, colorResponseMap, leftTimeResponseMap).let {
                        seoulTrafficDetails.addAll(it)
                    }
                }
            }
            iterator = seoulTrafficDetails.iterator()
        }

        return if (iterator!!.hasNext()) {
            iterator!!.next()
        } else {
            iterator = null
            null
        }
    }
    private fun generateActiveDetails(
        certificationId: Long,
        colorResponseMap: Map<Long, SeoulTrafficColorObject>,
        leftTimeResponseMap: Map<Long, SeoulTrafficLeftTimeObject>,
    ): List<SeoulLeftTimeChunk> {
        val details: MutableList<SeoulLeftTimeChunk> = ArrayList()
        val seoulCarTrafficColor = colorResponseMap[certificationId]
        val seoulCarTrafficLeftTime = leftTimeResponseMap[certificationId]

        if (seoulCarTrafficColor?.ntPdsgStatNm != null && seoulCarTrafficLeftTime?.ntPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "nt",
                seoulCarTrafficColor.ntPdsgStatNm,
                seoulCarTrafficLeftTime.ntPdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.etPdsgStatNm != null && seoulCarTrafficLeftTime?.etPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "et",
                seoulCarTrafficColor.etPdsgStatNm,
                seoulCarTrafficLeftTime.etPdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.stPdsgStatNm != null && seoulCarTrafficLeftTime?.stPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "st",
                seoulCarTrafficColor.stPdsgStatNm,
                seoulCarTrafficLeftTime.stPdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.wtPdsgStatNm != null && seoulCarTrafficLeftTime?.wtPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "wt",
                seoulCarTrafficColor.wtPdsgStatNm,
                seoulCarTrafficLeftTime.wtPdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.nePdsgStatNm != null && seoulCarTrafficLeftTime?.nePdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "ne",
                seoulCarTrafficColor.nePdsgStatNm,
                seoulCarTrafficLeftTime.nePdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.sePdsgStatNm != null && seoulCarTrafficLeftTime?.sePdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "se",
                seoulCarTrafficColor.sePdsgStatNm,
                seoulCarTrafficLeftTime.sePdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.swPdsgStatNm != null && seoulCarTrafficLeftTime?.swPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "sw",
                seoulCarTrafficColor.swPdsgStatNm,
                seoulCarTrafficLeftTime.swPdsgRmdrCs,
                details
            )
        }

        if (seoulCarTrafficColor?.nwPdsgStatNm != null && seoulCarTrafficLeftTime?.nwPdsgRmdrCs != null) {
            doGenerateActiveTrafficDetail(
                certificationId,
                colorResponseMap[certificationId]!!.regDt,
                leftTimeResponseMap[certificationId]!!.regDt,
                "nw",
                seoulCarTrafficColor.nwPdsgStatNm,
                seoulCarTrafficLeftTime.nwPdsgRmdrCs,
                details
            )
        }

        return details
    }

    private fun doGenerateActiveTrafficDetail(
        itstId: Long,
        colorRegDt: LocalDateTime,
        leftTimeRegDt: LocalDateTime,
        direction: String,
        pdsgStatNm: String?,
        pdsgRmdrCs: Double?,
        detials: MutableList<SeoulLeftTimeChunk>,
    ) {
        if (pdsgStatNm == null || pdsgRmdrCs == null) {
            null
        } else {
            SeoulLeftTimeChunk(
                itstId,
                direction,
                pdsgStatNm.toColor(),
                pdsgRmdrCs,
                colorRegDt,
                leftTimeRegDt
            )
        }?.let {
            detials.add(it)
        }
    }
}