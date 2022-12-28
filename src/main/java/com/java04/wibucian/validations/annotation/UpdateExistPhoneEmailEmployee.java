package com.java04.wibucian.validations.annotation;

import com.java04.wibucian.validations.validator.UpdateExistPhoneEmailEmployeeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UpdateExistPhoneEmailEmployeeValidator.class)
@Documented
public @interface UpdateExistPhoneEmailEmployee {
	String message() default "Phone or Email have registered!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
