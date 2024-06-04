package com.walking.api.web.client.dto.response;

import com.walking.api.web.client.dto.response.detail.Feature;
import java.util.List;
import lombok.*;

@Data
@ToString
public class TMapResponseDto {

	private String type;
	private List<Feature> features;
}
