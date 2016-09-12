package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;

public class ActivationDissociationImpl implements ActivationDissociation {

	private final String gas;
	private final String name;
	private final String pressure;
	private final String pressureUnit;
	private final String activationType;

	public ActivationDissociationImpl(ActivationDissociationBuilder activationDissociationBuilder) {
		this.gas = activationDissociationBuilder.gas;
		this.name = activationDissociationBuilder.name;
		this.pressure = activationDissociationBuilder.pressure;
		this.pressureUnit = activationDissociationBuilder.pressureUnit;
		this.activationType = activationDissociationBuilder.activationType;

	}

	@Override
	public String getGasType() {
		return gas;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getGasPressure() {
		return pressure;
	}

	@Override
	public String getActivationType() {
		return activationType;
	}

	@Override
	public String getPressureUnit() {
		return pressureUnit;
	}

}
