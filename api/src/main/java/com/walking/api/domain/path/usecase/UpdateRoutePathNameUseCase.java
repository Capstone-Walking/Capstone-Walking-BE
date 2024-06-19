package com.walking.api.domain.path.usecase;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.path.dto.UpdateRoutePathNameUseCaseIn;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateRoutePathNameUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;

	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME)
	public void execute(UpdateRoutePathNameUseCaseIn in) {
		pathFavoritesRepository.updatePathName(
				MemberEntity.builder().id(in.getMemberId()).build(),
				in.getPathId(),
				in.getName(),
				in.getStartAlias(),
				in.getEndAlias());
	}
}
