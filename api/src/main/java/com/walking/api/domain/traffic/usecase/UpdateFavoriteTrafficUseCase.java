package com.walking.api.domain.traffic.usecase;

import com.walking.api.domain.traffic.dto.UpdateFavoriteTrafficUseCaseIn;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateFavoriteTrafficUseCase {

	private final TrafficFavoritesRepository trafficFavoritesRepository;

	@Transactional
	public boolean execute(UpdateFavoriteTrafficUseCaseIn request) {
		TrafficFavoritesEntity favoriteTraffic =
				trafficFavoritesRepository
						.findByIdAndDeletedFalse(request.getFavoriteTrafficId())
						.orElseThrow(() -> new RuntimeException("TrafficFavoritesEntity not found"));

		TrafficFavoritesEntity updatedAlias = favoriteTraffic.updateAlias(request.getTrafficAlias());
		trafficFavoritesRepository.save(updatedAlias);
		return true;
	}
}
