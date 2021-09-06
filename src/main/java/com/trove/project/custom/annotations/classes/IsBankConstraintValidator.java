package com.trove.project.custom.annotations.classes;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.trove.project.custom.annotations.IsBank;

public class IsBankConstraintValidator implements ConstraintValidator<IsBank, String> {

	final List<String> list = List.of("fidelity bank", "guaranty trust bank", "united bank for africa", "access bank",
			"zenith bank", "union bank", "first bank of nigeria", "first city monument bank");

	@Override
	public void initialize(IsBank constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		if (value.isBlank())
			return false;

		if (list.contains(value.toLowerCase()))
			return true;
		return false;
	}

}
