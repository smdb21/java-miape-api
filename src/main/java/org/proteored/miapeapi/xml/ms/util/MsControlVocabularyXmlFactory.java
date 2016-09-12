package org.proteored.miapeapi.xml.ms.util;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.xml.ms.autogenerated.FuGECommonOntologyCvParamType;
import org.proteored.miapeapi.xml.ms.autogenerated.FuGECommonOntologyPropertyValue;
import org.proteored.miapeapi.xml.ms.autogenerated.FuGECommonOntologyUserParamType;
import org.proteored.miapeapi.xml.ms.autogenerated.MIAPEParamUnitType;
import org.proteored.miapeapi.xml.ms.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ms.autogenerated.ParamType;

public class MsControlVocabularyXmlFactory {
	private final ObjectFactory factory;
	private final ControlVocabularyManager cvManager;

	public MsControlVocabularyXmlFactory(ObjectFactory factory,
			ControlVocabularyManager controlVocabularyUtil) {
		this.factory = factory;
		this.cvManager = controlVocabularyUtil;
	}

	public ControlVocabularyManager getCvManager() {
		return this.cvManager;
	}

	public ParamType createCV(String name, String value, ControlVocabularySet... cvSets) {
		if (name == null)
			return null;
		if (cvSets == null)
			return createUserParam(name, value);
		ParamType param;
		if (cvSets.length == 0)
			return createUserParam(name, value);
		for (ControlVocabularySet cvSet : cvSets) {
			if (cvManager.isCV(name, cvSet)) {

				param = factory.createParamType();
				FuGECommonOntologyCvParamType ontologyCvParamType = factory
						.createFuGECommonOntologyCvParamType();
				Accession cvId = cvManager.getControlVocabularyId(name, cvSet);
				String cvRef = cvManager.getCVRef(cvId, cvSet);

				if (value != null)
					ontologyCvParamType.setValue(value);
				ontologyCvParamType.setAccession(cvId.toString());
				ontologyCvParamType.setName(name);
				ontologyCvParamType.setCvRef(cvRef);
				param.setCvParam(ontologyCvParamType);

				return param;
			}
		}
		return createUserParam(name, value);

	}

	private ParamType createUserParam(String name, String value) {
		ParamType param = factory.createParamType();
		FuGECommonOntologyUserParamType ontologyUserParamType = factory
				.createFuGECommonOntologyUserParamType();
		if (value != null)
			ontologyUserParamType.setValue(value);
		ontologyUserParamType.setName(name);
		param.setUserParam(ontologyUserParamType);

		return param;
	}

	public FuGECommonOntologyPropertyValue createProperty(String value, String unit,
			ControlVocabularySet... cvSets) {

		FuGECommonOntologyPropertyValue propertyValue = factory
				.createFuGECommonOntologyPropertyValue();
		propertyValue.setValue(value);
		if (unit == null || cvSets.length == 0)
			return propertyValue;

		ControlVocabularySet section = cvSets[0];
		if (cvManager.isCV(unit, section)) {
			Accession cvId = cvManager.getControlVocabularyId(unit, section);
			propertyValue.setUnitAccession(cvId.toString());
			propertyValue.setUnitCvRef(cvManager.getCVRef(cvId, section));

		}
		propertyValue.setUnitName(unit);

		return propertyValue;
	}

	public MIAPEParamUnitType createParamUnit(String value, String unit,
			ControlVocabularySet... cvSets) {
		if (value == null)
			return null;
		MIAPEParamUnitType paramUnitXML = factory.createMIAPEParamUnitType();
		paramUnitXML.setValue(value);
		if (unit == null || cvSets.length == 0)
			return paramUnitXML;

		ControlVocabularySet cvSet = cvSets[0];
		if (cvManager.isCV(unit, cvSet)) {
			Accession unitId = cvManager.getControlVocabularyId(unit, cvSet);
			paramUnitXML.setUnitAccession(unitId.toString());
			paramUnitXML.setUnitCvRef(cvManager.getCVRef(unitId, cvSet));
		}
		paramUnitXML.setUnitName(unit);

		return paramUnitXML;
	}

	public static String getName(ParamType paramType) {
		String ret;
		ret = "";
		if (paramType == null)
			return null;
		FuGECommonOntologyCvParamType cvParam = paramType.getCvParam();
		if (cvParam != null) {
			FuGECommonOntologyCvParamType fugeCVParam = cvParam;
			ret = fugeCVParam.getName();
		} else {
			FuGECommonOntologyUserParamType userParam = paramType.getUserParam();
			if (userParam != null) {
				FuGECommonOntologyUserParamType fugeUserParam = userParam;
				ret = fugeUserParam.getName();
			}
		}
		return ret;
	}

	public static String getValue(ParamType paramType) {

		if (paramType == null)
			return null;
		FuGECommonOntologyCvParamType cvParam = paramType.getCvParam();
		if (cvParam != null)
			return cvParam.getValue();
		FuGECommonOntologyUserParamType userParam = paramType.getUserParam();
		if (userParam != null)
			return userParam.getValue();
		return null;
	}

	public static String getValue(MIAPEParamUnitType paramUnitType) {

		if (paramUnitType == null)
			return null;
		return paramUnitType.getValue();
	}

	public static String getUnitName(MIAPEParamUnitType paramUnitType) {

		if (paramUnitType == null)
			return null;
		return paramUnitType.getUnitName();

	}

	public static String getName(ParamType paramType, ControlVocabularySet... cvSets) {
		String ret;
		ret = "";
		if (paramType == null)
			return null;
		FuGECommonOntologyCvParamType cvParam = paramType.getCvParam();
		if (cvParam != null) {
			FuGECommonOntologyCvParamType fugeCVParam = cvParam;
			ret = fugeCVParam.getName();
		} else {
			FuGECommonOntologyUserParamType userParam = paramType.getUserParam();
			if (userParam != null) {
				FuGECommonOntologyUserParamType fugeUserParam = userParam;
				ret = fugeUserParam.getName();
			}
		}
		return ret;
	}
}
