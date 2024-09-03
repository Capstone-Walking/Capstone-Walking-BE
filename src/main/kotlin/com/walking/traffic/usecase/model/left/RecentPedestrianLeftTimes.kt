package com.walking.traffic.usecase.model.left

import com.walking.traffic.data.entity.TrafficColor
import com.walking.traffic.data.entity.TrafficColor.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.function.BiPredicate

data class CalculateCycleResult(
    val cycle: Double,
    val updatedAt: LocalDateTime,
)

class RecentPedestrianLeftTimes(
    private val browseLeftTimeInterval: Int,
    private val pedestrianLeftTimeInfos: List<PedestrianLeftTimeInfo>,
    val latestLeftTimeTrafficColor: TrafficColor?,
    val latestLeftTimeTrafficLeftTime: Double?,
    val latestLeftTimeTrafficsRegisterTime: LocalDateTime?,
) {
    companion object {
        val INVALID_LEFT_TIMES = listOf(36001.0, 2550.0, 1229.0)
    }

    constructor(
        browseCarTrafficInterval: Int,
        pedestrianLeftTimeInfos: List<PedestrianLeftTimeInfo>,
    ) : this(
        browseCarTrafficInterval,
        pedestrianLeftTimeInfos,
        pedestrianLeftTimeInfos.maxByOrNull { it.createdAt }?.color,
        pedestrianLeftTimeInfos.maxByOrNull { it.createdAt }?.leftTime,
        pedestrianLeftTimeInfos.maxByOrNull { it.createdAt }?.createdAt
    )

    fun predictGreenCycle(): CalculateCycleResult? {
        return doPredict {
                beforeData, afterData ->
            beforeData.color == RED && afterData.color == GREEN
        }
    }

    fun predictRedCycle(): CalculateCycleResult? {
        return doPredict {
                beforeData, afterData ->
            beforeData.color == GREEN && afterData.color == RED
        }
    }

    private fun doPredict(predicate: BiPredicate<PedestrianLeftTimeInfo, PedestrianLeftTimeInfo>): CalculateCycleResult? {
        pedestrianLeftTimeInfos.sortedBy { it.createdAt }.zipWithNext { beforeData, afterData ->
            if (isValid(beforeData, afterData) && predicate.test(beforeData, afterData)) {
                return calculateCycle(beforeData, afterData)
            }
        }
        return null
    }

    private fun isValid(
        before: PedestrianLeftTimeInfo,
        after: PedestrianLeftTimeInfo,
    ): Boolean {
        return isValidTime(before, after) && checkMissingDataBetween(before, after)
    }

    private fun isValidTime(
        before: PedestrianLeftTimeInfo,
        after: PedestrianLeftTimeInfo,
    ) = (
        /** Invalid leftTime Data */
        !INVALID_LEFT_TIMES.contains(before.leftTime) &&
            !INVALID_LEFT_TIMES.contains(after.leftTime)
        )

    private fun checkMissingDataBetween(
        before: PedestrianLeftTimeInfo,
        after: PedestrianLeftTimeInfo,
    ): Boolean {
        val interval = Duration.between(before.createdAt, after.createdAt).seconds
        return interval in (1..<browseLeftTimeInterval + 1)
    }

    private fun calculateCycle(
        before: PedestrianLeftTimeInfo,
        after: PedestrianLeftTimeInfo,
    ): CalculateCycleResult? {
        return (after.leftTime + browseLeftTimeInterval - before.leftTime)
            .takeIf { it > 0 }
            ?.let {
                CalculateCycleResult(it, after.createdAt)
            }
    }
}