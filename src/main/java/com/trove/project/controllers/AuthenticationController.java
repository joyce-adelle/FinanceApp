package com.trove.project.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.dtos.CompletedRequestDto;
import com.trove.project.models.dtos.LoginDto;
import com.trove.project.models.dtos.ResetPasswordDto;
import com.trove.project.models.dtos.SignUpDto;
import com.trove.project.models.dtos.TokenDto;
import com.trove.project.models.dtos.UsernameDto;
import com.trove.project.security.JwtFilter;
import com.trove.project.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JwtToken> login(@RequestBody @Valid LoginDto loginDto) {

		String jwt = userService.login(loginDto.getUsername(), loginDto.getPassword(), loginDto.getRememberMe());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

		return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/signup")
	ResponseEntity<CompletedRequestDto> signUp(@RequestBody @Valid @NotNull(message = "The user cannot be null.") SignUpDto user)
			throws ExistsException, TransactionException {

		userService.create(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), user.getPassword());;
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PostMapping("/forgot-password")
	ResponseEntity<CompletedRequestDto> forgotPassword(@RequestBody @Valid UsernameDto username)
			throws ExistsException, TransactionException {

		userService.forgotPasswordInit(username.getUsername());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PutMapping("/verify/finish")
	public ResponseEntity<CompletedRequestDto> verifyEmail(@RequestBody @Valid TokenDto token)
			throws ExistsException, TransactionException, IllegalOperationException {
		userService.verifyFinish(token.getToken());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@PutMapping("/reset-password/finish")
	public ResponseEntity<CompletedRequestDto> resetPassword(@RequestBody @Valid ResetPasswordDto resPass)
			throws ExistsException, TransactionException, IllegalOperationException {
		userService.resetPasswordFinish(resPass.getToken(), resPass.getOldPassword(), resPass.getNewPassword());
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	static class JwtToken {

		private String idToken;

		JwtToken(String idToken) {
			this.idToken = idToken;
		}

		@JsonProperty("id_token")
		String getIdToken() {
			return idToken;
		}

		void setIdToken(String idToken) {
			this.idToken = idToken;
		}
	}
}
