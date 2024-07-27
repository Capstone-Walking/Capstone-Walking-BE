package com.walking.api.traffic.usecase

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
import com.walking.api.repository.dao.traffic.TrafficRepository
import com.walking.api.traffic.dto.BrowseFavoriteTrafficsUseCaseIn
import com.walking.api.traffic.dto.BrowseFavoriteTrafficsUseCaseOut
import com.walking.api.traffic.dto.detail.FavoriteTrafficDetail
import com.walking.api.traffic.dto.detail.PointDetail
import com.walking.api.traffic.dto.detail.TrafficDetailInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrowseFavoriteTrafficsUseCase(
    private val trafficFavoritesRepository: TrafficFavoritesRepository,
    private val trafficRepository: TrafficRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun execute(request: BrowseFavoriteTrafficsUseCaseIn): BrowseFavoriteTrafficsUseCaseOut {
        val trafficFavorites = trafficFavoritesRepository.findByMemberFkAndDeletedFalse(request.memberId)
        val details: MutableList<FavoriteTrafficDetail> = ArrayList()
        for (entity in trafficFavorites) {
            var detailInfo: TrafficDetailInfo? = null
            val traffic = trafficRepository.findById(entity.trafficFk).orElseThrow()
            detailInfo = try {
                traffic.detail?.let {
                    objectMapper.readValue(
                        it,
                        TrafficDetailInfo::class.java
                    )
                }
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
            val point = traffic.point
            details.add(
                FavoriteTrafficDetail(
                    entity.id,
                    detailInfo!!,
                    entity.alias,
                    PointDetail(point.y, point.x),
                    entity.createdAt
                )

            )
        }
        return BrowseFavoriteTrafficsUseCaseOut(details)
    }
}