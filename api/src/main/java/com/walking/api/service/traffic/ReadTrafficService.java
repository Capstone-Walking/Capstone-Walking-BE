package com.walking.api.service.traffic;

import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadTrafficService {

	private final TrafficRepository trafficRepository;

	public TrafficEntity executeById(Long trafficId) {
		return trafficRepository.findById(trafficId).orElseThrow();
	}

	public List<TrafficEntity> executeByIds(List<Long> trafficIds) {
		return trafficRepository.findByIds(trafficIds);
	}

	public List<TrafficEntity> executeByLocationAndDistance(Float lat, Float lng, Integer distance) {
		return trafficRepository.findByLocationAndDistance(lat, lng, distance);
	}

	public List<TrafficEntity> executeWithinBounds(
			Float blLng, Float blLat, Float trLng, Float trLat) {
		return trafficRepository.findTrafficWithinBounds(blLng, blLat, trLng, trLat);
	}
}
