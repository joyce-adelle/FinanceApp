package com.trove.project.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.models.BankAccount;
import com.trove.project.models.JwtUser;
import com.trove.project.models.entities.Loan;
import com.trove.project.repositories.LoanRepository;
import com.trove.project.security.SecurityUtils;
import com.trove.project.services.LoanService;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanRepository loanRepository;

	@Override
	public @NotNull Loan takeLoan(
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal amount,
			@NotNull @Min(6) @Max(12) Integer numberOfMonths, @NotNull @Valid BankAccount bankAccount)
			throws IllegalOperationException {

		JwtUser user = SecurityUtils.getCurrentUser()
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		BigDecimal value = user.getPortfolioValue();
		BigDecimal maxLoan = value.multiply(new BigDecimal(0.6)).setScale(2, RoundingMode.UP);

		if (amount.subtract(maxLoan).signum() > 0)
			throw new IllegalOperationException("cannot loan that amount. maximum loan is " + maxLoan);

		if (this.loanRepository.existsByCreatedByAndActive(user.getId(), true))
			throw new IllegalOperationException("cannot take loan while there is an active loan");

		// code to send money to bank account

		//fixed 18% annual interest
		return this.loanRepository.save(new Loan(0.015, numberOfMonths, amount));
	}

	@Override
	public @NotNull Slice<Loan> viewAllLoans(Pageable pageable) {

		Long id = SecurityUtils.getCurrentUserId().orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return this.loanRepository.findAllByCreatedBy(id, pageable);
	}

	@Override
	public @NotNull List<Loan> viewActiveLoan() {

		Long id = SecurityUtils.getCurrentUserId().orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return this.loanRepository.findAllByCreatedByAndActive(id, true);
	}

}
