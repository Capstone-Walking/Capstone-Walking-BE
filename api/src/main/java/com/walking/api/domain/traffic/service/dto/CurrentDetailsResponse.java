package com.walking.api.domain.traffic.service.dto;

import com.walking.api.domain.traffic.service.model.PredictedData;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentDetailsResponse {

	Map<Long, PredictedData> currentDetails;
}
