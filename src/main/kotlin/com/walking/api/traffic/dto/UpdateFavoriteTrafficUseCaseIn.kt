package com.walking.api.traffic.dto

data class UpdateFavoriteTrafficUseCaseIn(
    val memberId: Long,
    val favoriteTrafficId: Long,
    val trafficAlias: String
)