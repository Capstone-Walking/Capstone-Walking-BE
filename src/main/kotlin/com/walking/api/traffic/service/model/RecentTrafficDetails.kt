package com.walking.api.traffic.service.model

import com.walking.api.data.entity.traffic.TrafficColor
import java.time.Duration
import java.time.OffsetDateTime

class RecentTrafficDetails(
    private val interval: Int,
    /** timeLeftRegDt를 기준으로 정렬된 최근 trafficDetail 데이터 */
    private val trafficDetails: List<TargetTrafficDetailVO>
) {
    fun getTopTrafficDetail(): TargetTrafficDetailVO? {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0]
        } else {
            null
        }
    }

    fun getCurrentColor(): TrafficColor {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0].color
        } else {
            TrafficColor.DARK
        }
    }

    fun getCurrentTimeLeft(): Float {
        return if (trafficDetails.isNotEmpty()) {
            trafficDetails[0].timeLeft
        } else {
            return 0f
        }
    }

    /** trafficDetails를 통해 redCycle을 예측합니다. */
    fun predictRedCycle(): Float? {
        var redCycle: Float? = null
        val iterator = trafficDetails.iterator()
        if (!iterator.hasNext()) {
            return redCycle
        } else {
            var afterData = iterator.next()
            while (iterator.hasNext()) {
                val beforeData = iterator.next()
                /** green -> red 패턴 확인 */
                if (isGreenToRedPattern(beforeData, afterData)) {
                    /** beforeData와 afterData 사이의 시간 차이를 확인 */
                    if (checkMissingDataBetween(
                            beforeData.timeLeftRegDt,
                            afterData.timeLeftRegDt
                        )
                    ) {
                        redCycle = calculateCycle(beforeData, afterData)
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
                    if (checkMissingDataBetween(beforeData.timeLeftRegDt, afterData.timeLeftRegDt)) {
                        greenCycle = calculateCycle(beforeData, afterData)
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
        before: OffsetDateTime,
        afterData: OffsetDateTime
    ): Boolean {
        val bias = 10
        val differenceInSeconds = this.getDifferenceInSeconds(before, afterData)
        return differenceInSeconds > 0 && differenceInSeconds < interval + bias
    }

    private fun getDifferenceInSeconds(start: OffsetDateTime?, end: OffsetDateTime?): Float {
        val duration = Duration.between(start, end)
        val seconds = duration.seconds
        val nanoSeconds = duration.nano
        return seconds + nanoSeconds / 1000000000.0f
    }

    /**
     * ex)
     * before: 10s(green), interval: 30s, after: 50s(red)
     * before(신호정보 조회 API 호출) -> interval(대기) -> after(신호정보 조회 API 호출)
     * 위의 정보를 통해 interval 시점에 green이 10s 표시, red가 20s 표시되었음을 알 수 있다.
     * 따라서 after 시점에 이미 표시된 20s와 남은 시간인 50s를 더하여 redCycle이 70s임을 알 수 있다.
     */
    private fun calculateCycle(
        before: TargetTrafficDetailVO,
        afterData: TargetTrafficDetailVO
    ): Float {
        return afterData.timeLeft + interval - before.timeLeft
    }
}