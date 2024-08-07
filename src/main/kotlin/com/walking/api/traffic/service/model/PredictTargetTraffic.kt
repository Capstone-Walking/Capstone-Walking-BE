package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import com.walking.api.data.entity.traffic.TrafficColor.*
import org.apache.commons.logging.LogFactory
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.math.abs

class PredictTargetTraffic(
    val traffic: TargetTrafficVO,
    private var topTrafficDetail: TargetTrafficDetailVO?,
    var currentColor: TrafficColor,
    var currentTimeLeft: Float,
    var greenCycle: Float?,
    var redCycle: Float?
) {
    private val log = LogFactory.getLog(PredictTargetTraffic::class.java)
    constructor(
        traffic: TargetTrafficVO,
        recentTrafficDetails: RecentTrafficDetails
    ) : this(
        traffic,
        recentTrafficDetails.getTopTrafficDetail(),
        recentTrafficDetails.getCurrentColor(),
        recentTrafficDetails.getCurrentTimeLeft(),
        recentTrafficDetails.predictGreenCycle(),
        recentTrafficDetails.predictRedCycle()
    )

    /**
     * 사이클, 현재 신호 색상 및 잔여시간에 대해 모두 정상적으로 예측이 되었는지 판단합니다.
     */
    fun isAllPredicted(): Boolean {
        return ((redCycle != null) && (greenCycle != null)) && (currentTimeLeft > 0)
    }

    fun doPredict(standardTime: OffsetDateTime): Boolean {
        if (!((redCycle != null) && (greenCycle != null))) {
            return false
        }

        if (topTrafficDetail == null) {
            return false
        }
        val topTrafficDetail = topTrafficDetail!!

        if (topTrafficDetail.timeLeft >= 3600.1) {
            return false
        }

        /** 현재 시간과 가장 최근의 신호등 정보 조회 API 호출 시간 사이의 차이를 구합니다.  */
        val gapTimeBetweenLastTrafficDetailAndNow = getDifferenceInSeconds(topTrafficDetail.timeLeftRegDt, standardTime)
        log.info("Gap Time: $gapTimeBetweenLastTrafficDetailAndNow")
        log.info("Standard Time: $standardTime, Last Traffic Detail Time: ${topTrafficDetail.timeLeftRegDt}")

        /**
         * green/red 사이클 기반으로 가장 최근의 신호등 정보 조회 API 호출 시간과 현재 시간 사이의 가려진 정보를 구합니다.
         * ex)
         * Last Traffic Detail Time: 2021-08-01 12:00:00
         * Last Traffic Detail Time Left: 10s
         * Current Time: 2021-08-01 12:01:25
         * Current Color: RED
         *
         * 2021-08-01 12:00:10 ~ 2021-08-01 12:01:25 사이의 가려진 정보를 구합니다.
         *
         * RedCycle: 10s
         * GreenCycle: 20s
         *
         * 2021-08-01 12:00:00 RED, Time Left: 10s, Gap Time: 85s
         * 2021-08-01 12:00:10 GREEN, Gap Time: 85s - 10s = 75s // Left Time을 소진하고 color를 바꿔준다.
         * 2021-08-01 12:00:30 RED, Gap Time: 75s - 20s = 55s
         * 2021-08-01 12:00:40 GREEN, Gap Time: 55s - 10s = 45s
         * 2021-08-01 12:01:00 RED, Gap Time: 45s - 20s = 25s
         * 2021-08-01 12:01:10 GREEN, Gap Time: 25s - 10s = 15s
         * 2021-08-01 12:01:30 RED, Gap Time: 15s - 20s = -5s
         *
         * 2021-08-01 12:01:25에는 GREEN이어야 합니다.
         * 남은 시간의 경우 5s가 남았어야 합니다.
         * */
        var gapTime = gapTimeBetweenLastTrafficDetailAndNow - topTrafficDetail.timeLeft
        var color = if (currentColor.isRed) {
            GREEN
        } else {
            RED
        }

        while (gapTime > 0) {
            if (color.isRed) {
                color = GREEN
                gapTime -= redCycle!!
            } else {
                color = RED
                gapTime -= greenCycle!!
            }
        }
        log.info("Predicted Gap Time: $gapTime")
        currentColor = if (color.isRed) {
            GREEN
        } else {
            RED
        }
        currentTimeLeft = abs(gapTime)
        log.info("Predicted Color: $currentColor, Predicted Time Left: $currentTimeLeft")

        if (currentTimeLeft < 0) {
            currentTimeLeft = 0f
        }

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
}