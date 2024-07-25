package com.walking.api.traffic.usecase

import com.walking.api.data.entity.member.TrafficFavoritesEntity
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
import com.walking.api.traffic.dto.AddFavoriteTrafficUseCaseIn
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddFavoriteTrafficUseCase(private val trafficFavoritesRepository: TrafficFavoritesRepository) {
    @Transactional
    fun execute(request: AddFavoriteTrafficUseCaseIn): Boolean {
        val entity = TrafficFavoritesEntity(
            request.memberId,
            request.trafficId,
            request.trafficAlias
        )
        trafficFavoritesRepository.save(entity)
        return true
    }

    companion object {
        private val log = LoggerFactory.getLogger(
            AddFavoriteTrafficUseCase::class.java
        )
    }
}