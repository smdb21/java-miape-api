package org.proteored.miapeapi.validation;

import java.util.List;

public class ValidationReport {
	private final boolean isCompliant;
	private final List<MiapeSection> missingSections;
	private final List<MiapeSection> validSections;

	public ValidationReport(boolean isCompliant,
			List<MiapeSection> missingSections, List<MiapeSection> validSections) {
		this.isCompliant = isCompliant;
		this.missingSections = missingSections;
		this.validSections = validSections;
	}

	public List<MiapeSection> getMissingSections() {
		return missingSections;
	}

	public List<MiapeSection> getValidSections() {
		return validSections;
	}

	public boolean isCompliant() {
		return isCompliant;
	}
}