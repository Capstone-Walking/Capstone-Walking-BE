package com.walking.api.traffic.usecase

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.walking.api.data.entity.member.MemberEntity
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
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
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun execute(request: BrowseFavoriteTrafficsUseCaseIn): BrowseFavoriteTrafficsUseCaseOut {
        val trafficFavorites = trafficFavoritesRepository.findByMemberFkAndDeletedFalse(
            MemberEntity(request.memberId)
        )
        val details: MutableList<FavoriteTrafficDetail> = ArrayList()
        for (entity in trafficFavorites) {
            var detailInfo: TrafficDetailInfo? = null
            detailInfo = try {
                objectMapper.readValue(
                    entity.trafficFk.detail,
                    TrafficDetailInfo::class.java
                )
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
            val point = entity.trafficFk.point
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