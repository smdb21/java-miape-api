package org.proteored.miapeapi.validation.msi;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.validation.AbstractMiapeValidator;
import org.proteored.miapeapi.validation.MiapeSection;
import org.proteored.miapeapi.validation.MiapeValidator;
import org.proteored.miapeapi.validation.ValidationReport;

public class MiapeMSIValidator extends AbstractMiapeValidator  implements MiapeValidator<MiapeMSIDocument>{
	private static MiapeMSIValidator instance;
	
	private MiapeMSIValidator() {
	
	}
	
	public static MiapeMSIValidator getInstance() {
		if (instance == null) {
			instance = new MiapeMSIValidator();
		}
		return instance;
	}
	
	@Override
	public ValidationReport getReport(MiapeMSIDocument miape) {
		List<MiapeSection> missingSections = new ArrayList<MiapeSection>();
		List<MiapeSection> validSections = new ArrayList<MiapeSection>();
		boolean isCompliant = validateMiape(miape, missingSections, validSections, MiapeMSISection.values());
		ValidationReport result = new ValidationReport(isCompliant, missingSections, validSections);
		return result;
	}
	
	

}
