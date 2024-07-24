package com.walking.api.traffic.usecase

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.walking.api.data.entity.traffic.TrafficEntity
import com.walking.api.repository.dao.traffic.TrafficRepository
import com.walking.api.traffic.dto.SearchTrafficsUseCaseIn
import com.walking.api.traffic.dto.SearchTrafficsUseCaseOut
import com.walking.api.traffic.dto.detail.PointDetail
import com.walking.api.traffic.dto.detail.TrafficDetail
import com.walking.api.traffic.dto.detail.TrafficDetailInfo
import com.walking.api.traffic.service.TrafficPredictService
import com.walking.api.traffic.service.dto.TPQuery
import com.walking.api.traffic.service.model.PredictTargetTraffic
import com.walking.api.traffic.service.model.TargetTrafficVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class SearchTrafficsUseCase(
    private val trafficRepository: TrafficRepository,
    private val trafficPredictService: TrafficPredictService
) {
    @Transactional
    fun execute(request: SearchTrafficsUseCaseIn): SearchTrafficsUseCaseOut {
        val vblLng = request.vblLng
        val vblLat = request.vblLat
        val vtrLng = request.vtrLng
        val vtrLat = request.vtrLat

        /** 범위 안에 있는 신호등들을 조회합니다.  */
        val inBoundsTrafficIds =
            trafficRepository.findTrafficWithinBounds(vblLng, vblLat, vtrLng, vtrLat).stream()
                .map { obj: TrafficEntity -> obj.id }
                .collect(Collectors.toList())
        val predictedData: List<PredictTargetTraffic> = ArrayList(
            trafficPredictService
                .execute(TPQuery(inBoundsTrafficIds))
                .predictedData
                .values
        )
        val trafficDetails = execute(predictedData)
        return SearchTrafficsUseCaseOut(trafficDetails)
    }

    fun execute(predictedData: List<PredictTargetTraffic>): List<TrafficDetail> {
        return predictedData.stream()
            .map { predictedDatum: PredictTargetTraffic ->
                TrafficDetail(
                    predictedDatum.traffic.id,
                    predictedDatum.currentColorDescription!!,
                    predictedDatum.currentTimeLeft!!,
                    PointDetail(
                        predictedDatum.traffic.point.getY(),
                        predictedDatum.traffic.point.getX()
                    ),
                    predictedDatum.redCycle!!,
                    predictedDatum.greenCycle!!,
                    convertToTrafficDetailInfo(predictedDatum.traffic),
                    false,
                    predictedDatum.traffic.name
                )
            }
            .collect(Collectors.toList())
    }

    private fun convertToTrafficDetailInfo(trafficEntity: TargetTrafficVO): TrafficDetailInfo {
        val objectMapper = ObjectMapper()
        var trafficDetailInfo = TrafficDetailInfo(-1L, "ERROR", "ERROR")
        trafficDetailInfo = try {
            objectMapper.readValue(
                trafficEntity.detail,
                TrafficDetailInfo::class.java
            )
        } catch (e: JsonMappingException) {
            throw RuntimeException("Convert to TrafficDetailInfo fail", e)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Convert to TrafficDetailInfo fail", e)
        }
        return trafficDetailInfo
    }
}