package com.trove.project.models.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class TopUpDto {

	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal amount;

}
