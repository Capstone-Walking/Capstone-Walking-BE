package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import com.walking.api.data.entity.traffic.TrafficColor.*
import java.time.Duration
import java.time.OffsetDateTime

class PredictTargetTraffic(
    val traffic: TargetTrafficVO,
    private var topTrafficDetail: TargetTrafficDetailVO?,
    var currentColor: TrafficColor,
    var currentTimeLeft: Float,
    var greenCycle: Float?,
    var redCycle: Float?
) {
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

        var color = currentColor

        /** 현재 시간과 가장 최근의 신호등 정보 조회 API 호출 시간 사이의 차이를 구합니다.  */
        val gapTimeBetweenLastTrafficDetailAndNow = getDifferenceInSeconds(topTrafficDetail.timeLeftRegDt, standardTime)

        /**
         * green/red 사이클 기반으로 가장 최근의 신호등 정보 조회 API 호출 시간과 현재 시간 사이의 가려진 정보를 구합니다.
         * ex)
         * Last Traffic Detail Time: 2021-08-01 12:00:00
         * Last Traffic Detail Time Left: 10s
         * Current Time: 2021-08-01 12:01:20
         *
         * 2021-08-01 12:00:10 ~ 2021-08-01 12:01:20 사이의 가려진 정보를 구합니다.
         *
         * RedCycle: 10s
         * GreenCycle: 20s
         *
         * 2021-08-01 12:00:00 RED, Time Left: 10s, Gap Time: 80s
         * 2021-08-01 12:00:10 GREEN, Gap Time: 80s - 10s = 70s
         * 2021-08-01 12:00:30 RED, Gap Time: 70s - 20s = 50s
         * 2021-08-01 12:00:40 GREEN, Gap Time: 50s - 10s = 40s
         * 2021-08-01 12:01:00 RED, Gap Time: 40s - 20s = 20s
         * 2021-08-01 12:01:10 GREEN, Gap Time: 20s - 10s = 10s
         * 2021-08-01 12:01:30 RED, Gap Time: 10s - 20s = -10s
         *
         * 2021-08-01 12:01:20에는 GREEN이어야 합니다.
         * 남은 시간의 경우 -10s + 20s = 10s 남았습니다.
         * */
        var gapTime = gapTimeBetweenLastTrafficDetailAndNow - topTrafficDetail.timeLeft
        while (gapTime >= 0) {
            if (color.isRed) {
                color = GREEN
                gapTime -= greenCycle!!
            } else {
                color = RED
                gapTime -= redCycle!!
            }
        }

        currentColor = color
        currentTimeLeft = if (color.isRed) {
            (gapTime + greenCycle!!)
        } else {
            (gapTime + redCycle!!)
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