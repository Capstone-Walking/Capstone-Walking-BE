package com.walking.api.traffic.service.model

import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.max

class RecentTrafficDetails(private val interval: Int, private val trafficDetails: List<TargetTrafficDetailVO>) {

    fun predictRedCycle(): Optional<Float> {
        var redCycle = Optional.empty<Float>()
        val iterator = trafficDetails.iterator()
        var afterData = iterator.next()
        while (iterator.hasNext()) {
            val beforeData = iterator.next()
            if (isGreenToRedPattern(beforeData, afterData)) {
                val calculateCycle = calculateCycle(beforeData, afterData)
                if (redCycle.isPresent && calculateCycle.isPresent) {
                    /** redCycle을 정확히 계산하지 못하더라고 최대 redCycle을 저장하여 사용 */
                    redCycle = Optional.of(
                        max(redCycle.get().toDouble(), calculateCycle.get().toDouble()).toFloat()
                    )
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

    fun predictGreenCycle(): Optional<Float> {
        var greenCycle = Optional.empty<Float>()
        val iterator = trafficDetails.iterator()
        var afterData = iterator.next()
        while (iterator.hasNext()) {
            val beforeData = iterator.next()
            if (isRedToGreenPattern(beforeData, afterData)) {
                val calculateCycle = calculateCycle(beforeData, afterData)
                if (greenCycle.isPresent && calculateCycle.isPresent) {
                    greenCycle = Optional.of(
                        max(greenCycle.get().toDouble(), calculateCycle.get().toDouble())
                            .toFloat()
                    )
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

    fun getDifferenceInSeconds(start: OffsetDateTime?, end: OffsetDateTime?): Float {
        val duration = Duration.between(start, end)
        val seconds = duration.seconds
        val nanoSeconds = duration.nano
        return seconds + nanoSeconds / 1000000000.0f
    }

    private fun calculateCycle(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Optional<Float> {
        return Optional.of(afterData.timeLeft + interval - before.timeLeft)
    }
}