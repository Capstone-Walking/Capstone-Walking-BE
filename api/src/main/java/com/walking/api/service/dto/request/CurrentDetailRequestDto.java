package com.walking.api.service.dto.request;

import com.walking.api.service.dto.PredictedData;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentDetailRequestDto {

	private Map<TrafficEntity, PredictedData> predictedCycleMap;
}
