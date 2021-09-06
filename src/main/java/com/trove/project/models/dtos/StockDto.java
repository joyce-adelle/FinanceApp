package com.trove.project.models.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class StockDto {

	@NotNull
	@Size(min = 4, max = 100)
	private String name;

	@NotNull
	@Size(min = 4, max = 10)
	private String symbol;

	@NotNull
	@DecimalMin(value = "1.0", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pricePerShare;

	@NotNull
	@Positive
	private Double equityValue;

}
