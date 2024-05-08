package com.walking.batch.service;

import com.walking.batch.service.dto.TrafficColorDto;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/** 신호제어기 신호 정보 API 요청 */
@Service
public class TrafficColorApiService extends BaseApiService<TrafficColorDto> {

	private final String API_KEY = "f6675d12-8dff-42b8-b031-9e1cc55cf39a";
	private final String URL = "http://t-data.seoul.go.kr";
	private final String PATH = "/apig/apiman-gateway/tapi/v2xSignalPhaseInformation/1.0";

	@Override
	protected List<TrafficColorDto> doApiService(RestTemplate restTemplate) {
		URI uri =
				UriComponentsBuilder.fromUriString(URL)
						.path(PATH)
						.queryParam("apiKey", API_KEY)
						.encode()
						.build()
						.toUri();

		return Arrays.asList(restTemplate.getForObject(uri, TrafficColorDto[].class));
	}
}
