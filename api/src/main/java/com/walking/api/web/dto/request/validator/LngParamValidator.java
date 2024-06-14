package com.walking.api.web.dto.request.validator;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LngParamValidator implements ConstraintValidator<LngParam, Float> {

	@Override
	public boolean isValid(Float value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			addConstraintViolation(context, "lng is null");
			return false;
		}

		if (value < 0) {
			addConstraintViolation(context, "lng is less than 0");
			return false;
		}

		/* 경도 범위 */
		if (!((124 < value) && (value < 132))) {
			addConstraintViolation(context, "lng is out of range");
			return false;
		}

		return true;
	}

	private void addConstraintViolation(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

	@Override
	public void initialize(LngParam constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}
