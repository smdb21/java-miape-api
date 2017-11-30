package org.proteored.miapeapi.xml.util;

public abstract class MiapeIdentifierCounter {
	private static int counter = 0;

	public static synchronized int increaseCounter() {
		counter++;
		return counter;
	}

	public static synchronized int getCounter() {
		return counter;
	}

	public static synchronized void clearCounter() {
		counter = 0;
	}
}
