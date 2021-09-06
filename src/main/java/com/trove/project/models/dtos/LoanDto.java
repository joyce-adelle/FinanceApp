package com.trove.project.models.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.trove.project.custom.annotations.IsBank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class LoanDto {

	@NotNull
	@DecimalMin(value = "1.0", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal amount;

	@NotNull
	@Min(6)
	@Max(12)
	private Integer numberOfMonths;

	@NotNull
	private String accountName;

	@NotNull
	@Size(min = 10, max = 10, message = "Bank account number should be 10 digits")
	@Pattern(regexp = "^[0-9]+$")
	private String bankAccountNo;

	@NotNull
	@IsBank
	private String bankName;

}
