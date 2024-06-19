package com.walking.api.domain.client;

import com.walking.api.domain.client.dto.request.TMapRequestDto;
import com.walking.api.domain.client.dto.response.TMapResponseDto;
import com.walking.api.domain.client.util.WebClientUtil;
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

	public TMapResponseDto searchPath(TMapRequestDto requestDto) {
		Map<String, String> headers = new HashMap<>();
		headers.put("accept", " application/json");
		headers.put("content-type", " application/json");
		headers.put("appKey", apiKey);

		return webClientUtil.postWithHeaders(url, requestDto, TMapResponseDto.class, headers);
	}
}
