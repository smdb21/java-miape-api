package org.proteored.miapeapi.exceptions;

public class PdfException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PdfException() {
		super();
	}

	public PdfException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PdfException(String message) {
		super(message);
	}

	public PdfException(Throwable throwable) {
		super(throwable);
	}

}
