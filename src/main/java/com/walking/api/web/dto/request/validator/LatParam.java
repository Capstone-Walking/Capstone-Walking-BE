package com.walking.api.web.dto.request.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LatParamValidator.class)
public @interface LatParam {
	String message() default "LatParam validation failed.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
