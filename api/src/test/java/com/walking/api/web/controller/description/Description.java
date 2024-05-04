package com.walking.api.web.controller.description;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class Description {

	private static FieldDescriptor getTimestampDescriptor() {
		return fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간");
	}

	private static FieldDescriptor getCodeDescriptor() {
		return fieldWithPath("code").type(JsonFieldType.STRING).description("code");
	}

	private static FieldDescriptor getMessageDescriptor() {
		return fieldWithPath("message").type(JsonFieldType.STRING).description("message");
	}

	public static FieldDescriptor[] common(FieldDescriptor[] data) {
		return ArrayUtils.addAll(
				data, getTimestampDescriptor(), getMessageDescriptor(), getCodeDescriptor());
	}

	public static FieldDescriptor[] common() {
		return new FieldDescriptor[] {
			getTimestampDescriptor(), getMessageDescriptor(), getCodeDescriptor()
		};
	}

	public static HeaderDescriptorWithType authHeader() {
		return headerWithName("Authorization")
				.defaultValue("{{accessToken}}")
				.description("Bearer 어세스 토큰");
	}
}
