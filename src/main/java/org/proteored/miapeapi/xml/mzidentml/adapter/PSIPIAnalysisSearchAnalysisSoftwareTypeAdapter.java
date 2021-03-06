package org.proteored.miapeapi.xml.mzidentml.adapter;

import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIAnalysisSearchAnalysisSoftwareType;
import org.proteored.miapeapi.xml.mzidentml.util.MzidentmlControlVocabularyXmlFactory;

public class PSIPIAnalysisSearchAnalysisSoftwareTypeAdapter implements
		Adapter<PSIPIAnalysisSearchAnalysisSoftwareType> {
	private final Software software;
	private final ObjectFactory factory;
	private final MzidentmlControlVocabularyXmlFactory cvFactory;

	public PSIPIAnalysisSearchAnalysisSoftwareTypeAdapter(Software software, ObjectFactory factory,
			MzidentmlControlVocabularyXmlFactory cvFactory) {
		this.cvFactory = cvFactory;
		this.factory = factory;
		this.software = software;
	}

	@Override
	public PSIPIAnalysisSearchAnalysisSoftwareType adapt() {
		PSIPIAnalysisSearchAnalysisSoftwareType softwareXML = factory
				.createPSIPIAnalysisSearchAnalysisSoftwareType();
		softwareXML.setSoftwareName(cvFactory.createCV(software.getName(), null,
				SoftwareName.getInstance(cvFactory.getCvManager())));
		softwareXML.setCustomizations(software.getCustomizations());
		softwareXML.setVersion(software.getVersion());
		softwareXML.setURI(software.getURI());
		softwareXML.setName(software.getName());
		softwareXML.setContactRole(new FuGECommonAuditContactRoleTypeAdapter(software
				.getManufacturer(), ContactPositionMS.SOFTWARE_VENDOR, factory, cvFactory).adapt());

		return softwareXML;
	}

}
