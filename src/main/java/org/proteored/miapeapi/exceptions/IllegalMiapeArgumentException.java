package org.proteored.miapeapi.exceptions;

public class IllegalMiapeArgumentException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalMiapeArgumentException() {
		super();
	}

	public IllegalMiapeArgumentException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IllegalMiapeArgumentException(String message) {
		super(message);
	}

	public IllegalMiapeArgumentException(Throwable throwable) {
		super(throwable);
	}
}
