package com.walking.api.domain.path.usecase;

import com.walking.api.data.entity.member.MemberEntity;
import com.walking.api.domain.path.dto.UpdateRoutePathNameUseCaseIn;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UpdateRoutePathNameUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;

	@Transactional
	public void execute(UpdateRoutePathNameUseCaseIn in) {
		pathFavoritesRepository.updatePathName(
				MemberEntity.builder().id(in.getMemberId()).build(),
				in.getPathId(),
				in.getName(),
				in.getStartAlias(),
				in.getEndAlias());
	}
}
