package com.walking.api.traffic.usecase

import com.walking.api.traffic.dto.UpdateFavoriteTrafficUseCaseIn
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateFavoriteTrafficUseCase(private val trafficFavoritesRepository: TrafficFavoritesRepository) {
    @Transactional
    fun execute(request: UpdateFavoriteTrafficUseCaseIn): Boolean {
        val favoriteTraffic = trafficFavoritesRepository
            .findByIdAndDeletedFalse(request.favoriteTrafficId)
            .orElseThrow { RuntimeException("TrafficFavoritesEntity not found") }
        val updatedAlias = favoriteTraffic.updateAlias(
            request.trafficAlias
        )
        trafficFavoritesRepository.save(updatedAlias)
        return true
    }

    companion object {
        private val log = LoggerFactory.getLogger(
            UpdateFavoriteTrafficUseCase::class.java
        )
    }
}