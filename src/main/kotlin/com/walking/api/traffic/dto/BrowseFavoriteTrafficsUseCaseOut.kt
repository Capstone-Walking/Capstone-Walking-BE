package com.walking.api.traffic.dto

import com.walking.api.traffic.dto.detail.FavoriteTrafficDetail

data class BrowseFavoriteTrafficsUseCaseOut(
    val traffics: List<FavoriteTrafficDetail>
)