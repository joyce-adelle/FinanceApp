package com.trove.project.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.dtos.AuthorityDto;
import com.trove.project.models.dtos.CompletedRequestDto;
import com.trove.project.models.dtos.EmailDto;
import com.trove.project.models.dtos.InitializePaymentWithPaystackDto;
import com.trove.project.models.dtos.TopUpDto;
import com.trove.project.models.dtos.UpdateUserDto;
import com.trove.project.models.entities.User;
import com.trove.project.services.PaymentService;
import com.trove.project.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentService paymentService;

	@GetMapping(value = { "", "/", "/page" })
	public ResponseEntity<Slice<User>> getUsers(Pageable pageable) {
		return ResponseEntity.ok(userService.getAllusers(pageable));
	}

	@PutMapping("/user")
	public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserDto user) {
		User currentUser = userService.getCurrentUserWithAuthorities();
		if (user.getFirstname() != null)
			currentUser.setFirstname(user.getFirstname());
		if (user.getLastname() != null)
			currentUser.setLastname(user.getLastname());
		return ResponseEntity.ok(userService.update(currentUser));
	}

	@GetMapping("/user")
	public ResponseEntity<User> getLoggedUser() {
		return ResponseEntity.ok(userService.getCurrentUserWithAuthorities());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id) {
		return ResponseEntity.ok(userService.getUserWithAuthorities(id));
	}

	@PutMapping("/{id}/addauthority")
	public ResponseEntity<CompletedRequestDto> addUserAuthority(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody AuthorityDto authority) {
		userService.addAuthority(id, authority.getAuthorityName());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PutMapping("/{id}/removeauthority")
	public ResponseEntity<CompletedRequestDto> removeUserAuthority(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody AuthorityDto authority) {
		userService.removeAuthority(id, authority.getAuthorityName());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PostMapping("/user/change-email")
	public ResponseEntity<CompletedRequestDto> changeEmail(@RequestBody @Valid EmailDto newEmail)
			throws ExistsException, TransactionException {
		userService.changeEmailInit(newEmail.getEmail());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PostMapping("/user/verify/init")
	public ResponseEntity<CompletedRequestDto> verifyEmail() throws TransactionException, IllegalOperationException {
		userService.verifyInit();
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PostMapping("/user/reset-password/init")
	public ResponseEntity<CompletedRequestDto> resetPassword() throws TransactionException {
		userService.resetPasswordInit();
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PutMapping("/user/top-up-wallet")
	public ResponseEntity<InitializePaymentWithPaystackDto> topUserWallet(@Valid @RequestBody TopUpDto topUp)
			throws TransactionException, IllegalOperationException {
		return ResponseEntity.ok(
				new InitializePaymentWithPaystackDto(this.paymentService.topUpWalletWithPaystack(topUp.getAmount())));
	}
}