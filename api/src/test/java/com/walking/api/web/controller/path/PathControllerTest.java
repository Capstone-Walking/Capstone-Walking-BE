package com.walking.api.web.controller.path;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.api.ApiApp;
import com.walking.api.web.controller.description.Description;
import com.walking.api.web.dto.request.path.FavoritePathBody;
import com.walking.api.web.dto.request.path.PatchFavoritePathNameBody;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = ApiApp.class)
class PathControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	private static final String TAG = "PathControllerTest";
	private static final String BASE_URL = "/api/v1/paths";

	@Test
	@DisplayName("GET /detail 상세 경로를 조회한다.")
	void detailRoute() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL + "/detail")
								.param("startLat", "37.5662952")
								.param("startLng", "126.9779451")
								.param("endLat", "37.5662952")
								.param("endLng", "126.9779451")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"detailRoute",
								resource(
										ResourceSnippetParameters.builder()
												.description("상세 경로 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("PathDetailRequest"))
												.requestParameters(
														List.of(
																new ParameterDescriptorWithType("startLat")
																		.type(SimpleType.NUMBER)
																		.description("출발지 위도"),
																new ParameterDescriptorWithType("startLng")
																		.type(SimpleType.NUMBER)
																		.description("출발지 경도"),
																new ParameterDescriptorWithType("endLat")
																		.type(SimpleType.NUMBER)
																		.description("도착지 위도"),
																new ParameterDescriptorWithType("endLng")
																		.type(SimpleType.NUMBER)
																		.description("도착지 경도")))
												.responseSchema(Schema.schema("PathDetailResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.totalTime")
																			.type(JsonFieldType.NUMBER)
																			.description("총 소요 시간"),
																	fieldWithPath("data.trafficCount")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 개수"),
																	fieldWithPath("data.startPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("출발지"),
																	fieldWithPath("data.startPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 위도"),
																	fieldWithPath("data.startPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 경도"),
																	fieldWithPath("data.endPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("도착지"),
																	fieldWithPath("data.endPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 위도"),
																	fieldWithPath("data.endPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 경도"),
																	fieldWithPath("data.traffics")
																			.type(JsonFieldType.ARRAY)
																			.description("신호등 목록"),
																	fieldWithPath("data.traffics[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffics[].detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].isFavorite")
																			.type(JsonFieldType.BOOLEAN)
																			.description("즐겨찾기 여부"),
																	fieldWithPath("data.traffics[].viewName")
																			.type(JsonFieldType.STRING)
																			.description("신호등 이름"),
																	fieldWithPath("data.traffics[].point")
																			.type(JsonFieldType.OBJECT)
																			.description("위치"),
																	fieldWithPath("data.traffics[].point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("위도"),
																	fieldWithPath("data.traffics[].point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("경도"),
																	fieldWithPath("data.traffics[].color")
																			.type(JsonFieldType.STRING)
																			.description("색상"),
																	fieldWithPath("data.traffics[].timeLeft")
																			.type(JsonFieldType.NUMBER)
																			.description("남은 시간"),
																	fieldWithPath("data.traffics[].redCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("빨간불 사이클"),
																	fieldWithPath("data.traffics[].greenCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("초록불 사이클"),
																	fieldWithPath("data.paths")
																			.type(JsonFieldType.ARRAY)
																			.description("경로"),
																	fieldWithPath("data.paths[].lat")
																			.type(JsonFieldType.NUMBER)
																			.description("위도"),
																	fieldWithPath("data.paths[].lng")
																			.type(JsonFieldType.NUMBER)
																			.description("경도")
																}))
												.build())));
	}

	@Test
	@DisplayName("POST /favorite 즐겨찾기 경로를 추가한다.")
	void addFavoriteRoute() throws Exception {
		FavoritePathBody favoritePathBody =
				FavoritePathBody.builder()
						.name("test")
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
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("AddFavoriteRouteResponse"))
												.responseFields(Description.common())
												.build())));
	}

	@Test
	@DisplayName("GET /favorite 즐겨찾기 경로 목록을 조회한다. - 필터링 적용")
	void browseFavoriteRouteFilter() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL + "/favorite")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}")
								.param("filter", "createdAt"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"browseFavoriteRouteFilter",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 목록 조회 - 필터링 적용")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseFavoriteRouteFilterRequest"))
												.requestHeaders(Description.authHeader())
												.requestParameters(
														new ParameterDescriptorWithType("filter")
																.type(SimpleType.STRING)
																.description("정렬 기준")
																.optional()
																.defaultValue("createdAt"))
												.responseSchema(Schema.schema("BrowseFavoriteRouteFilterResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.favoriteRoutes")
																			.type(JsonFieldType.ARRAY)
																			.description("즐겨찾기 경로 목록"),
																	fieldWithPath("data.favoriteRoutes[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("즐겨찾기 경로 id"),
																	fieldWithPath("data.favoriteRoutes[].name")
																			.type(JsonFieldType.STRING)
																			.description("즐겨찾기 경로 이름"),
																	fieldWithPath("data.favoriteRoutes[].startPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("출발지"),
																	fieldWithPath("data.favoriteRoutes[].startPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 위도"),
																	fieldWithPath("data.favoriteRoutes[].startPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 경도"),
																	fieldWithPath("data.favoriteRoutes[].endPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("도착지"),
																	fieldWithPath("data.favoriteRoutes[].endPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 위도"),
																	fieldWithPath("data.favoriteRoutes[].endPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 경도"),
																	fieldWithPath("data.favoriteRoutes[].createdAt")
																			.type(JsonFieldType.STRING)
																			.description("생성일")
																}))
												.build())));
	}

	@Test
	@DisplayName("GET /favorite 즐겨찾기 경로 목록을 조회한다. - 이름 검색")
	void browseFavoriteRouteName() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL + "/favorite")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}")
								.param("name", "name"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"browseFavoriteRouteName",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 목록 조회 - 이름 검색")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseFavoriteRouteNameRequest"))
												.requestHeaders(Description.authHeader())
												.requestParameters(
														new ParameterDescriptorWithType("name")
																.type(SimpleType.STRING)
																.description("검색할 이름")
																.optional())
												.responseSchema(Schema.schema("BrowseFavoriteRouteNameResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.favoriteRoutes")
																			.type(JsonFieldType.ARRAY)
																			.description("즐겨찾기 경로 목록"),
																	fieldWithPath("data.favoriteRoutes[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("즐겨찾기 경로 id"),
																	fieldWithPath("data.favoriteRoutes[].name")
																			.type(JsonFieldType.STRING)
																			.description("즐겨찾기 경로 이름"),
																	fieldWithPath("data.favoriteRoutes[].startPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("출발지"),
																	fieldWithPath("data.favoriteRoutes[].startPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 위도"),
																	fieldWithPath("data.favoriteRoutes[].startPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 경도"),
																	fieldWithPath("data.favoriteRoutes[].endPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("도착지"),
																	fieldWithPath("data.favoriteRoutes[].endPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 위도"),
																	fieldWithPath("data.favoriteRoutes[].endPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 경도"),
																	fieldWithPath("data.favoriteRoutes[].createdAt")
																			.type(JsonFieldType.STRING)
																			.description("생성일")
																}))
												.build())));
	}

	@Test
	@DisplayName("GET /favorite/{favoriteId} 즐겨찾기 경로를 조회한다.")
	void detailFavoriteRoute() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL + "/favorite/{favoriteId}", 1)
								.param("favoriteId", "1")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"detailFavoriteRoute",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 상세 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("DetailFavoriteRouteRequest"))
												.requestHeaders(Description.authHeader())
												.requestParameters(
														new ParameterDescriptorWithType("favoriteId")
																.type(SimpleType.NUMBER)
																.description("즐겨찾기 경로 id"))
												.responseSchema(Schema.schema("DetailFavoriteRouteResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.totalTime")
																			.type(JsonFieldType.NUMBER)
																			.description("총 소요 시간"),
																	fieldWithPath("data.trafficCount")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 개수"),
																	fieldWithPath("data.startPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("출발지"),
																	fieldWithPath("data.startPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 위도"),
																	fieldWithPath("data.startPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("출발지 경도"),
																	fieldWithPath("data.endPoint")
																			.type(JsonFieldType.OBJECT)
																			.description("도착지"),
																	fieldWithPath("data.endPoint.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 위도"),
																	fieldWithPath("data.endPoint.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("도착지 경도"),
																	fieldWithPath("data.traffics")
																			.type(JsonFieldType.ARRAY)
																			.description("신호등 목록"),
																	fieldWithPath("data.traffics[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffics[].detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].isFavorite")
																			.type(JsonFieldType.BOOLEAN)
																			.description("즐겨찾기 여부"),
																	fieldWithPath("data.traffics[].viewName")
																			.type(JsonFieldType.STRING)
																			.description("신호등 이름"),
																	fieldWithPath("data.traffics[].point")
																			.type(JsonFieldType.OBJECT)
																			.description("위치"),
																	fieldWithPath("data.traffics[].point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("위도"),
																	fieldWithPath("data.traffics[].point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("경도"),
																	fieldWithPath("data.traffics[].color")
																			.type(JsonFieldType.STRING)
																			.description("색상"),
																	fieldWithPath("data.traffics[].timeLeft")
																			.type(JsonFieldType.NUMBER)
																			.description("남은 시간"),
																	fieldWithPath("data.traffics[].redCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("빨간불 사이클"),
																	fieldWithPath("data.traffics[].greenCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("초록불 사이클"),
																	fieldWithPath("data.paths")
																			.type(JsonFieldType.ARRAY)
																			.description("경로"),
																	fieldWithPath("data.paths[].lat")
																			.type(JsonFieldType.NUMBER)
																			.description("위도"),
																	fieldWithPath("data.paths[].lng")
																			.type(JsonFieldType.NUMBER)
																			.description("경도")
																}))
												.build())));
	}

	@Test
	@DisplayName("PATCH /favorite/{favoriteId} 즐겨찾기 경로를 수정한다 - 이름 수정")
	void updateFavoriteRoute() throws Exception {
		PatchFavoritePathNameBody pathNameBody =
				PatchFavoritePathNameBody.builder().name("test").build();

		String content = objectMapper.writeValueAsString(pathNameBody);

		mockMvc
				.perform(
						patch(BASE_URL + "/favorite/{favoriteId}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.param("favoriteId", "1")
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"updateFavoriteRoute",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 이름 수정")
												.tag(TAG)
												.requestSchema(Schema.schema("UpdateFavoriteRouteRequest"))
												.requestHeaders(Description.authHeader())
												.requestParameters(
														new ParameterDescriptorWithType("favoriteId")
																.type(SimpleType.NUMBER)
																.description("즐겨찾기 경로 id"))
												.responseSchema(Schema.schema("UpdateFavoriteRouteResponse"))
												.responseFields(Description.common())
												.build())));
	}

	@Test
	@DisplayName("DELETE /favorite/{favoriteId} 즐겨찾기 경로를 삭제한다.")
	void deleteFavoriteRoute() throws Exception {
		mockMvc
				.perform(
						delete(BASE_URL + "/favorite/{favoriteId}", 1)
								.param("favoriteId", "1")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"deleteFavoriteRoute",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 경로 삭제")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteFavoriteRouteRequest"))
												.requestHeaders(Description.authHeader())
												.requestParameters(
														new ParameterDescriptorWithType("favoriteId")
																.type(SimpleType.NUMBER)
																.description("즐겨찾기 경로 id"))
												.responseSchema(Schema.schema("DeleteFavoriteRouteResponse"))
												.responseFields(Description.common())
												.build())));
	}
}
