package com.trove.project.custom.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.trove.project.custom.annotations.classes.PasswordConstraintValidator;

/*
 * Annotation to verify a password
 */
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ PARAMETER, FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword {

	String message() default "Invalid Password";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}