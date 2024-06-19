package com.walking.api.web.controller.traffic;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import com.walking.api.domain.traffic.dto.BrowseFavoriteTrafficsUseCaseOut;
import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import com.walking.api.domain.traffic.dto.detail.PointDetail;
import com.walking.api.domain.traffic.dto.detail.TrafficDetailInfo;
import com.walking.api.domain.traffic.usecase.AddFavoriteTrafficUseCase;
import com.walking.api.domain.traffic.usecase.BrowseFavoriteTrafficsUseCase;
import com.walking.api.domain.traffic.usecase.DeleteFavoriteTrafficUseCase;
import com.walking.api.domain.traffic.usecase.UpdateFavoriteTrafficUseCase;
import com.walking.api.web.controller.description.Description;
import com.walking.api.web.dto.request.traffic.FavoriteTrafficBody;
import com.walking.api.web.dto.request.traffic.PatchFavoriteTrafficNameBody;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

	@MockBean AddFavoriteTrafficUseCase addFavoriteTrafficUseCase;
	@MockBean BrowseFavoriteTrafficsUseCase browseFavoriteTrafficsUseCase;
	@MockBean DeleteFavoriteTrafficUseCase deleteFavoriteTrafficUseCase;
	@MockBean UpdateFavoriteTrafficUseCase updateFavoriteTrafficUseCase;
	private static final String TAG = "TrafficControllerTest";
	private static final String BASE_URL = "/api/v1/traffics";

	static double TF_BACK_DOOR_LAT = 35.178501;
	static double TF_BACK_DOOR_LNG = 126.912083;
	static double TF_BACK_THREE_LAT = 35.175841;
	static double TF_BACK_THREE_LNG = 126.912491;
	static double TF_CHANPUNG_LAT = 35.180332;
	static double TF_CHANPUNG_LNG = 126.912123;
	static double TF_CUCU_LAT = 35.176495;
	static double TF_CUCU_LNG = 126.896888;

	@Test
	@DisplayName("GET 신호등 정보 조회 - 화면 좌표로 조회")
	void searchTrafficsWithViewPointParam() throws Exception {

		mockMvc
				.perform(
						get(BASE_URL)
								.contentType(MediaType.APPLICATION_JSON)
								.param("vblLat", "37.595000")
								.param("vblLng", "127.07870")
								.param("vtrLat", "37.605372")
								.param("vtrLng", "127.080309"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"searchTrafficsWithViewPointParam",
								resource(
										ResourceSnippetParameters.builder()
												.summary("신호등 정보 조회")
												.description(
														"vblLat, vblLng, vtrLat, vtrLng로 화면 좌표로 신호등 정보 조회 || traLat, traLng로 신호등 좌표로 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("SearchTrafficsWithViewPointParamRequest"))
												.requestParameters(
														new ParameterDescriptorWithType("vblLat")
																.type(SimpleType.NUMBER)
																.description("화면 좌측 하단 위도")
																.optional()
																.defaultValue(35.175840),
														new ParameterDescriptorWithType("vblLng")
																.type(SimpleType.NUMBER)
																.description("화면 좌측 하단 경도")
																.optional()
																.defaultValue(126.912490),
														new ParameterDescriptorWithType("vtrLat")
																.type(SimpleType.NUMBER)
																.description("화면 우측 상단 위도")
																.optional()
																.defaultValue(35.178526),
														new ParameterDescriptorWithType("vtrLng")
																.type(SimpleType.NUMBER)
																.description("화면 우측 상단 경도")
																.optional()
																.defaultValue(124.123457))
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
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].detail.trafficId")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffics[].detail.apiSource")
																			.type(JsonFieldType.STRING)
																			.description("신호등 api 출처"),
																	fieldWithPath("data.traffics[].detail.direction")
																			.type(JsonFieldType.STRING)
																			.description("신호등 방향"),
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
	@DisplayName("GET 신호등 정보 조회 - 신호등 좌표로 조회")
	void searchTrafficsWithTrafficParam() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL)
								.contentType(MediaType.APPLICATION_JSON)
								.param("traLat", "35.178501")
								.param("traLng", "126.912083"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"searchTrafficsWithTrafficParam",
								resource(
										ResourceSnippetParameters.builder()
												.summary("신호등 정보 조회")
												.description(
														"vblLat, vblLng, vtrLat, vtrLng로 화면 좌표로 신호등 정보 조회 || traLat, traLng로 신호등 좌표로 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("SearchTrafficsWithTrafficParamRequest"))
												.requestParameters(
														new ParameterDescriptorWithType("traLat")
																.type(SimpleType.NUMBER)
																.description("신호등 위도")
																.optional()
																.defaultValue(35.178501),
														new ParameterDescriptorWithType("traLng")
																.type(SimpleType.NUMBER)
																.description("신호등 경도")
																.optional()
																.defaultValue(126.912083))
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
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].detail.trafficId")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffics[].detail.apiSource")
																			.type(JsonFieldType.STRING)
																			.description("신호등 api 출처"),
																	fieldWithPath("data.traffics[].detail.direction")
																			.type(JsonFieldType.STRING)
																			.description("신호등 방향"),
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
	@DisplayName("GET /{trafficId} 신호등 정보 조회 - 신호등 ID로 조회")
	void browseTraffic() throws Exception {
		mockMvc
				.perform(get(BASE_URL + "/{trafficId}", 3).contentType(MediaType.APPLICATION_JSON))
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
																.description("신호등 ID")
																.defaultValue(3L))
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
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffic.detail.trafficId")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffic.detail.apiSource")
																			.type(JsonFieldType.STRING)
																			.description("신호등 api 출처"),
																	fieldWithPath("data.traffic.detail.direction")
																			.type(JsonFieldType.STRING)
																			.description("신호등 방향"),
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
	@DisplayName("POST /favorite 신호등 즐겨찾기 추가")
	void addFavoriteTraffic() throws Exception {
		FavoriteTrafficBody body =
				FavoriteTrafficBody.builder().trafficId(1L).trafficAlias("alias1").build();
		String content = objectMapper.writeValueAsString(body);

		when(addFavoriteTrafficUseCase.execute(any())).thenReturn(true);

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
	@DisplayName("GET /favorite 즐겨찾기 신호등 조회")
	void browseFavoriteTraffics() throws Exception {

		when(browseFavoriteTrafficsUseCase.execute(any()))
				.thenReturn(
						BrowseFavoriteTrafficsUseCaseOut.builder()
								.traffics(
										List.of(
												FavoriteTrafficDetail.builder()
														.id(1L)
														.detail(
																TrafficDetailInfo.builder()
																		.trafficId(1L)
																		.apiSource("seoul")
																		.direction("nt")
																		.build())
														.name("후문")
														.point(
																PointDetail.builder()
																		.lat(TF_BACK_DOOR_LAT)
																		.lng(TF_BACK_DOOR_LNG)
																		.build())
														.createdAt(LocalDateTime.now())
														.build(),
												FavoriteTrafficDetail.builder()
														.id(2L)
														.detail(
																TrafficDetailInfo.builder()
																		.trafficId(2L)
																		.apiSource("seoul")
																		.direction("wt")
																		.build())
														.name("후문3거리")
														.point(
																PointDetail.builder()
																		.lat(TF_BACK_THREE_LAT)
																		.lng(TF_BACK_THREE_LNG)
																		.build())
														.createdAt(LocalDateTime.now())
														.build(),
												FavoriteTrafficDetail.builder()
														.id(3L)
														.detail(
																TrafficDetailInfo.builder()
																		.trafficId(3L)
																		.apiSource("seoul")
																		.direction("sw")
																		.build())
														.name("창평")
														.point(
																PointDetail.builder()
																		.lat(TF_CHANPUNG_LAT)
																		.lng(TF_CHANPUNG_LNG)
																		.build())
														.createdAt(LocalDateTime.now())
														.build(),
												FavoriteTrafficDetail.builder()
														.id(4L)
														.detail(
																TrafficDetailInfo.builder()
																		.trafficId(4L)
																		.apiSource("seoul")
																		.direction("nt")
																		.build())
														.name("쿠쿠")
														.point(PointDetail.builder().lat(TF_CUCU_LAT).lng(TF_CUCU_LNG).build())
														.createdAt(LocalDateTime.now())
														.build()))
								.build());

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
																			.type(JsonFieldType.OBJECT)
																			.description("신호등 상세 정보"),
																	fieldWithPath("data.traffics[].detail.trafficId")
																			.type(JsonFieldType.NUMBER)
																			.description("신호등 id"),
																	fieldWithPath("data.traffics[].detail.apiSource")
																			.type(JsonFieldType.STRING)
																			.description("신호등 api 출처"),
																	fieldWithPath("data.traffics[].detail.direction")
																			.type(JsonFieldType.STRING)
																			.description("신호등 방향"),
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
	@DisplayName("PATCH /favorite/{favoriteId} 즐겨찾기 신호등 수정")
	void updateFavoriteTraffic() throws Exception {
		PatchFavoriteTrafficNameBody body =
				PatchFavoriteTrafficNameBody.builder().trafficAlias("alias2").build();
		String content = objectMapper.writeValueAsString(body);

		when(updateFavoriteTrafficUseCase.execute(any())).thenReturn(true);

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
	@DisplayName("DELETE /favorite/{favoriteId} 즐겨찾기 신호등 삭제")
	void deleteFavoriteTraffic() throws Exception {

		when(deleteFavoriteTrafficUseCase.execute(any())).thenReturn(true);

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
