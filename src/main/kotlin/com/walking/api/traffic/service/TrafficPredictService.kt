package com.walking.api.traffic.service

import com.walking.api.data.entity.traffic.TrafficDetailEntity
import com.walking.api.data.entity.traffic.TrafficEntity
import com.walking.api.repository.dao.traffic.TrafficDetailRepository
import com.walking.api.repository.dao.traffic.TrafficRepository
import com.walking.api.traffic.service.dto.TPQuery
import com.walking.api.traffic.service.dto.TPVO
import com.walking.api.traffic.service.model.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.stream.Collectors

@Service
class TrafficPredictService(
    private val trafficDetailRepository: TrafficDetailRepository,
    private val trafficRepository: TrafficRepository
) {
    @Value("\${walking.batch.schedular.interval:70}")
    private val interval = 0

    @Transactional
    fun execute(requestDto: TPQuery): TPVO {
        val trafficIds = requestDto.trafficIds

        /** trafficIds를 통해 traffic 데이터를 가져옵니다.  */
        val traffics = trafficRepository.findAllInIds(trafficIds).stream()
            .map { e: TrafficEntity ->
                TargetTrafficVO(e.id, e.name, e.point, e.detail)
            }
            .collect(Collectors.toList())

        val trafficPredictTargets = PredictTargetTraffics()
        for (traffic in traffics) {
            /** traffic의 id를 통해 최근 30개의 trafficDetail 데이터를 가져옵니다.  */
            val recentTrafficDetails = trafficDetailRepository
                .findTopWhereTrafficIdOrderByTimeLeftRegDtDesc(traffic.id, 30)
                .stream()
                .map { e: TrafficDetailEntity ->
                    TargetTrafficDetailVO(
                        e.id,
                        e.traffic,
                        e.color,
                        e.timeLeft,
                        e.direction,
                        e.colorRegDt,
                        e.timeLeftRegDt
                    )
                }
                .collect(Collectors.toList())

            /** traffic과 traffic_detail 데이터를 통해 PredictTargetTraffic을 생성합니다.  */
            val predictTargetTraffic = PredictTargetTraffic(traffic, RecentTrafficDetails(interval, recentTrafficDetails))
            trafficPredictTargets.addTraffic(predictTargetTraffic)
        }

        val now = OffsetDateTime.now()
        val predictedMap = trafficPredictTargets.doAllPredict(now)
        return TPVO(predictedMap)
    }
}