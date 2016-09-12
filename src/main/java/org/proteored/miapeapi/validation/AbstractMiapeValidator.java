package org.proteored.miapeapi.validation;

import java.util.List;

import org.proteored.miapeapi.interfaces.MiapeDocument;

public abstract class AbstractMiapeValidator  {
	protected  boolean validateMiape(
			MiapeDocument miape, List<MiapeSection> missingSections,
			List<MiapeSection> validSections, MiapeSection[] sections) {
		boolean isCompliant = true;
		for (MiapeSection miapeSection : sections) {
			boolean containsSection = miapeSection.validate(miape);
			if (containsSection) {
				validSections.add(miapeSection);
			} else {
				isCompliant = false;
				missingSections.add(miapeSection);
			}
		}
		return isCompliant;
	}
}
