package com.walking.api.traffic.dto

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.walking.api.traffic.dto.detail.FavoriteTrafficDetail
import com.walking.api.traffic.dto.detail.PointDetail
import com.walking.api.traffic.dto.detail.TrafficDetail
import com.walking.api.traffic.dto.detail.TrafficDetailInfo
import com.walking.api.traffic.service.model.PredictTargetTraffic
import com.walking.api.traffic.service.model.TargetTrafficVO

data class BrowseTrafficsUseCaseOut(
    val traffic: TrafficDetail
) {
    constructor(traffic: PredictTargetTraffic, favoriteTrafficDetail: FavoriteTrafficDetail?) : this(
        convertTrafficDetail(traffic, favoriteTrafficDetail)
    )

    constructor(trafficId: Long) : this(
        TrafficDetail(
            id = trafficId,
            color = "",
            timeLeft = 0f,
            point = PointDetail(0.0, 0.0),
            redCycle = 0f,
            greenCycle = 0f,
            detail = TrafficDetailInfo(-1L, "", ""),
            isFavorite = false,
            viewName = "ERROR"
        )
    )

    companion object {
        fun convertTrafficDetail(
            traffic: PredictTargetTraffic,
            favoriteTrafficDetail: FavoriteTrafficDetail?
        ): TrafficDetail {
            val trafficEntity = traffic.traffic
            var isFavorite = false
            var viewName: String = trafficEntity.name

            if (favoriteTrafficDetail != null &&
                favoriteTrafficDetail.id == trafficEntity.id
            ) {
                isFavorite = true
                viewName = favoriteTrafficDetail.name
            }

            return TrafficDetail(
                id = trafficEntity.id,
                color = traffic.currentColor.toString(),
                timeLeft = traffic.currentTimeLeft!!,
                point = PointDetail(trafficEntity.point.y, trafficEntity.point.x),
                redCycle = traffic.redCycle!!,
                greenCycle = traffic.greenCycle!!,
                detail = convertToTrafficDetailInfo(trafficEntity),
                isFavorite = isFavorite,
                viewName = viewName
            )
        }
        private fun convertToTrafficDetailInfo(trafficEntity: TargetTrafficVO): TrafficDetailInfo {
            val objectMapper = ObjectMapper()
            var trafficDetailInfo = TrafficDetailInfo(-1L, "ERROR", "ERROR")
            trafficDetailInfo = try {
                val values = objectMapper.readValue(
                    trafficEntity.detail,
                    HashMap::class.java
                )
                TrafficDetailInfo(
                    values["trafficId"] as Int,
                    values["apiSource"] as String,
                    values["direction"] as String
                )
            } catch (e: JsonMappingException) {
                throw RuntimeException("Convert to TrafficDetailInfo fail", e)
            } catch (e: JsonProcessingException) {
                throw RuntimeException("Convert to TrafficDetailInfo fail", e)
            }
            return trafficDetailInfo
        }
    }
}