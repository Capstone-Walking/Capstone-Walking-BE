package com.walking.api.web.client;

import com.walking.api.web.client.dto.request.TMapRequestDto;
import com.walking.api.web.client.dto.response.TMapResponseDto;
import com.walking.api.web.client.webclient.WebClientUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TMapClient implements RestClient {

	private final WebClientUtil webClientUtil;

	private final String url =
			"https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=function";
	private final String apiKey = "4ftEOGkWCl4ChZ4K8Z5OG3pn8i0yRcDD7b73tqY5";

	public TMapResponseDto TMapDetailPathSearch(TMapRequestDto requestDto) {
		Map<String, String> Headers = new HashMap<>();
		Headers.put("accept", " application/json");
		Headers.put("content-type", " application/json");
		Headers.put("appKey", apiKey);

		return webClientUtil.postWithHeaders(url, requestDto, TMapResponseDto.class, Headers);
	}
}
