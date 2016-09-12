package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.factories.SoftwareBuilder;
import org.proteored.miapeapi.interfaces.ms.Acquisition;

public class AcquisitionBuilder extends SoftwareBuilder {

	String parameterFile;
	String targetList;
	String transitionListFile;

	AcquisitionBuilder(String name) {
		super(name);
	}

	public AcquisitionBuilder parameterFile(String value) {
		this.parameterFile = value;
		return this;
	}

	public AcquisitionBuilder targetList(String value) {
		this.targetList = value;
		return this;
	}

	public AcquisitionBuilder transitionListFile(String value) {
		this.transitionListFile = value;
		return this;
	}

	@Override
	public Acquisition build() {
		return new AcquisitionImpl(this);
	}
}