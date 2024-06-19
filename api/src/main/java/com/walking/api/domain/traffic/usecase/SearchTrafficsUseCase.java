package com.walking.api.domain.traffic.usecase;

import static com.walking.api.repository.config.ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME;

import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseIn;
import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseOut;
import com.walking.api.domain.traffic.dto.detail.TrafficDetail;
import com.walking.api.domain.traffic.service.TrafficPredictService;
import com.walking.api.domain.traffic.service.dto.TPQuery;
import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import com.walking.api.repository.dao.traffic.TrafficRepository;
import com.walking.api.web.dto.support.TrafficDetailConverter;
import com.walking.data.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchTrafficsUseCase {

	private final TrafficRepository trafficRepository;

	private final TrafficPredictService trafficPredictService;

	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME)
	public SearchTrafficsUseCaseOut execute(SearchTrafficsUseCaseIn request) {
		final Float vblLng = request.getVblLng();
		final Float vblLat = request.getVblLat();
		final Float vtrLng = request.getVtrLng();
		final Float vtrLat = request.getVtrLat();

		List<Long> inBoundsTrafficIds =
				trafficRepository.findTrafficWithinBounds(vblLng, vblLat, vtrLng, vtrLat).stream()
						.map(BaseEntity::getId)
						.collect(Collectors.toList());

		TPQuery predictRequest = TPQuery.builder().trafficIds(inBoundsTrafficIds).build();
		List<PredictedTraffic> predictedData =
				new ArrayList<>(trafficPredictService.execute(predictRequest).getPredictedData().values());

		List<TrafficDetail> trafficDetails = TrafficDetailConverter.execute(predictedData);
		return SearchTrafficsUseCaseOut.builder().traffics(trafficDetails).build();
	}
}
