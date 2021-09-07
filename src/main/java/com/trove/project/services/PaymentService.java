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

	/**
	 * top up user wallet.
	 *
	 * @return url to use for payment.
	 */
	String topUpWalletWithPaystack(
			@NotNull @DecimalMin(value = "0.0", inclusive = true) @Digits(integer = 10, fraction = 2) BigDecimal amount)
			throws TransactionException, IllegalOperationException;

	/**
	 * verify payment for active loan (used by frontend).
	 *
	 * @return active loan if payment was successful.
	 */
	@NotNull
	Loan verifyPaymentForLoanWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException;

	/**
	 * verify payment to top up user wallet (used by frontend).
	 *
	 * @return updated user if payment was successful.
	 */
	@NotNull
	User verifyTopUpWalletWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException;

	/**
	 * pay periodic loan.
	 *
	 * @return url to use for payment.
	 */
	String payPeriodicLoanViaPaystack() throws IllegalOperationException, TransactionException;

	/**
	 * Get all payments for user with id.
	 *
	 * @return all payments for user with id.
	 */
	@NotNull
	Slice<Payment> getAllPaymentsForUser(@NotNull @Min(1) Long userId, Pageable pageable);

	/**
	 * verify payment to top up user wallet.
	 *
	 * used by payment provider (paystack)
	 */
	void verifyTopUpWalletWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException;

	/**
	 * verify payment for loan.
	 *
	 * used by payment provider (paystack)
	 */
	void verifyPaymentForLoanWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException;

}
