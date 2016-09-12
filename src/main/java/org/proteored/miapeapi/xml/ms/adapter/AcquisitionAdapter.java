package org.proteored.miapeapi.xml.ms.adapter;

import org.proteored.miapeapi.cv.ms.AcquisitionSoftware;
import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.xml.ms.autogenerated.MSAcquisition;
import org.proteored.miapeapi.xml.ms.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ms.util.MsControlVocabularyXmlFactory;

public class AcquisitionAdapter implements Adapter<MSAcquisition> {
	private final Acquisition acquisition;
	private final ObjectFactory factory;
	private final MsControlVocabularyXmlFactory cvFactory;

	public AcquisitionAdapter(Acquisition acquisition, ObjectFactory factory,
			MsControlVocabularyXmlFactory cvFactory) {
		this.acquisition = acquisition;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public MSAcquisition adapt() {
		MSAcquisition analyisSoftwareXML = factory.createMSAcquisition();
		analyisSoftwareXML.setParameterFile(acquisition.getParameterFile());
		analyisSoftwareXML.setName(cvFactory.createCV(acquisition.getName(), null,
				AcquisitionSoftware.getInstance(cvFactory.getCvManager())));
		analyisSoftwareXML.setCatalogNumber(acquisition.getCatalogNumber());
		analyisSoftwareXML.setCustomizations(acquisition.getCustomizations());
		analyisSoftwareXML.setComments(acquisition.getComments());
		analyisSoftwareXML.setDescription(cvFactory.createCV(acquisition.getDescription(), null,
				DataTransformation.getInstance(cvFactory.getCvManager())));
		analyisSoftwareXML.setManufacturer(acquisition.getManufacturer());
		analyisSoftwareXML.setModel(acquisition.getModel());
		analyisSoftwareXML.setParameters(acquisition.getParameters());
		analyisSoftwareXML.setURI(acquisition.getURI());
		analyisSoftwareXML.setVersion(acquisition.getVersion());
		analyisSoftwareXML.setTransitionListFile(acquisition.getTransitionListFile());
		analyisSoftwareXML.setTargetList(acquisition.getTargetList());
		return analyisSoftwareXML;
	}

}
