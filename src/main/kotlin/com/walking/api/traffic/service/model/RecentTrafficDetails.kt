package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.max

class RecentTrafficDetails(private val interval: Int, private val trafficDetails: List<TargetTrafficDetailVO>) {
    fun getTopTrafficDetail(): TargetTrafficDetailVO? {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0]
        } else {
            null
        }
    }

    fun getCurrentColor(): TrafficColor? {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0].color
        } else {
            null
        }
    }

    fun getCurrentTimeLeft(): Float? {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0].timeLeft
        } else {
            null
        }
    }

    fun predictRedCycle(): Float? {
        var redCycle: Float? = null
        val iterator = trafficDetails.iterator()
        if (!iterator.hasNext()) {
            return redCycle
        } else {
            var afterData = iterator.next()
            while (iterator.hasNext()) {
                val beforeData = iterator.next()
                if (isGreenToRedPattern(beforeData, afterData)) {
                    val calculateCycle = calculateCycle(beforeData, afterData)
                    if (!Objects.isNull(redCycle)) {
                        /** redCycle을 정확히 계산하지 못하더라고 최대 redCycle을 저장하여 사용 */
                        redCycle = max(redCycle!!.toDouble(), calculateCycle.toDouble()).toFloat()
                    }
                    redCycle = calculateCycle
                    if (!checkMissingDataBetween(beforeData, afterData)) {
                        break
                    }
                }
                afterData = beforeData
            }
            return redCycle
        }
    }

    fun predictGreenCycle(): Float? {
        var greenCycle: Float? = null
        val iterator = trafficDetails.iterator()
        if (!iterator.hasNext()) {
            return greenCycle
        } else {
            var afterData = iterator.next()
            while (iterator.hasNext()) {
                val beforeData = iterator.next()
                if (isRedToGreenPattern(beforeData, afterData)) {
                    val calculateCycle = calculateCycle(beforeData, afterData)
                    if (!Objects.isNull(greenCycle)) {
                        /** greenCycle을 정확히 계산하지 못하더라고 최대 greenCycle을 저장하여 사용 */
                        greenCycle = max(greenCycle!!.toDouble(), calculateCycle.toDouble()).toFloat()
                    }
                    greenCycle = calculateCycle
                    if (!checkMissingDataBetween(beforeData, afterData)) {
                        break
                    }
                }
                afterData = beforeData
            }
            return greenCycle
        }
    }

    private fun isGreenToRedPattern(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Boolean {
        return before.color.isGreen && afterData.color.isRed
    }

    private fun isRedToGreenPattern(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Boolean {
        return before.color.isRed && afterData.color.isGreen
    }

    private fun checkMissingDataBetween(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Boolean {
        val bias = 10
        val differenceInSeconds = this.getDifferenceInSeconds(
            before.timeLeftRegDt,
            afterData.timeLeftRegDt
        )
        return differenceInSeconds > 0 && differenceInSeconds < interval + bias
    }

    private fun getDifferenceInSeconds(start: OffsetDateTime?, end: OffsetDateTime?): Float {
        val duration = Duration.between(start, end)
        val seconds = duration.seconds
        val nanoSeconds = duration.nano
        return seconds + nanoSeconds / 1000000000.0f
    }

    private fun calculateCycle(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Float {
        return afterData.timeLeft + interval - before.timeLeft
    }
}