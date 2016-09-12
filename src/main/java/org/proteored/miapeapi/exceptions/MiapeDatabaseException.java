package org.proteored.miapeapi.exceptions;

public class MiapeDatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MiapeDatabaseException() {
		super();
	}

	public MiapeDatabaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MiapeDatabaseException(String message) {
		super(message);
	}

	public MiapeDatabaseException(Throwable throwable) {
		super(throwable);
	}

}
