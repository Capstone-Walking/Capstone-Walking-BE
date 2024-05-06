package com.walking.api.web.dto.response;

import com.walking.api.web.dto.response.detail.TrafficDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrowseTrafficsResponse {

	private TrafficDetail traffic;
}
