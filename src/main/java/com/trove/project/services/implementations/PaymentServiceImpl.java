package com.trove.project.services.implementations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.InitializeTransactionResponse;
import com.trove.project.models.VerifyTransactionResponse;
import com.trove.project.models.entities.Loan;
import com.trove.project.models.entities.Payment;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.LoanRepository;
import com.trove.project.repositories.PaymentRepository;
import com.trove.project.repositories.UserRepository;
import com.trove.project.security.SecurityUtils;
import com.trove.project.services.PaymentService;
import com.trove.project.utilities.RandomString;

import me.iyanuadelekan.paystackjava.core.Transactions;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Value("${payment.callback.url}")
	private String paymentCallbackUrl;

	@Value("${top-up.callback.url}")
	private String topUpCallbackUrl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	private InitializeTransactionResponse payWithPaystack(@NotNull @Email String email,
			@NotNull @DecimalMin(value = "0.0", inclusive = true) @Digits(integer = 10, fraction = 2) BigDecimal amount,
			boolean wallet) throws TransactionException {
		try {

			HashMap<String, Object> query = new HashMap<>();

			String reference = RandomString.randomString(15);
			while (this.paymentRepository.existsByReference(reference))
				reference = RandomString.randomString(15);

			query.put("reference", RandomString.randomString(15));
			query.put("amount", amount.multiply(new BigDecimal(100)).toBigIntegerExact());
			query.put("email", email);
//			query.put("callback_url", wallet ? topUpCallbackUrl : paymentCallbackUrl);
			if (wallet)
				query.put("metadata", "{\"wallet\":true}");
			else
				query.put("metadata", "{\"wallet\":false}");

			Transactions transactions = new Transactions();
			JSONObject jsonObject = transactions.initializeTransaction(query);

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(jsonObject.toString(), InitializeTransactionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TransactionException("sorry error occured while connecting to paystack");
		}

	}

	private VerifyTransactionResponse verifyPaymentWithPaystack(@NotNull String reference)
			throws JsonMappingException, JsonProcessingException, TransactionException {

		try {
			Transactions transactions = new Transactions();

			JSONObject jsonObject = transactions.verifyTransaction(reference);
			ObjectMapper mapper = new ObjectMapper();

			return mapper.readValue(jsonObject.toString(), VerifyTransactionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TransactionException("sorry error occured while connecting to paystack");
		}

	}

	@Override
	public String payPeriodicLoanViaPaystack() throws IllegalOperationException, TransactionException {
		Long userId = SecurityUtils.getCurrentUserId()
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		String email = SecurityUtils.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found"))
				.getEmail();
		List<Loan> loans = loanRepository.findAllByCreatedByAndActive(userId, true);
		if (loans.isEmpty())
			throw new ResourceNotFoundException("No active loan found for this user");
		Loan loan = loans.get(0);

		Optional<Payment> check = this.paymentRepository.findOneByCreatedByAndLoanIdAndStatus(userId, loan.getId(),
				false);
		if (check.isPresent())
			return check.get().getAuthorization_url();
		try {
			InitializeTransactionResponse res = this.payWithPaystack(email, loan.getMonthlyPayment(), false);
			if (res.isStatus()) {
				paymentRepository.save(
						new Payment(res.getData().getReference(), res.getData().getAuthorization_url(), loan.getId()));
				return res.getData().getAuthorization_url();
			} else
				throw new TransactionException("sorry error occured while connecting to paystack");
		} catch (TransactionException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TransactionException("sorry error occured while connecting to paystack");
		}
	}

	@Override
	public String topUpWalletWithPaystack(
			@NotNull @DecimalMin(value = "0.0", inclusive = true) @Digits(integer = 10, fraction = 2) BigDecimal amount)
			throws TransactionException {
		String email = SecurityUtils.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found"))
				.getEmail();
		try {
			InitializeTransactionResponse res = this.payWithPaystack(email, amount, true);
			if (res.isStatus()) {
				paymentRepository.save(new Payment(res.getData().getReference(), res.getData().getAuthorization_url()));
				return res.getData().getAuthorization_url();
			} else
				throw new TransactionException("sorry error occured while connecting to paystack");
		} catch (TransactionException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TransactionException("sorry error occured while connecting to paystack");
		}
	}

	@Override
	public @NotNull User verifyTopUpWalletWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException {
		try {
			VerifyTransactionResponse res = this.verifyPaymentWithPaystack(reference);

			if (res.getData().getStatus().equals("success")) {
				Payment payment = this.paymentRepository.findOneByReferenceAndStatus(reference, false)
						.orElseThrow(() -> new ResourceNotFoundException("Payment not found or has been made"));

				User user = this.userRepository.findById(payment.getCreatedById()).get();
				user.setWallet(user.getWallet().add(res.getData().getAmount().divide(new BigDecimal(100))));
				payment.setStatus(true);
				this.paymentRepository.save(payment);
				return this.userRepository.save(user);
			} else
				throw new IllegalOperationException("Payment was unsuccessful, cannot top up wallet");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new TransactionException();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw e;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (TransactionException e) {
			e.printStackTrace();
			throw e;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalOperationException();
		}
	}

	@Override
	public void verifyTopUpWalletWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException {
		try {
			ObjectMapper mapper = new ObjectMapper();

			VerifyTransactionResponse res = mapper.readValue(verify, VerifyTransactionResponse.class);
			if (res.getData().getStatus().equals("success")) {
				Payment payment = this.paymentRepository
						.findOneByReferenceAndStatus(res.getData().getReference(), false)
						.orElseThrow(() -> new ResourceNotFoundException("Payment not found or has been made"));

				User user = this.userRepository.findById(payment.getCreatedById()).get();
				user.setWallet(user.getWallet().add(res.getData().getAmount().divide(new BigDecimal(100))));
				payment.setStatus(true);
				this.paymentRepository.save(payment);
				this.userRepository.save(user);
			} else
				throw new IllegalOperationException("Payment was unsuccessful, cannot top up wallet");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new TransactionException();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw e;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalOperationException();
		}
	}

	@Override
	public @NotNull Loan verifyPaymentForLoanWithPaystack(@NotNull String reference)
			throws IllegalOperationException, TransactionException {
		try {
			VerifyTransactionResponse res = this.verifyPaymentWithPaystack(reference);

			if (res.getData().getStatus().equals("success")) {
				Payment payment = this.paymentRepository.findOneByReferenceAndStatus(reference, false)
						.orElseThrow(() -> new ResourceNotFoundException("Payment not found or has been made"));
				List<Loan> loans = loanRepository.findAllByCreatedByAndActive(payment.getCreatedById(), true);
				if (loans.isEmpty())
					throw new ResourceNotFoundException("Loan not found for this user");
				Loan loan = loans.get(0);
				loan.setAmountPaid(loan.getAmountPaid().add(res.getData().getAmount().divide(new BigDecimal(100))));

				payment.setStatus(true);
				this.paymentRepository.save(payment);
				return this.loanRepository.save(loan);
			} else
				throw new IllegalOperationException("Payment was unsuccessful, cannot pay for loan");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new TransactionException();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw e;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (TransactionException e) {
			e.printStackTrace();
			throw e;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalOperationException();
		}
	}

	@Override
	public void verifyPaymentForLoanWithPaystackWebhook(@NotNull String verify)
			throws IllegalOperationException, TransactionException {
		try {
			ObjectMapper mapper = new ObjectMapper();

			VerifyTransactionResponse res = mapper.readValue(verify, VerifyTransactionResponse.class);
			if (res.getData().getStatus().equals("success")) {
				Payment payment = this.paymentRepository
						.findOneByReferenceAndStatus(res.getData().getReference(), false)
						.orElseThrow(() -> new ResourceNotFoundException("Payment not found or has been made"));
				List<Loan> loans = loanRepository.findAllByCreatedByAndActive(payment.getCreatedById(), true);
				if (loans.isEmpty())
					throw new ResourceNotFoundException("Loan not found for this user");
				Loan loan = loans.get(0);
				loan.setAmountPaid(loan.getAmountPaid().add(res.getData().getAmount().divide(new BigDecimal(100))));

				payment.setStatus(true);
				this.paymentRepository.save(payment);
				this.loanRepository.save(loan);
			} else
				throw new IllegalOperationException("Payment was unsuccessful, cannot pay for loan");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new TransactionException();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw e;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalOperationException();
		}
	}

	@Override
	public @NotNull Slice<Payment> getAllPaymentsForUser(@NotNull @Min(1) Long userId, Pageable pageable) {
		return this.paymentRepository.findAllByCreatedBy(userId, pageable);
	}

}
