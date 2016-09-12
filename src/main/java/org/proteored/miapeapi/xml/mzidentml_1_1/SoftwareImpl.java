package org.proteored.miapeapi.xml.mzidentml_1_1;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.interfaces.Software;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractContact;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

public class SoftwareImpl implements Software {
	private final AnalysisSoftware xmlSoftware;
	private final Integer identifier;
	private final ControlVocabularyManager cvManager;

	public SoftwareImpl(AnalysisSoftware softwareXML, Integer identifier,
			ControlVocabularyManager cvManager) {
		this.xmlSoftware = softwareXML;
		this.identifier = identifier;
		this.cvManager = cvManager;
	}

	@Override
	public String getCatalogNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomizations() {
		final String customizations = xmlSoftware.getCustomizations();
		if (customizations != null && !"".equals(customizations))
			return customizations;
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getManufacturer() {
		if (xmlSoftware.getContactRole() != null) {
			AbstractContact contact = xmlSoftware.getContactRole().getContact();
			if (contact != null)
				return contact.getName();

		}
		return null;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		if (xmlSoftware.getSoftwareName() != null) {
			CvParam cvParam = xmlSoftware.getSoftwareName().getCvParam();
			if (cvParam != null) {
				final ControlVocabularyTerm cvTermByAccession = SoftwareName
						.getInstance(cvManager).getCVTermByAccession(
								new Accession(cvParam.getAccession()));
				if (cvTermByAccession != null)

					return cvParam.getName();

			}
			UserParam userParam = xmlSoftware.getSoftwareName().getUserParam();
			if (userParam != null) {
				return userParam.getName();
			}
		}
		return xmlSoftware.getName();
	}

	@Override
	public String getParameters() {
		return null;
	}

	@Override
	public String getURI() {
		return xmlSoftware.getUri();
	}

	@Override
	public String getVersion() {
		return xmlSoftware.getVersion();
	}

	@Override
	public String toString() {
		return "SoftwareImpl [getCatalogNumber()=" + getCatalogNumber()
				+ ", getComments()=" + getComments() + ", getCustomizations()="
				+ getCustomizations() + ", getDescription()="
				+ getDescription() + ", getManufacturer()=" + getManufacturer()
				+ ", getModel()=" + getModel() + ", getName()=" + getName()
				+ ", getParameters()=" + getParameters() + ", getUri()="
				+ getURI() + ", getVersion()=" + getVersion() + "]";
	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

}
