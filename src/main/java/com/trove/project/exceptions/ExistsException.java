package com.trove.project.exceptions;

public class ExistsException extends Exception {
 
	private static final long serialVersionUID = 2329397655150547235L;
	
	public ExistsException() {
		super("User already exists");
	}
	
	public ExistsException(String message) {
		super(message);
	}

}
