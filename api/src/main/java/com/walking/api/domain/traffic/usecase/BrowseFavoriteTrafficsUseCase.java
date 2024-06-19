package com.walking.api.domain.traffic.usecase;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.domain.traffic.dto.BrowseFavoriteTrafficsUseCaseIn;
import com.walking.api.domain.traffic.dto.BrowseFavoriteTrafficsUseCaseOut;
import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetailInfo;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowseFavoriteTrafficsUseCase {

	private final TrafficFavoritesRepository trafficFavoritesRepository;
	private final ObjectMapper objectMapper;

	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME)
	public BrowseFavoriteTrafficsUseCaseOut execute(BrowseFavoriteTrafficsUseCaseIn request) {
		List<TrafficFavoritesEntity> trafficFavorites =
				trafficFavoritesRepository.findByMemberFkAndDeletedFalse(
						MemberEntity.builder().id(request.getMemberId()).build());

		List<FavoriteTrafficDetail> details = new ArrayList<>();
		for (TrafficFavoritesEntity entity : trafficFavorites) {
			TrafficDetailInfo detailInfo = null;
			try {
				detailInfo =
						objectMapper.readValue(entity.getTrafficFk().getDetail(), TrafficDetailInfo.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			Point point = entity.getTrafficFk().getPoint();
			details.add(
					FavoriteTrafficDetail.builder()
							.id(entity.getId())
							.detail(detailInfo)
							.name(entity.getAlias())
							.point(PointDetail.builder().lat(point.getY()).lng(point.getX()).build())
							.createdAt(entity.getCreatedAt())
							.build());
		}

		return BrowseFavoriteTrafficsUseCaseOut.builder().traffics(details).build();
	}
}
