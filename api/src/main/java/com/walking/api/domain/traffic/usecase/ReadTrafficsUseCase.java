package com.walking.api.domain.traffic.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.domain.traffic.dto.BrowseTrafficsUseCaseIn;
import com.walking.api.domain.traffic.dto.BrowseTrafficsUseCaseOut;
import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetailInfo;
import com.walking.api.domain.traffic.service.TrafficPredictService;
import com.walking.api.domain.traffic.service.dto.TPVO;
import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadTrafficsUseCase {

	private final TrafficFavoritesRepository trafficFavoritesRepository;

	private final ObjectMapper objectMapper;

	private final TrafficPredictService trafficPredictService;

	public BrowseTrafficsUseCaseOut execute(BrowseTrafficsUseCaseIn request) {
		final Long trafficId = request.getTrafficId();
		final Long memberId = request.getMemberId();

		TPVO trafficPredictServiceVO = trafficPredictService.execute(List.of(trafficId));
		PredictedTraffic predictedTraffic = trafficPredictServiceVO.getPredictedData().get(trafficId);

		Optional<FavoriteTrafficDetail> favoriteTrafficDetail = Optional.empty();
		if (memberId != -1) {
			Optional<FavoriteTrafficDetail> memberFavoriteTrafficDetail =
					getFavoriteTrafficDetail(memberId, trafficId);
			if (memberFavoriteTrafficDetail.isPresent()) {
				favoriteTrafficDetail = memberFavoriteTrafficDetail;
			}
		}

		return new BrowseTrafficsUseCaseOut(predictedTraffic, favoriteTrafficDetail);
	}

	private Optional<FavoriteTrafficDetail> getFavoriteTrafficDetail(Long memberId, Long trafficId) {
		Optional<TrafficFavoritesEntity> favoriteTraffic =
				trafficFavoritesRepository.findByIdAndMemberFkAndDeletedFalse(
						trafficId, MemberEntity.builder().id(memberId).build());
		if (favoriteTraffic.isEmpty()) {
			return Optional.empty();
		}

		TrafficDetailInfo detailInfo = null;
		try {
			detailInfo =
					objectMapper.readValue(
							favoriteTraffic.get().getTrafficFk().getDetail(), TrafficDetailInfo.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		Point point = favoriteTraffic.get().getTrafficFk().getPoint();
		return Optional.of(
				FavoriteTrafficDetail.builder()
						.id(favoriteTraffic.get().getId())
						.detail(detailInfo)
						.name(favoriteTraffic.get().getAlias())
						.point(PointDetail.builder().lat(point.getY()).lng(point.getX()).build())
						.createdAt(favoriteTraffic.get().getCreatedAt())
						.build());
	}
}
