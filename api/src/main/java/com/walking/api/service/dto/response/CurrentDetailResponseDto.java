package com.walking.api.service.dto.response;

import com.walking.api.service.dto.PredictedData;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentDetailResponseDto {

	Map<TrafficEntity, PredictedData> currentDetails;
}
