package com.walking.api.traffic.dto

data class AddFavoriteTrafficUseCaseIn(
    val memberId: Long,
    val trafficId: Long,
    val trafficAlias: String
)