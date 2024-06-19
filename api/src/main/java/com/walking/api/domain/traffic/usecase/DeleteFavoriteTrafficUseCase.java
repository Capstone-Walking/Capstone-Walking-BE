package com.walking.api.domain.traffic.usecase;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.traffic.dto.DeleteFavoriteTrafficUseCaseIn;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteFavoriteTrafficUseCase {
	private final TrafficFavoritesRepository trafficFavoritesRepository;

	@Transactional(value = TRANSACTION_MANAGER_NAME)
	public boolean execute(DeleteFavoriteTrafficUseCaseIn request) {
		TrafficFavoritesEntity favoriteTraffic =
				trafficFavoritesRepository
						.findByIdAndMemberFkAndDeletedFalse(
								request.getFavoriteTrafficId(),
								MemberEntity.builder().id(request.getMemberId()).build())
						.orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기 정보가 존재하지 않습니다."));
		trafficFavoritesRepository.delete(favoriteTraffic);
		return true;
	}
}
