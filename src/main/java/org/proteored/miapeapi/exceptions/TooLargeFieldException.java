package org.proteored.miapeapi.exceptions;

public class TooLargeFieldException extends MiapeDatabaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String SQL_SERVER_ERROR_MESSAGE = "Los datos de cadena o binarios se truncarIan.";
	public final static int SQL_SERVER_ERROR_CODE = 8152;
	public TooLargeFieldException() {
		super();
	}

	public TooLargeFieldException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TooLargeFieldException(String message) {
		super(message);
	}

	public TooLargeFieldException(Throwable throwable) {
		super(throwable);
	}

}
