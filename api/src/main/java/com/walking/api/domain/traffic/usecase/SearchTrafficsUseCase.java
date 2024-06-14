package com.walking.api.domain.traffic.usecase;

import com.walking.api.converter.TrafficDetailConverter;
import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseRequest;
import com.walking.api.domain.traffic.dto.SearchTrafficsUseCaseResponse;
import com.walking.api.domain.traffic.service.TrafficPredictService;
import com.walking.api.domain.traffic.service.dto.TrafficPredictServiceRequest;
import com.walking.api.domain.traffic.service.model.PredictedData;
import com.walking.api.repository.dao.traffic.TrafficRepository;
import com.walking.api.web.dto.response.detail.TrafficDetail;
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

	@Transactional
	public SearchTrafficsUseCaseResponse execute(SearchTrafficsUseCaseRequest request) {
		final Float vblLng = request.getVblLng();
		final Float vblLat = request.getVblLat();
		final Float vtrLng = request.getVtrLng();
		final Float vtrLat = request.getVtrLat();

		List<Long> inBoundsTrafficIds =
				trafficRepository.findTrafficWithinBounds(vblLng, vblLat, vtrLng, vtrLat).stream()
						.map(BaseEntity::getId)
						.collect(Collectors.toList());

		TrafficPredictServiceRequest predictRequest =
				TrafficPredictServiceRequest.builder().trafficIds(inBoundsTrafficIds).build();
		List<PredictedData> predictedData =
				new ArrayList<>(trafficPredictService.execute(predictRequest).getPredictedData().values());

		List<TrafficDetail> trafficDetails = TrafficDetailConverter.execute(predictedData);
		return SearchTrafficsUseCaseResponse.builder().traffics(trafficDetails).build();
	}
}
