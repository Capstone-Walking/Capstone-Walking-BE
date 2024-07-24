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
        val traffics = trafficRepository.findAllInIds(trafficIds).stream()
            .map { e: TrafficEntity ->
                TargetTrafficVO(e.id, e.name, e.point, e.detail)
            }
            .collect(Collectors.toList())
        val trafficPredictTargets = PredictTargetTraffics()
        for (traffic in traffics) {
            /** trafficId를 통해 traffic_detail 데이터를 가져옵니다.  */
            val recentTrafficDetails = trafficDetailRepository
                .findTopWhereTrafficIdOrderByTimeLeftRegDtDesc(traffic.id)
                .stream()
                .map { e: TrafficDetailEntity ->
                    TargetTrafficDetailVO(
                        e.id,
                        e.traffic.id,
                        e.color,
                        e.timeLeft,
                        e.direction,
                        e.colorRegDt,
                        e.timeLeftRegDt
                    )
                }
                .collect(Collectors.toList())
            val predictTargetTraffic = PredictTargetTraffic(traffic)
            if (!recentTrafficDetails.isEmpty()) {
                predictTargetTraffic.topTrafficDetail = recentTrafficDetails[0]
            }
            predictTargetTraffic.predictCycle(
                RecentTrafficDetails(interval, recentTrafficDetails)
            )
            trafficPredictTargets.addTraffic(predictTargetTraffic)
        }
        val now = OffsetDateTime.now()
        val predictedMap = trafficPredictTargets.doAllPredict(now)
        return TPVO(predictedMap)
    }
}