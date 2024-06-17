package com.walking.api.domain.traffic.dto;

import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetail;
import com.walking.api.domain.traffic.service.model.PredictedData;
import com.walking.api.web.dto.support.TrafficDetailConverter;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BrowseTrafficsUseCaseResponse {

	private TrafficDetail traffic;

	public BrowseTrafficsUseCaseResponse(
			PredictedData predictedData, Optional<FavoriteTrafficDetail> favoriteTrafficDetail) {
		this.traffic = TrafficDetailConverter.execute(predictedData, favoriteTrafficDetail);
	}
}
