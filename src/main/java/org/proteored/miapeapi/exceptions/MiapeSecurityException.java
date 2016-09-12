package org.proteored.miapeapi.exceptions;

public class MiapeSecurityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MiapeSecurityException() {
		super();
	}

	public MiapeSecurityException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MiapeSecurityException(String message) {
		super(message);
	}

	public MiapeSecurityException(Throwable throwable) {
		super(throwable);
	}
}
