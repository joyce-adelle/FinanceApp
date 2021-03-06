package com.trove.project.custom.annotations.classes;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.google.common.base.Joiner;

import com.trove.project.custom.annotations.ValidPassword;

/*
 * Validator for a password
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public void initialize(ValidPassword constraintAnnotation) {

	}

	// valid password cannot contain space and must be between 8 to 20 characters
	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context) {

		final PasswordValidator validator = new PasswordValidator(
				Arrays.asList(new LengthRule(8, 20), new WhitespaceRule()));
		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
				.addConstraintViolation();
		return false;
	}

}
