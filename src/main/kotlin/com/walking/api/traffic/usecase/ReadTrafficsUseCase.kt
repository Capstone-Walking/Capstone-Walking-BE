package com.walking.api.traffic.usecase

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.walking.api.data.entity.member.MemberEntity
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
import com.walking.api.traffic.dto.BrowseTrafficsUseCaseIn
import com.walking.api.traffic.dto.BrowseTrafficsUseCaseOut
import com.walking.api.traffic.dto.detail.FavoriteTrafficDetail
import com.walking.api.traffic.dto.detail.PointDetail
import com.walking.api.traffic.dto.detail.TrafficDetailInfo
import com.walking.api.traffic.service.TrafficPredictService
import com.walking.api.traffic.service.dto.TPQuery
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReadTrafficsUseCase(
    private val trafficFavoritesRepository: TrafficFavoritesRepository,
    private val objectMapper: ObjectMapper,
    private val trafficPredictService: TrafficPredictService
) {
    @Transactional
    fun execute(request: BrowseTrafficsUseCaseIn): BrowseTrafficsUseCaseOut {
        val trafficId = request.trafficId
        val memberId = request.memberId
        val trafficPredictServiceVO = trafficPredictService.execute(
            TPQuery(
                trafficIds = listOf(trafficId)
            )
        )
        val predictTargetTraffic = trafficPredictServiceVO.predictedData[trafficId]
        var favoriteTrafficDetail: Optional<FavoriteTrafficDetail> = Optional.empty()
        if (memberId != -1L) {
            val memberFavoriteTrafficDetail = getFavoriteTrafficDetail(memberId!!, trafficId)
            if (memberFavoriteTrafficDetail.isPresent) {
                favoriteTrafficDetail = memberFavoriteTrafficDetail
            }
        }
        return BrowseTrafficsUseCaseOut(predictTargetTraffic!!, favoriteTrafficDetail.orElse(null))
    }

    private fun getFavoriteTrafficDetail(
        memberId: Long,
        trafficId: Long
    ): Optional<FavoriteTrafficDetail> {
        val favoriteTraffic = trafficFavoritesRepository.findByIdAndMemberFkAndDeletedFalse(
            trafficId,
            MemberEntity(memberId)
        )
        if (favoriteTraffic.isEmpty) {
            return Optional.empty()
        }
        var detailInfo: TrafficDetailInfo? = null
        detailInfo = try {
            objectMapper.readValue(
                favoriteTraffic.get().trafficFk.detail,
                TrafficDetailInfo::class.java
            )
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        val point = favoriteTraffic.get().trafficFk.point
        return Optional.of(
            FavoriteTrafficDetail(
                favoriteTraffic.get().id,
                detailInfo!!,
                favoriteTraffic.get().alias,
                PointDetail(point.y, point.x),
                favoriteTraffic.get().createdAt
            )
        )
    }
}