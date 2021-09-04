package com.trove.project.exceptions;

public class InsufficientFundsException extends RuntimeException {

	private static final long serialVersionUID = 7617102187418744896L;

	public InsufficientFundsException() {
		super("not enough in wallet to complete this transaction");
	}

	public InsufficientFundsException(String message) {
		super(message);
	}
}
