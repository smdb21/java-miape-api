package org.proteored.miapeapi.xml.mzml;

import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

public class AdditionalInformationImpl implements MSAdditionalInformation {

	private String name = null;
	private String value = null;

	public AdditionalInformationImpl(CVParam cvParam) {
		this.name = cvParam.getName();
		if (cvParam.getValue() != null) {
			this.value = cvParam.getValue();
		}
		if (cvParam.getUnitName() != null && !"".equals(cvParam.getUnitName())) {
			this.value = this.value + " " + cvParam.getUnitName();
		}
	}

	public AdditionalInformationImpl(UserParam userParam) {
		this.name = userParam.getName();
		if (userParam.getValue() != null) {
			this.value = userParam.getValue();
		}
		if (userParam.getUnitName() != null && !"".equals(userParam.getUnitName())) {
			this.value = this.value + " " + userParam.getUnitName();
		}
	}

	public AdditionalInformationImpl(String sampleName) {
		this.name = "Sample name";
		this.value = sampleName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
