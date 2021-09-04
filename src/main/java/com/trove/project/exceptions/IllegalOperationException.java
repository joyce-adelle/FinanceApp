package com.trove.project.exceptions;

public class IllegalOperationException extends Exception {

	private static final long serialVersionUID = 4057186255004431351L;

	public IllegalOperationException() {
		super("this operation is either not permitted or is corrupted!!");
	}

	public IllegalOperationException(String message) {
		super(message);
	}

}
