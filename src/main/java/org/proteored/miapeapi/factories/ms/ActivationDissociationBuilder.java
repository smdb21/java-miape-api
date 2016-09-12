package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.ms.DissociationMethod;
import org.proteored.miapeapi.cv.ms.GasType;
import org.proteored.miapeapi.cv.ms.PressureUnit;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;

public class ActivationDissociationBuilder {

	String gas;
	String name;
	String pressure;
	String activationType;
	String pressureUnit;

	ActivationDissociationBuilder(String name) {
		this.name = name;
	}

	/**
	 * Set the gas used in the collision cell. It should be one of the possible
	 * values from {@link GasType}
	 */
	public ActivationDissociationBuilder gas(String value) {
		this.gas = value;
		return this;
	}

	public ActivationDissociationBuilder pressure(String value) {
		this.pressure = value;
		return this;
	}

	/**
	 * Set the pressure unit. It should be one of the possible values from
	 * {@link PressureUnit}
	 */
	public ActivationDissociationBuilder pressureUnit(String value) {
		this.pressureUnit = value;
		return this;
	}

	/**
	 * Set the activation type. It should be one of the possible values from
	 * {@link DissociationMethod}
	 */
	public ActivationDissociationBuilder activationType(String value) {
		this.activationType = value;
		return this;
	}

	public ActivationDissociation build() {
		return new ActivationDissociationImpl(this);
	}
}