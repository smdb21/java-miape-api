package org.proteored.miapeapi.experiment.model;

public enum IdentificationItemEnum {
	PROTEIN, PEPTIDE, PSM;

	public static IdentificationItemEnum[] valuesWithNoPSMs() {
		IdentificationItemEnum[] ret = { PROTEIN, PEPTIDE };
		return ret;
	}

}
