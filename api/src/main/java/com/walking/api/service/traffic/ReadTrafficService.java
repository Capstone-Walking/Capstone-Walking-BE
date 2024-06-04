package com.walking.api.service.traffic;

import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadTrafficService {

	private final TrafficRepository trafficRepository;

	@Transactional(readOnly = true)
	public TrafficEntity executeById(Long trafficId) {
		return trafficRepository.findById(trafficId).orElseThrow();
	}

	@Transactional(readOnly = true)
	public List<TrafficEntity> executeByIds(List<Long> trafficIds) {
		return trafficRepository.findByIds(trafficIds);
	}

	@Transactional(readOnly = true)
	public List<TrafficEntity> executeByLocationAndDistance(Float lat, Float lng, Integer distance) {
		return trafficRepository.findByLocationAndDistance(lat, lng, distance);
	}

	@Transactional(readOnly = true)
	public List<TrafficEntity> executeWithinBounds(
			Float blLng, Float blLat, Float trLng, Float trLat) {
		return trafficRepository.findTrafficWithinBounds(blLng, blLat, trLng, trLat);
	}
}
