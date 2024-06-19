package com.walking.api.web.dto.request.validator;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatParamValidator implements ConstraintValidator<LatParam, Float> {

	@Override
	public boolean isValid(Float value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			addConstraintViolation(context, "lat is null");
			return false;
		}

		if (value < 0) {
			addConstraintViolation(context, "lat is less than 0");
			return false;
		}

		/* 위도 범위 */
		if (!((33 < value) && (value < 43))) {
			addConstraintViolation(context, "lat is out of range");
			return false;
		}

		return true;
	}

	private void addConstraintViolation(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

	@Override
	public void initialize(LatParam constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}
