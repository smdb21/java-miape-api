package org.proteored.miapeapi.exceptions;

public class MiapeDataInconsistencyException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MiapeDataInconsistencyException() {
		super();
	}

	public MiapeDataInconsistencyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MiapeDataInconsistencyException(String message) {
		super(message);
	}

	public MiapeDataInconsistencyException(Throwable throwable) {
		super(throwable);
	}
}
