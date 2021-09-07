package com.trove.project.custom.annotations.classes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.trove.project.custom.annotations.NotEquals;

/*
 * Validator for notEqual annotation
 */
public class NotEqualsConstraintValidator implements ConstraintValidator<NotEquals, String> {
	String notEqualValue = "";

	@Override
	public void initialize(NotEquals constraintAnnotation) {
		notEqualValue = constraintAnnotation.notEqualValue();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		return !value.contentEquals(notEqualValue);
	}

}