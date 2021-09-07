package com.trove.project.controllers;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.InsufficientFundsException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.exceptions.TransactionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/*
 * Handles all exceptions thrown to the api
 */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
		ErrorResponse errors = new ErrorResponse();
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			ErrorItem error = new ErrorItem();
			error.setMessage(violation.getPropertyPath() + ": " + violation.getMessage());
			errors.addError(error);
		}

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorItem> handle(ResourceNotFoundException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ExistsException.class)
	public ResponseEntity<ErrorItem> handle(ExistsException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorItem> handle(UsernameNotFoundException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TransactionException.class)
	public ResponseEntity<ErrorItem> handle(TransactionException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalOperationException.class)
	public ResponseEntity<ErrorItem> handle(IllegalOperationException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorItem> handle(InsufficientFundsException e) {
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	public static class ErrorItem {

		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	public static class ErrorResponse {

		private List<ErrorItem> errors = new ArrayList<>();

		public List<ErrorItem> getErrors() {
			return errors;
		}

		public void setErrors(List<ErrorItem> errors) {
			this.errors = errors;
		}

		public void addError(ErrorItem error) {
			this.errors.add(error);
		}

	}
}
