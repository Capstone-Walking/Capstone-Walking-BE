package com.walking.api.domain.path.usecase;

import com.walking.api.data.entity.member.MemberEntity;
import com.walking.api.domain.path.dto.DeleteFavoriteRouteUseCaseIn;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DeleteFavoriteRouteUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;

	public void execute(DeleteFavoriteRouteUseCaseIn in) {
		pathFavoritesRepository.deleteByMemberFkAndId(
				MemberEntity.builder().id(in.getMemberId()).build(), in.getPathId());
	}
}
