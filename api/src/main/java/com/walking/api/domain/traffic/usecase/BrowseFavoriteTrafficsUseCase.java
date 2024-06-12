package com.walking.api.domain.traffic.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.domain.traffic.dto.BrowseFavoriteTrafficsUseCaseRequest;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.api.web.dto.response.BrowseFavoriteTrafficsResponse;
import com.walking.api.web.dto.response.detail.FavoriteTrafficDetail;
import com.walking.api.web.dto.response.detail.PointDetail;
import com.walking.api.web.dto.response.detail.TrafficDetailInfo;
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

	@Transactional
	public BrowseFavoriteTrafficsResponse execute(BrowseFavoriteTrafficsUseCaseRequest request) {
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

		return BrowseFavoriteTrafficsResponse.builder().traffics(details).build();
	}
}
