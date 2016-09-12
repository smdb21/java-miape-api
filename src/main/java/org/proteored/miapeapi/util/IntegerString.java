package org.proteored.miapeapi.util;

/**
 * Miape ID and MIAPE Type pair
 * 
 * @author Salva
 * 
 */
public class IntegerString {
	private Integer miapeID;
	private String miapeType;

	public IntegerString() {

	}

	public IntegerString(Integer miapeID, String miapeType) {
		this.miapeID = miapeID;
		this.miapeType = miapeType;
	}

	public Integer getMiapeID() {
		return miapeID;
	}

	public String getMiapeType() {
		return miapeType;
	}

	public void setMiapeID(Integer miapeID) {
		this.miapeID = miapeID;
	}

	public void setMiapeType(String miapeType) {
		this.miapeType = miapeType;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof IntegerString) {
			IntegerString is = (IntegerString) arg0;
			if (is.miapeID == this.miapeID && is.miapeType.equals(this.miapeType))
				return true;
			else
				return false;
		}
		return super.equals(arg0);

	}

}
