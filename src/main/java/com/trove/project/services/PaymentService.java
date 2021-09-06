package com.trove.project.services;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.entities.Loan;
import com.trove.project.models.entities.Payment;
import com.trove.project.models.entities.User;

public interface PaymentService {

	String topUpWalletWithPaystack(
			@NotNull @DecimalMin(value = "0.0", inclusive = true) @Digits(integer = 10, fraction = 2) BigDecimal amount)
			throws TransactionException, IllegalOperationException;

	@NotNull
	Loan verifyPaymentForLoanWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException;

	@NotNull
	User verifyTopUpWalletWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException;

	String payPeriodicLoanViaPaystack() throws IllegalOperationException, TransactionException;

	@NotNull
	Slice<Payment> getAllPaymentsForUser(@NotNull @Min(1) Long userId, Pageable pageable);

	void verifyTopUpWalletWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException;

	void verifyPaymentForLoanWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException;

}
