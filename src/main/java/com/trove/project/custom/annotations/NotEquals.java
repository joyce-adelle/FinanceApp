package com.trove.project.custom.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.trove.project.custom.annotations.classes.NotEqualsConstraintValidator;

/*
 * Annotation to verify that a string is not equal to set value
 */
@Constraint(validatedBy = NotEqualsConstraintValidator.class)
@Target({ PARAMETER, FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface NotEquals {
	// error message
	public String message() default "inappropriate value";

	// represents group of constraints
	public Class<?>[] groups() default {};

	// represents additional information about annotation
	public Class<? extends Payload>[] payload() default {};

	String notEqualValue();
}