package org.proteored.miapeapi.validation.ms;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.validation.AbstractMiapeValidator;
import org.proteored.miapeapi.validation.MiapeSection;
import org.proteored.miapeapi.validation.MiapeValidator;
import org.proteored.miapeapi.validation.ValidationReport;

public class MiapeMSValidator extends AbstractMiapeValidator  implements MiapeValidator<MiapeMSDocument>{
	private static MiapeMSValidator instance;
	
	private MiapeMSValidator() {
	
	}
	
	public static MiapeMSValidator getInstance() {
		if (instance == null) {
			instance = new MiapeMSValidator();
		}
		return instance;
	}
	
	@Override
	public ValidationReport getReport(MiapeMSDocument miape) {
		List<MiapeSection> missingSections = new ArrayList<MiapeSection>();
		List<MiapeSection> validSections = new ArrayList<MiapeSection>();
		boolean isCompliant = validateMiape(miape, missingSections, validSections, MiapeMSSection.values());
		ValidationReport result = new ValidationReport(isCompliant, missingSections, validSections);
		return result;
	}
	
	

}
