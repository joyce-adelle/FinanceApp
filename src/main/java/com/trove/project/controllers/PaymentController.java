package com.trove.project.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.dtos.CompletedRequestDto;
import com.trove.project.models.entities.Loan;
import com.trove.project.models.entities.Payment;
import com.trove.project.models.entities.User;
import com.trove.project.services.PaymentService;
import com.trove.project.utilities.PaystackAuthValidator;

/*
 * Controller to handle all payments
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Value("${paystack.secret}")
	private String secret;

	//admin get payments for a particular user
	//for verification purposes
	@GetMapping(value = {"/admin", "/admin/page" })
	public ResponseEntity<@NotNull Slice<Payment>> getAllPaymentForUser(
			@RequestParam("userId") @NotNull @Min(1) Long userId, Pageable pageable) {

		return ResponseEntity.ok(this.paymentService.getAllPaymentsForUser(userId, pageable));
	}

	@PostMapping("/verify/loan")
	public ResponseEntity<Loan> verifyPaymentForLoan(@RequestParam("reference") @NotNull String ref)
			throws TransactionException, IllegalOperationException {

		return ResponseEntity.ok(this.paymentService.verifyPaymentForLoanWithPaystack(ref));
	}

	@PostMapping("/verify/topup")
	public ResponseEntity<User> verifyTopupForUser(@RequestParam("reference") @NotNull String ref)
			throws IllegalOperationException, TransactionException {

		return ResponseEntity.ok(this.paymentService.verifyTopUpWalletWithPaystack(ref));
	}

	@PostMapping("/webhook/verify")
	public ResponseEntity<CompletedRequestDto> verifyPaymentFromPayStack(HttpServletRequest request,
			@RequestBody @NotNull String verify) throws IllegalOperationException {

		try {

			String authToken = Optional.ofNullable(request.getHeader("X-Paystack-Signature"))
					.orElseThrow(() -> new IllegalOperationException("This operation is illegal"));
			;
			if (PaystackAuthValidator.isTokenValid(verify, authToken, secret)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(verify);

				if (jsonNode.get("event").asText().equals("charge.success")) {
					if (jsonNode.get("data").get("metadata").get("wallet").asBoolean())
						this.paymentService.verifyTopUpWalletWithPaystackWebhook(verify);
					else
						this.paymentService.verifyPaymentForLoanWithPaystackWebhook(verify);
				}

				return ResponseEntity.ok(new CompletedRequestDto(true));
			}

			return new ResponseEntity<>(new CompletedRequestDto(false), HttpStatus.NOT_ACCEPTABLE);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CompletedRequestDto(false), HttpStatus.NOT_ACCEPTABLE);
		}
	}

}
