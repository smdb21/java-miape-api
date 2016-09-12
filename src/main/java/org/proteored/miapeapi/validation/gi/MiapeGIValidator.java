package org.proteored.miapeapi.validation.gi;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.validation.AbstractMiapeValidator;
import org.proteored.miapeapi.validation.MiapeSection;
import org.proteored.miapeapi.validation.MiapeValidator;
import org.proteored.miapeapi.validation.ValidationReport;

public class MiapeGIValidator extends AbstractMiapeValidator implements
		MiapeValidator<MiapeGIDocument> {
	private static MiapeGIValidator instance;

	private MiapeGIValidator() {

	}

	public static MiapeGIValidator getInstance() {
		if (instance == null) {
			instance = new MiapeGIValidator();
		}
		return instance;
	}

	@Override
	public ValidationReport getReport(MiapeGIDocument miape) {
		List<MiapeSection> missingSections = new ArrayList<MiapeSection>();
		List<MiapeSection> validSections = new ArrayList<MiapeSection>();
		boolean isCompliant = validateMiape(miape, missingSections,
				validSections, MiapeGISection.values());
		ValidationReport result = new ValidationReport(isCompliant,
				missingSections, validSections);
		return result;
	}

}
