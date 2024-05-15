package com.walking.api.web.controller.path;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.ApiApp;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = ApiApp.class)
@Slf4j
class PathControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	private static final String TAG = "PathControllerTest";
	private static final String BASE_URL = "/api/v1/paths";

	@Test
	@Rollback(value = false)
	@DisplayName("즐겨찾기 경로를 추가한다.")
	void addFavoriteRoute() throws Exception {
		FavoritePathBody favoritePathBody =
				FavoritePathBody.builder()
						.name("test")
						.startAlias("12")
						.endAlias("12")
						.startLat(33.5662952)
						.startLng(124.9779451)
						.endLat(33.5662952)
						.endLng(124.9779451)
						.build();

		String content = objectMapper.writeValueAsString(favoritePathBody);

		mockMvc
				.perform(
						post(BASE_URL + "/favorite")
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"addFavoriteRoute",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 추가")
												.tag(TAG)
												.requestSchema(Schema.schema("AddFavoriteRouteRequest"))
												//
												// .requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("AddFavoriteRouteResponse"))
												//
												// .responseFields(Description.common())
												.build())));
	}

	@Test
	@DisplayName("즐겨찾기 경로를 검색한다.")
	void getFavoriteRoute() throws Exception {

		MvcResult result =
				mockMvc
						.perform(
								get(BASE_URL + "/favorite?filter=order")
										.param("filter", "name")
										.header("Authorization", "Bearer {{accessToken}}"))
						.andDo(print())
						.andExpect(status().is2xxSuccessful())
						.andReturn();

		// response 데이터 변환
		Map<String, String> responseMap =
				new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
	}
}
