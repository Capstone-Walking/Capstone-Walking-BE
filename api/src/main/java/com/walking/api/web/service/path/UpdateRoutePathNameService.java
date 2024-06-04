package com.walking.api.web.service.path;

import com.walking.api.repository.member.PathFavoritesRepository;
import com.walking.api.web.service.path.dto.request.PathFavoriteNameRequest;
import com.walking.data.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateRoutePathNameService {

	private final PathFavoritesRepository pathFavoritesRepository;

	@Transactional
	public void execute(Long memberId, Long pathId, PathFavoriteNameRequest pathFavoriteNameRequest) {

		pathFavoritesRepository.updatePathName(
				MemberEntity.builder().id(memberId).build(),
				pathId,
				pathFavoriteNameRequest.getName(),
				pathFavoriteNameRequest.getStartAlias(),
				pathFavoriteNameRequest.getEndAlias());
	}
}
