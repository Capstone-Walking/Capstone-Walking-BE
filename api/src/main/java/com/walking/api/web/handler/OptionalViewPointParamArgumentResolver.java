package com.walking.api.web.handler;

import com.walking.api.web.dto.request.point.OptionalViewPointParam;
import com.walking.api.web.dto.request.point.ViewPointParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OptionalViewPointParamArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter
				.getParameterType()
				.getSimpleName()
				.contains(ViewPointParam.class.getSimpleName());
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory)
			throws Exception {
		String vblLat = webRequest.getParameter("vblLat");
		String vblLng = webRequest.getParameter("vblLng");
		String vtrLat = webRequest.getParameter("vtrLat");
		String vtrLng = webRequest.getParameter("vtrLng");

		if (isNotSet(vblLat) || isNotSet(vblLng) || isNotSet(vtrLat) || isNotSet(vtrLng)) {
			return OptionalViewPointParam.builder().viewPointParam(null).build();
		}

		return OptionalViewPointParam.builder()
				.viewPointParam(
						ViewPointParam.builder()
								.vblLat(Double.parseDouble(vblLat))
								.vblLng(Double.parseDouble(vblLng))
								.vtrLat(Double.parseDouble(vtrLat))
								.vtrLng(Double.parseDouble(vtrLng))
								.build())
				.build();
	}

	private boolean isNotSet(String value) {
		return value == null;
	}
}
