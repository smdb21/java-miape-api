package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.interfaces.Software;

public class XTandemSoftwareImpl implements Software {

	private final String version;
	private final Integer identifier;
	private final ControlVocabularyManager cvManager;

	public XTandemSoftwareImpl(String procVersion, Integer softwareID,
			ControlVocabularyManager cvManager) {
		this.version = procVersion;
		this.identifier = softwareID;
		this.cvManager = cvManager;
	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getName() {
		final ControlVocabularyTerm xTandemTerm = SoftwareName.getXTandemTerm(cvManager);
		if (xTandemTerm != null)
			return xTandemTerm.getPreferredName();
		return "X!Tandem";
	}

	@Override
	public String getManufacturer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameters() {
		return "see input parameters section";
	}

	@Override
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatalogNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomizations() {
		// TODO Auto-generated method stub
		return null;
	}

}
