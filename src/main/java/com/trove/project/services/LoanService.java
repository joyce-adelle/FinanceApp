package com.trove.project.services;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;

import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.models.BankAccount;
import com.trove.project.models.entities.Loan;

@Validated
public interface LoanService {

	/**
	 * create a loan.
	 *
	 * @return newly created loan.
	 */
	@NotNull
	Loan takeLoan(
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal amount,
			@NotNull @Min(6) @Max(12) Integer numberOfMonths, @NotNull @Valid BankAccount bankAccount)
			throws IllegalOperationException;

	/**
	 * get all user loans.
	 *
	 * @return all loans by user.
	 */
	@NotNull
	Slice<Loan> viewAllLoans(Pageable pageable);

	/**
	 * user active loan.
	 *
	 * @return a list with active loan which is of size 1 
	 * as user can only have one active loan.
	 */
	@NotNull
	List<Loan> viewActiveLoan();

}
