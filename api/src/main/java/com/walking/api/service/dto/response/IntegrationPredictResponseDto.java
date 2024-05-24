package com.walking.api.service.dto.response;

import com.walking.api.service.dto.PredictedData;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntegrationPredictResponseDto {

	/** Key: 신호등 아이디, Value: 예측된 데이터 */
	private Map<Long, PredictedData> predictedDataMap;
}
