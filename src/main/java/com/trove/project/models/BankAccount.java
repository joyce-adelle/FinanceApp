package com.trove.project.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.trove.project.custom.annotations.IsBank;

public class BankAccount {

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
