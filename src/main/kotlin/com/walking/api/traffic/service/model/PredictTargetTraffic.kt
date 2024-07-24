package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import com.walking.api.data.entity.traffic.TrafficColor.*
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.abs

class PredictTargetTraffic(
    val traffic: TargetTrafficVO,
    var topTrafficDetail: TargetTrafficDetailVO? = null,
    var redCycle: Float? = null,
    var greenCycle: Float? = null,
    var currentColor: TrafficColor? = null,
    var currentTimeLeft: Float? = null
) {

    /**
     * 사이클 예측이 끝났는지 여부를 반환합니다.
     */
    val isPredictCycleSuccessful: Boolean
        get() = isPredictedRedCycle && isPredictedGreenCycle

    private val isPredictedRedCycle: Boolean
        get() = redCycle != null

    private val isPredictedGreenCycle: Boolean
        get() = greenCycle != null

    /**
     * 사이클, 현재 신호 색상 및 잔여시간에 대해 모두 정상적으로 예측이 되었는지 판단합니다.
     */
    val isAllPredicted: Boolean
        get() = isPredictCycleSuccessful && currentColor != null && currentTimeLeft!! > 0

    val currentColorDescription: String? =
        this.currentColor?.toString()

    fun predictCycle(recentTrafficDetails: RecentTrafficDetails) {
        if (!isPredictedGreenCycle) {
            this.updateGreenCycle(recentTrafficDetails.predictGreenCycle())
        }
        if (!isPredictedRedCycle) {
            this.updateRedCycle(recentTrafficDetails.predictRedCycle())
        }
    }

    fun doPredict(standardTime: OffsetDateTime): Boolean {
        if (!this.isPredictCycleSuccessful) {
            return false
        }
        if (this.topTrafficDetail == null) {
            return false
        }

        var color = this.currentColor
        val topTimeLeft = topTrafficDetail!!.timeLeft
        val gapTime = this.getDifferenceInSeconds(this.topTrafficDetail!!.timeLeftRegDt, standardTime)
        if (gapTime > 60 * 60) {
            this.updateCurrentColor(DARK)
            this.updateCurrentTimeLeft(0f)
            return false
        }
        var seconds = gapTime - topTimeLeft
        while (seconds >= 0) {
            if (color!!.isRed) {
                color = GREEN
                val cycleOfNextColor = this.getCycleByColor(GREEN)
                seconds -= cycleOfNextColor!!
            } else {
                color = RED
                val cycleOfNextColor = this.getCycleByColor(RED)
                seconds -= cycleOfNextColor!!
            }
        }

        this.updateCurrentColor(color)
        this.updateCurrentTimeLeft(abs(seconds))
        return true
    }

    private fun getDifferenceInSeconds(
        start: OffsetDateTime?,
        end: OffsetDateTime?
    ): Float {
        val duration = Duration.between(start, end)
        val seconds = duration.seconds
        val nanoSeconds = duration.nano
        return seconds + nanoSeconds / 1000000000.0f
    }

    private fun updateRedCycle(redCycle: Optional<Float>) {
        if (redCycle.isEmpty || redCycle.get() < 0 || redCycle.get() > 1000) {
            this.redCycle = null
            return
        }
        this.redCycle = redCycle.orElse(null)
    }

    private fun updateGreenCycle(greenCycle: Optional<Float>) {
        if (greenCycle.isEmpty || greenCycle.get() < 0 || greenCycle.get() > 1000) {
            this.greenCycle = null
            return
        }
        this.greenCycle = greenCycle.orElse(null)
    }

    private fun updateCurrentColor(color: TrafficColor?) {
        currentColor = color
    }

    private fun updateCurrentTimeLeft(timeLeft: Float?) {
        currentTimeLeft = timeLeft
    }

    private fun getCycleByColor(color: TrafficColor): Float? {
        if (color.isGreen) {
            return greenCycle
        }
        return if (color.isRed) {
            redCycle
        } else {
            -1f
        }
    }
}