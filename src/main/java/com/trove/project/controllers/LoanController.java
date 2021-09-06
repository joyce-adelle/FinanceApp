package com.trove.project.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.BankAccount;
import com.trove.project.models.dtos.InitializePaymentWithPaystackDto;
import com.trove.project.models.dtos.LoanDto;
import com.trove.project.models.entities.Loan;
import com.trove.project.services.LoanService;
import com.trove.project.services.PaymentService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	@Autowired
	private LoanService loanService;

	@Autowired
	private PaymentService paymentService;

	@PostMapping(value = { "", "/" })
	public ResponseEntity<Loan> createLoan(@RequestBody @NotNull @Valid LoanDto loanDto)
			throws IllegalOperationException {

		return ResponseEntity.ok(this.loanService.takeLoan(loanDto.getAmount(), loanDto.getNumberOfMonths(),
				new BankAccount(loanDto.getAccountName(), loanDto.getBankAccountNo(), loanDto.getBankName())));
	}

	@GetMapping(value = { "", "/", "/page" })
	public ResponseEntity<Slice<Loan>> getAllUserLoans(Pageable pageable) {

		return ResponseEntity.ok(this.loanService.viewAllLoans(pageable));
	}

	@GetMapping("/active-loan")
	public ResponseEntity<List<Loan>> getUserActiveLoan() {

		return ResponseEntity.ok(this.loanService.viewActiveLoan());
	}

	@PostMapping("/pay/paystack")
	public ResponseEntity<InitializePaymentWithPaystackDto> payPeriodicLoanViaPaystack()
			throws TransactionException, IllegalOperationException {

		return ResponseEntity.ok(new InitializePaymentWithPaystackDto(this.paymentService.payPeriodicLoanViaPaystack()));
	}

}
