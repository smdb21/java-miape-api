package org.proteored.miapeapi.exceptions;

public class WrongXMLFormatException extends IllegalMiapeArgumentException {
	private static final long serialVersionUID = 1L;

	public WrongXMLFormatException() {
		super();
	}

	public WrongXMLFormatException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public WrongXMLFormatException(String message) {
		super(message);
	}

	public WrongXMLFormatException(Throwable throwable) {
		super(throwable);
	}
}
