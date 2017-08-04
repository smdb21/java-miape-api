package org.proteored.miapeapi.exceptions;

public class InterruptedMIAPEThreadException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3816278939316366357L;

	/**
	 * 
	 */

	public InterruptedMIAPEThreadException() {
		super();
	}

	public InterruptedMIAPEThreadException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InterruptedMIAPEThreadException(String message) {
		super(message);
	}

	public InterruptedMIAPEThreadException(Throwable throwable) {
		super(throwable);
	}
}
