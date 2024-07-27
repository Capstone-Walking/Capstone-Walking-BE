package com.walking.api.domain.path.usecase;

import com.walking.api.domain.path.dto.ReadFavoritesPathUseCaseIn;
import com.walking.api.domain.path.dto.ReadFavoritesPathUseCaseOut;
import com.walking.api.repository.dao.dto.response.PathFavoritesVo;
import com.walking.api.repository.dao.member.MemberRepository;
import com.walking.api.repository.dao.path.PathFavoritesRepository;
import com.walking.api.web.dto.request.OrderFilter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 데이터베이스에 저장된 멤버의 즐겨찾기 경로를 조회합니다. */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReadFavoritesPathUseCase {

	private final PathFavoritesRepository pathFavoritesRepository;
	// todo refactor service 클래스로 분리
	private final MemberRepository memberRepository;

	public List<ReadFavoritesPathUseCaseOut> execute(ReadFavoritesPathUseCaseIn in) {
		if (in.isOrderFiltered()) {
			return doExecute(in.getMemberId(), in.getOrderFilter());
		}
		List<PathFavoritesVo> pathFavorites =
				pathFavoritesRepository.findPathFavoritesByMemberFkAndFilterName(
						in.getMemberId(), in.getName());

		return mappedFavoritesPathOrder(pathFavorites);
	}

	private List<ReadFavoritesPathUseCaseOut> doExecute(Long memberId, OrderFilter orderFilter) {
		if (orderFilter == OrderFilter.NAME) {
			return mappedFavoritesPathOrder(
					pathFavoritesRepository.findPathFavoritesByMemberFkOrderByName(memberId));
		}
		if (orderFilter == OrderFilter.CREATEDAT) {
			return mappedFavoritesPathOrder(
					pathFavoritesRepository.findPathFavoritesByMemberFkOrderByCreatedAt(memberId));
		}

		if (orderFilter == OrderFilter.ORDER) {
			return mappedFavoritesPathOrder(
					pathFavoritesRepository.findPathFavoritesEntitiesByMemberFkOrderByOrderDesc(memberId));
		}

		throw new IllegalArgumentException("잘못된 OrderFilter입니다.");
	}

	private List<ReadFavoritesPathUseCaseOut> mappedFavoritesPathOrder(
			List<PathFavoritesVo> pathFavorites) {
		// 인덱스를 위한 AtomicInteger
		AtomicInteger index = new AtomicInteger();
		return pathFavorites.stream()
				.map(
						vo ->
								ReadFavoritesPathUseCaseOut.builder()
										.id(vo.getId())
										.startPoint((Point) vo.getStartPoint())
										.endPoint((Point) vo.getEndPoint())
										.startAlias(vo.getStartAlias())
										.endAlias(vo.getEndAlias())
										.name(vo.getName())
										.createdAt(vo.getCreatedAt())
										.order((long) index.getAndIncrement()) // 인덱스 값 사용 및 증가
										.build())
				.collect(Collectors.toList());
	}
}
