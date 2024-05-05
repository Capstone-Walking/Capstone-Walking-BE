package com.walking.api.web.handler;

import com.walking.api.web.dto.request.point.OptionalTrafficPointParam;
import com.walking.api.web.dto.request.point.TrafficPointParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OptionalTrafficPointParamArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter
				.getParameterType()
				.getSimpleName()
				.contains(TrafficPointParam.class.getSimpleName());
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory)
			throws Exception {
		String traLat = webRequest.getParameter("traLat");
		String traLng = webRequest.getParameter("traLng");

		if (isNotSet(traLat) || isNotSet(traLng)) {
			return OptionalTrafficPointParam.builder().trafficPointParam(null).build();
		}

		return OptionalTrafficPointParam.builder()
				.trafficPointParam(
						TrafficPointParam.builder()
								.traLat(Double.parseDouble(traLat))
								.traLng(Double.parseDouble(traLng))
								.build())
				.build();
	}

	private boolean isNotSet(String value) {
		return value == null;
	}
}
