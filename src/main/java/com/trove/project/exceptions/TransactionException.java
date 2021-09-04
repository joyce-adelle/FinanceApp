package com.trove.project.exceptions;

public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = 2285932622453377474L;

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException() {
		super("error occured while performing this operation. Please try again");
	}

}
