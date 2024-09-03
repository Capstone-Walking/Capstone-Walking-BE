package com.walking.traffic.usecase.service.left

import com.walking.traffic.data.entity.PedestrianLightEntity
import com.walking.traffic.data.entity.TrafficColor
import com.walking.traffic.data.repository.PedestrianLightRepository
import com.walking.traffic.usecase.model.*
import com.walking.traffic.usecase.model.left.RecentPedestrianLeftTimes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.function.Function
import java.util.function.Predicate

@Service
class SeoulLeftTimeCalculateService(
    private val pedestrianLightRepository: PedestrianLightRepository,
) : CalculatePedestrianLightService<Map<Long, RecentPedestrianLeftTimes>> {

    /**
     * 1. 신호등 정보와 예측 정보를 매핑합니다.
     * 2. 예측 정보가 없다면 가장 최근의 예측 정보를 가져옵니다.
     * 3. 예측 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    override fun execute(
        sources: List<PedestrianLightEntity>,
        data: Map<Long, RecentPedestrianLeftTimes>,
    ): List<PedestrianTrafficLight> {
        val ids = sources.map { it.id }
        val idMappedLocationName = sources.associateBy({ it.id }, { it.locationName })
        val idMappedPoint = sources.associateBy({ it.id }, { it.locationPoint })

        val pedestrianTrafficLights = mutableListOf<PedestrianTrafficLight>()
        for (id in ids) {
            val recentLeftTraffic = data[id]
            val topLeftTrafficsRegisterTime: LocalDateTime =
                recentLeftTraffic?.latestLeftTimeTrafficsRegisterTime ?: continue

            val topLeftTrafficColor = recentLeftTraffic.latestLeftTimeTrafficColor ?: TrafficColor.DARK
            val topLeftTrafficLeftTime = recentLeftTraffic.latestLeftTimeTrafficLeftTime ?: 0.0

            val greenCycle: CycleDetail = recentLeftTraffic.predictGreenCycle()?.let {
                CycleDetail(it.cycle, PredictStatus.SUCCESS, it.updatedAt)
            } ?: ifPredictFail(
                id,
                { it.greenCycle == 0.0 },
                { CycleDetail(it.greenCycle, PredictStatus.OLD, it.modifiedAt) }
            )

            val redCycle: CycleDetail = recentLeftTraffic.predictRedCycle()?.let {
                CycleDetail(it.cycle, PredictStatus.SUCCESS, it.updatedAt)
            } ?: ifPredictFail(
                id,
                { it.redCycle == 0.0 },
                { CycleDetail(it.redCycle, PredictStatus.OLD, it.modifiedAt) }
            )

            pedestrianTrafficLights.add(
                PedestrianTrafficLight(
                    id = id,
                    name = idMappedLocationName[id]!!,
                    color = ColorDetail(topLeftTrafficColor.name, topLeftTrafficsRegisterTime),
                    leftTime = LeftTimeDetail(topLeftTrafficLeftTime, topLeftTrafficsRegisterTime),
                    point = PointDetail(
                        lat = idMappedPoint[id]!!.y,
                        lng = idMappedPoint[id]!!.x
                    ),
                    redCycle = redCycle,
                    greenCycle = greenCycle
                )
            )
        }

        return pedestrianTrafficLights
    }

    /**
     * @param cycleNotUpdatePredict: 주기 예측이 업데이트 되지 않은 경우
     * @param oldUpdatedCycleFunction:주기 예측이 업데이트 된 경우 주기를 반환하는 함수
     */
    private fun ifPredictFail(
        id: Long,
        cycleNotUpdatePredict: Predicate<PedestrianLightEntity>,
        oldUpdatedCycleFunction: Function<PedestrianLightEntity, CycleDetail>,
    ): CycleDetail {
        return pedestrianLightRepository.findById(id).let {
            if (it.isEmpty) {
                CycleDetail(0.0, PredictStatus.FAIL)
            } else {
                if (cycleNotUpdatePredict.test(it.get())) {
                    CycleDetail(0.0, PredictStatus.FAIL)
                } else {
                    oldUpdatedCycleFunction.apply(it.get())
                }
            }
        }
    }
}