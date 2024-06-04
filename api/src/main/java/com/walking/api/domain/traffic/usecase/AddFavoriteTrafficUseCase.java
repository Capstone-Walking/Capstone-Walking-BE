package com.walking.api.domain.traffic.usecase;

import com.walking.api.domain.traffic.dto.AddFavoriteTrafficUseCaseRequest;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddFavoriteTrafficUseCase {

	private final TrafficFavoritesRepository trafficFavoritesRepository;

	@Transactional
	public boolean execute(AddFavoriteTrafficUseCaseRequest request) {
		TrafficFavoritesEntity entity =
				TrafficFavoritesEntity.builder()
						.memberFk(MemberEntity.builder().id(request.getMemberId()).build())
						.trafficFk(TrafficEntity.builder().id(request.getTrafficId()).build())
						.alias(request.getTrafficAlias())
						.build();

		trafficFavoritesRepository.save(entity);
		return true;
	}
}
