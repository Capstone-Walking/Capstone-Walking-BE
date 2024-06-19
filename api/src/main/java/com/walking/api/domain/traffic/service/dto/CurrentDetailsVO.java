package com.walking.api.domain.traffic.service.dto;

import com.walking.api.domain.traffic.service.model.PredictedTraffic;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentDetailsVO {

	Map<Long, PredictedTraffic> currentDetails;
}
