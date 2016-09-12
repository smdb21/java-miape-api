package org.proteored.miapeapi.exceptions;

/**
 * @author Salva
 * 
 */
public class NoEntryRetrievedMiapeException extends MiapeDatabaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoEntryRetrievedMiapeException() {
		super();
	}

	public NoEntryRetrievedMiapeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public NoEntryRetrievedMiapeException(String message) {
		super(message);
	}

	public NoEntryRetrievedMiapeException(Throwable throwable) {
		super(throwable);
	}
}
