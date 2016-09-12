package org.proteored.miapeapi.xml.msi.adapter;

import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.xml.msi.autogenerated.MSISoftwareType;
import org.proteored.miapeapi.xml.msi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class SoftwareMSIAdapter implements Adapter<MSISoftwareType> {
	private final Software software;
	private final ObjectFactory factory;
	private final MSIControlVocabularyXmlFactory cvFactory;

	public SoftwareMSIAdapter(Software software, ObjectFactory factory,
			MSIControlVocabularyXmlFactory cvFactory) {
		this.cvFactory = cvFactory;
		this.factory = factory;
		this.software = software;
	}

	@Override
	public MSISoftwareType adapt() {
		MSISoftwareType xmlSoftware = factory.createMSISoftwareType();
		xmlSoftware.setCatalogNumber(software.getCatalogNumber());
		xmlSoftware.setComments(software.getComments());
		xmlSoftware.setCustomizations(software.getCustomizations());
		xmlSoftware.setDescription(cvFactory.createCV(software.getDescription(), null,
				DataTransformation.getInstance(cvFactory.getCvManager())));
		xmlSoftware.setManufacturer(software.getManufacturer());
		xmlSoftware.setModel(software.getModel());
		xmlSoftware.setName(cvFactory.createCV(software.getName(), null,
				SoftwareName.getInstance(cvFactory.getCvManager())));
		xmlSoftware.setParameters(software.getParameters());
		xmlSoftware.setURI(software.getURI());
		xmlSoftware.setVersion(software.getVersion());
		xmlSoftware.setId(MiapeXmlUtil.IdentifierPrefixes.SOFTWARE.getPrefix() + software.getId());

		return xmlSoftware;
	}

}
