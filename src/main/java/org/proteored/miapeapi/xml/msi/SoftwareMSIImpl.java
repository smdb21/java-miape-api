package org.proteored.miapeapi.xml.msi;

import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.xml.msi.autogenerated.MSISoftwareType;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class SoftwareMSIImpl implements Software {
	private final MSISoftwareType xmlSoftware;

	public SoftwareMSIImpl(MSISoftwareType xmlSoftware) {
		this.xmlSoftware = xmlSoftware;
	}

	@Override
	public String getCatalogNumber() {
		return xmlSoftware.getCatalogNumber();
	}

	@Override
	public String getComments() {
		return xmlSoftware.getComments();
	}

	@Override
	public String getCustomizations() {
		return xmlSoftware.getCustomizations();
	}

	@Override
	public String getDescription() {
		String ret = null;
		final String name = MSIControlVocabularyXmlFactory.getName(this.xmlSoftware.getDescription());
		final String value = MSIControlVocabularyXmlFactory.getValue(this.xmlSoftware.getDescription());
		if (name != null)
			ret = name;
		if (value != null) {
			if (ret != null)
				ret += " = ";
			ret += value;
		}
		return ret;

	}

	@Override
	public String getManufacturer() {
		return xmlSoftware.getManufacturer();
	}

	@Override
	public String getModel() {
		return xmlSoftware.getModel();
	}

	@Override
	public String getName() {
		return MSIControlVocabularyXmlFactory.getName(xmlSoftware.getName());
	}

	@Override
	public String getParameters() {
		return xmlSoftware.getParameters();
	}

	@Override
	public String getURI() {
		return xmlSoftware.getURI();
	}

	@Override
	public String getVersion() {
		return xmlSoftware.getVersion();
	}

	@Override
	public int getId() {
		return MiapeXmlUtil.getIdFromXMLId(xmlSoftware.getId());
	}

}
