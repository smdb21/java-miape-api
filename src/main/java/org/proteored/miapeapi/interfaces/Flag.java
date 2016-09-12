package org.proteored.miapeapi.interfaces;

public enum Flag {
	YES(true), NO(false);
	private final boolean value;

	private Flag(boolean value) {
		this.value = value;
	}

	public int getValue() {
		if (value)
			return 1;
		return 0;
	}

	public static boolean getValue(int integValue) {
		return integValue == 1;
	}
	
	public static int getValue(boolean booleanValue) {
		if (booleanValue) {
			return 1;
		}
		return 0;
	}
}
