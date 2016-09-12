package org.proteored.miapeapi.xml.mzml;

import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;

public class CollisionCellImpl implements ActivationDissociation {
	private String name = null;
	private String gasType = null;
	private String gasPressure = null;
	private String gasPressureUnit = null;
	private String activationType = null;

	public CollisionCellImpl(String name, String gasType, String gasPressure, String gasPressureUnit,
			String activationType) {
		this.activationType = activationType;
		this.gasPressure = gasPressure;
		this.gasPressureUnit = gasPressureUnit;
		this.gasType = gasType;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getGasType() {
		return this.gasType;
	}

	@Override
	public String getGasPressure() {
		return this.gasPressure;
	}

	@Override
	public String getPressureUnit() {
		return this.gasPressureUnit;
	}

	@Override
	public String getActivationType() {
		return this.activationType;
	}

}
