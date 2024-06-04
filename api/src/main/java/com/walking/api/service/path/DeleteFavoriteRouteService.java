package com.walking.api.service.path;

import com.walking.api.repository.path.PathFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeleteFavoriteRouteService {

	private final PathFavoritesRepository pathFavoritesRepository;

	public void execute(Long memberId, Long pathId) {

		pathFavoritesRepository.deleteByMemberFkAndId(
				MemberEntity.builder().id(memberId).build(), pathId);
	}
}
