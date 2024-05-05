package com.walking.api.web.controller.traffic;

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
import com.walking.api.web.dto.request.traffic.FavoriteTrafficBody;
import com.walking.api.web.dto.request.traffic.PatchFavoriteTrafficNameBody;
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
class TrafficControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	private static final String TAG = "TrafficControllerTest";
	private static final String BASE_URL = "/api/v1/traffics";

	@Test
	@DisplayName("신호등 정보 조회 - 화면 좌표로 조회")
	void searchTrafficsWithViewPointParam() throws Exception {

		mockMvc
				.perform(
						get(BASE_URL)
								.contentType(MediaType.APPLICATION_JSON)
								.param("vblLat", "33.5662952")
								.param("vblLng", "124.9779451")
								.param("vtrLat", "33.5662952")
								.param("vtrLng", "124.9779451"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"searchTrafficsWithViewPointParam",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 정보 조회 - 화면 좌표로 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("SearchTrafficsWithViewPointParamRequest"))
												.requestParameters(
														new ParameterDescriptorWithType("vblLat")
																.type(SimpleType.NUMBER)
																.description("화면 좌측 위도"),
														new ParameterDescriptorWithType("vblLng")
																.type(SimpleType.NUMBER)
																.description("화면 좌측 경도"),
														new ParameterDescriptorWithType("vtrLat")
																.type(SimpleType.NUMBER)
																.description("화면 우측 위도"),
														new ParameterDescriptorWithType("vtrLng")
																.type(SimpleType.NUMBER)
																.description("화면 우측 경도"))
												.responseSchema(Schema.schema("SearchTrafficsWithViewPointParamResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.traffics")
																			.type(JsonFieldType.ARRAY)
																			.description("신호등 정보"),
																	fieldWithPath("data.traffics[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 ID"),
																	fieldWithPath("data.traffics[].detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].isFavorite")
																			.type(JsonFieldType.BOOLEAN)
																			.description("즐겨찾기 여부"),
																	fieldWithPath("data.traffics[].viewName")
																			.type(JsonFieldType.STRING)
																			.description("화면 이름"),
																	fieldWithPath("data.traffics[].point")
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 좌표"),
																	fieldWithPath("data.traffics[].point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 위도"),
																	fieldWithPath("data.traffics[].point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 경도"),
																	fieldWithPath("data.traffics[].color")
																			.type(JsonFieldType.STRING)
																			.description("신호등 색상"),
																	fieldWithPath("data.traffics[].timeLeft")
																			.type(JsonFieldType.NUMBER)
																			.description("남은 시간"),
																	fieldWithPath("data.traffics[].redCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("빨간불 주기"),
																	fieldWithPath("data.traffics[].greenCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("초록불 주기")
																}))
												.build())));
	}

	@Test
	@DisplayName("신호등 정보 조회 - 신호등 좌표로 조회")
	void searchTrafficsWithTrafficParam() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL)
								.contentType(MediaType.APPLICATION_JSON)
								.param("traLat", "33.5662952")
								.param("traLng", "124.9779451"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"searchTrafficsWithTrafficParam",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 정보 조회 - 신호등 좌표로 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("SearchTrafficsWithTrafficParamRequest"))
												.requestParameters(
														new ParameterDescriptorWithType("traLat")
																.type(SimpleType.NUMBER)
																.description("신호등 위도"),
														new ParameterDescriptorWithType("traLng")
																.type(SimpleType.NUMBER)
																.description("신호등 경도"))
												.responseSchema(Schema.schema("SearchTrafficsWithTrafficParamResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.traffics")
																			.type(JsonFieldType.ARRAY)
																			.description("신호등 정보"),
																	fieldWithPath("data.traffics[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 ID"),
																	fieldWithPath("data.traffics[].detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].isFavorite")
																			.type(JsonFieldType.BOOLEAN)
																			.description("즐겨찾기 여부"),
																	fieldWithPath("data.traffics[].viewName")
																			.type(JsonFieldType.STRING)
																			.description("화면 이름"),
																	fieldWithPath("data.traffics[].point")
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 좌표"),
																	fieldWithPath("data.traffics[].point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 위도"),
																	fieldWithPath("data.traffics[].point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 경도"),
																	fieldWithPath("data.traffics[].color")
																			.type(JsonFieldType.STRING)
																			.description("신호등 색상"),
																	fieldWithPath("data.traffics[].timeLeft")
																			.type(JsonFieldType.NUMBER)
																			.description("남은 시간"),
																	fieldWithPath("data.traffics[].redCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("빨간불 주기"),
																	fieldWithPath("data.traffics[].greenCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("초록불 주기")
																}))
												.build())));
	}

	@Test
	@DisplayName("신호등 정보 조회 - 신호등 ID로 조회")
	void browseTraffic() throws Exception {
		mockMvc
				.perform(get(BASE_URL + "/{trafficId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"browseTraffic",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 ID로 정보 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseTrafficRequest"))
												.pathParameters(
														new ParameterDescriptorWithType("trafficId")
																.type(SimpleType.NUMBER)
																.description("신호등 ID"))
												.responseSchema(Schema.schema("BrowseTrafficResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.traffic")
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 정보"),
																	fieldWithPath("data.traffic.id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 ID"),
																	fieldWithPath("data.traffic.detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffic.isFavorite")
																			.type(JsonFieldType.BOOLEAN)
																			.description("즐겨찾기 여부"),
																	fieldWithPath("data.traffic.viewName")
																			.type(JsonFieldType.STRING)
																			.description("화면 이름"),
																	fieldWithPath("data.traffic.point")
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 좌표"),
																	fieldWithPath("data.traffic.point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 위도"),
																	fieldWithPath("data.traffic.point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 경도"),
																	fieldWithPath("data.traffic.color")
																			.type(JsonFieldType.STRING)
																			.description("신호등 색상"),
																	fieldWithPath("data.traffic.timeLeft")
																			.type(JsonFieldType.NUMBER)
																			.description("남은 시간"),
																	fieldWithPath("data.traffic.redCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("빨간불 주기"),
																	fieldWithPath("data.traffic.greenCycle")
																			.type(JsonFieldType.NUMBER)
																			.description("초록불 주기")
																}))
												.build())));
	}

	@Test
	@DisplayName("신호등 즐겨찾기 추가")
	void addFavoriteTraffic() throws Exception {
		FavoriteTrafficBody body =
				FavoriteTrafficBody.builder().trafficId(1L).trafficAlias("alias1").build();
		String content = objectMapper.writeValueAsString(body);

		mockMvc
				.perform(
						post(BASE_URL + "/favorite")
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"addFavoriteTraffic",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 즐겨찾기 추가")
												.tag(TAG)
												.requestSchema(Schema.schema("AddFavoriteTrafficRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("AddFavoriteTrafficResponse"))
												.responseFields(Description.common())
												.build())));
	}

	@Test
	@DisplayName("즐겨찾기 신호등 조회")
	void browseFavoriteTraffics() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL + "/favorite")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"browseFavoriteTraffics",
								resource(
										ResourceSnippetParameters.builder()
												.description("즐겨찾기 신호등 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseFavoriteTrafficsRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("BrowseFavoriteTrafficsResponse"))
												.responseFields(
														Description.common(
																new FieldDescriptor[] {
																	fieldWithPath("data")
																			.type(JsonFieldType.OBJECT)
																			.description("데이터"),
																	fieldWithPath("data.traffics")
																			.type(JsonFieldType.ARRAY)
																			.description("즐겨찾기 신호등 정보"),
																	fieldWithPath("data.traffics[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 ID"),
																	fieldWithPath("data.traffics[].detail")
																			.type(JsonFieldType.STRING)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].name")
																			.type(JsonFieldType.STRING)
																			.description("신호등 별칭"),
																	fieldWithPath("data.traffics[].point")
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 좌표"),
																	fieldWithPath("data.traffics[].point.lat")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 위도"),
																	fieldWithPath("data.traffics[].point.lng")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 경도"),
																	fieldWithPath("data.traffics[].createdAt")
																			.type(JsonFieldType.STRING)
																			.description("생성일")
																}))
												.build())));
	}

	@Test
	@DisplayName("즐겨찾기 신호등 수정 - 별칭 수정")
	void updateFavoriteTraffic() throws Exception {
		PatchFavoriteTrafficNameBody body =
				PatchFavoriteTrafficNameBody.builder().trafficAlias("alias2").build();
		String content = objectMapper.writeValueAsString(body);

		mockMvc
				.perform(
						patch(BASE_URL + "/favorite/{favoriteId}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header("Authorization", "Bearer {{accessToken}}")
								.param("trafficId", "1"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"updateFavoriteTraffic",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 즐겨찾기 수정")
												.tag(TAG)
												.requestSchema(Schema.schema("UpdateFavoriteTrafficRequest"))
												.requestHeaders(Description.authHeader())
												.pathParameters(
														new ParameterDescriptorWithType("favoriteId")
																.type(SimpleType.NUMBER)
																.description("즐겨찾기 ID"))
												.responseSchema(Schema.schema("UpdateFavoriteTrafficResponse"))
												.responseFields(Description.common())
												.build())));
	}

	@Test
	@DisplayName("즐겨찾기 신호등 삭제")
	void deleteFavoriteTraffic() throws Exception {
		mockMvc
				.perform(
						delete(BASE_URL + "/favorite/{favoriteId}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer {{accessToken}}")
								.param("trafficId", "1"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"deleteFavoriteTraffic",
								resource(
										ResourceSnippetParameters.builder()
												.description("신호등 즐겨찾기 삭제")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteFavoriteTrafficRequest"))
												.requestHeaders(Description.authHeader())
												.pathParameters(
														new ParameterDescriptorWithType("favoriteId")
																.type(SimpleType.NUMBER)
																.description("즐겨찾기 ID"))
												.responseSchema(Schema.schema("DeleteFavoriteTrafficResponse"))
												.responseFields(Description.common())
												.build())));
	}
}
