package com.walking.api.traffic.dto

data class DeleteFavoriteTrafficUseCaseIn(
    val favoriteTrafficId: Long,
    val memberId: Long
)