package com.trove.project.custom.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.trove.project.custom.annotations.classes.IsBankConstraintValidator;

/*
 * Annotation to verify a bank name
 */
@Constraint(validatedBy = IsBankConstraintValidator.class)
@Target({ PARAMETER, FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface IsBank {

	// error message
	public String message() default "not a valid bank";

	// represents group of constraints
	public Class<?>[] groups() default {};

	// represents additional information about annotation
	public Class<? extends Payload>[] payload() default {};

}
