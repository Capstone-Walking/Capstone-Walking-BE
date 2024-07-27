package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import com.walking.api.data.entity.traffic.TrafficColor.*
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.abs

class PredictTargetTraffic(
    val traffic: TargetTrafficVO,
    val recentTrafficDetails: RecentTrafficDetails,
    var topTrafficDetail: TargetTrafficDetailVO? = null,
    var redCycle: Float? = null,
    var greenCycle: Float? = null,
    var currentColor: TrafficColor? = null,
    var currentTimeLeft: Float? = null
) {
    constructor(traffic: TargetTrafficVO, recentTrafficDetails: RecentTrafficDetails) : this(traffic, recentTrafficDetails, null, null, null, null, null) {
        this.topTrafficDetail = recentTrafficDetails.getTopTrafficDetail()
        this.currentColor = recentTrafficDetails.getCurrentColor()
        this.currentTimeLeft = recentTrafficDetails.getCurrentTimeLeft()
    }

    /**
     * 사이클, 현재 신호 색상 및 잔여시간에 대해 모두 정상적으로 예측이 되었는지 판단합니다.
     */
    fun isAllPredicted(): Boolean {
        return isPredictCycleSuccessful() && currentColor != null && currentTimeLeft!! > 0
    }

    private fun isPredictCycleSuccessful(): Boolean {
        return isPredictedRedCycle() && isPredictedGreenCycle()
    }

    private fun isPredictedRedCycle(): Boolean {
        return redCycle != null
    }

    private fun isPredictedGreenCycle(): Boolean {
        return greenCycle != null
    }

    fun predictCycle() {
        this.updateGreenCycle(recentTrafficDetails.predictGreenCycle())
        this.updateRedCycle(recentTrafficDetails.predictRedCycle())
    }

    fun doPredict(standardTime: OffsetDateTime): Boolean {
        if (!this.isPredictCycleSuccessful()) {
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

    private fun updateRedCycle(redCycle: Float?) {
        if ((redCycle == null) || (redCycle < 0) || (redCycle > 1000)) {
            this.redCycle = null
            return
        }
        redCycle.also { this.redCycle = it }
    }

    private fun updateGreenCycle(greenCycle: Float?) {
        if ((greenCycle == null) || (greenCycle < 0) || (greenCycle > 1000)) {
            this.greenCycle = null
            return
        }
        greenCycle.also { this.greenCycle = it }
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