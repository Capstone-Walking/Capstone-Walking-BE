package com.walking.api.domain.path.usecase;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.path.dto.DeleteFavoriteRouteUseCaseIn;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true, transactionManager = TRANSACTION_MANAGER_NAME)
public class DeleteFavoriteRouteUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;

	public void execute(DeleteFavoriteRouteUseCaseIn in) {
		pathFavoritesRepository.deleteByMemberFkAndId(
				MemberEntity.builder().id(in.getMemberId()).build(), in.getPathId());
	}
}
