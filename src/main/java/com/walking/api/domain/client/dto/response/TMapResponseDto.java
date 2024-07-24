package com.walking.api.domain.client.dto.response;

import com.walking.api.domain.client.dto.response.detail.FeatureDetail;
import java.util.List;
import lombok.*;

@Data
@ToString
public class TMapResponseDto {

	private String type;
	private List<FeatureDetail> featureDetails;
}
