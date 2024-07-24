package com.walking.api.traffic.usecase

import com.walking.api.data.entity.member.MemberEntity
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository
import com.walking.api.traffic.dto.DeleteFavoriteTrafficUseCaseIn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteFavoriteTrafficUseCase(private val trafficFavoritesRepository: TrafficFavoritesRepository) {
    @Transactional
    fun execute(request: DeleteFavoriteTrafficUseCaseIn): Boolean {
        val favoriteTraffic = trafficFavoritesRepository
            .findByIdAndMemberFkAndDeletedFalse(
                request.favoriteTrafficId,
                MemberEntity(request.memberId)
            )
            .orElseThrow { IllegalArgumentException("해당 즐겨찾기 정보가 존재하지 않습니다.") }
        trafficFavoritesRepository.delete(favoriteTraffic)
        return true
    }
}