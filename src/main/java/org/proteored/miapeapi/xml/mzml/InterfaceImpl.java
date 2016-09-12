package org.proteored.miapeapi.xml.mzml;

import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class InterfaceImpl implements Equipment {
	public final static String INTERFACE = "Interface";
	private final String[] INTERFACE_NAME_TEXT_LIST = { "interface", "interface name", "inlet" }; // use
	// EXACT
	private final String[] INTERFACE_VERSION_TEXT_LIST = { "interface version" };
	private final String[] INTERFACE_MANUFACTURER_TEXT_LIST = { "interface manufacturer" };
	private final String[] INTERFACE_MODEL_TEXT_LIST = { "interface model" };
	private final String[] INTERFACE_DESCRIPTION_TEXT_LIST = { "interface description" };
	private final String[] INTERFACE_CATALOG_TEXT_LIST = { "interface catalog",
			"interface serial number" };
	private final String[] INTERFACE_URI_TEXT_LIST = { "interface uri" };

	private String name = null;
	private String version = null;
	private String manufacturer = null;
	private String model = null;
	private String description = null;
	private String catalogNumber = null;
	private String uri = null;

	public InterfaceImpl(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList) {
		this.name = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, INTERFACE_NAME_TEXT_LIST, MatchMode.EXACT);
		if (this.name == null) {
			this.name = INTERFACE;
		}
		// TODO add parsing of the new CV term when available
		this.manufacturer = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, INTERFACE_MANUFACTURER_TEXT_LIST,
				MatchMode.ANYWHERE);
		// TODO add parsing of the new CV term when available
		this.model = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, INTERFACE_MODEL_TEXT_LIST, MatchMode.ANYWHERE);
		this.description = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, INTERFACE_DESCRIPTION_TEXT_LIST,
				MatchMode.ANYWHERE);
		this.catalogNumber = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, INTERFACE_CATALOG_TEXT_LIST,
				MatchMode.ANYWHERE);
		this.uri = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, INTERFACE_URI_TEXT_LIST, MatchMode.ANYWHERE);
		this.version = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, INTERFACE_VERSION_TEXT_LIST, MatchMode.ANYWHERE);
	}

	public String getFullDescription() {
		StringBuilder sb = new StringBuilder();
		if (this.name != null && !INTERFACE.equals(this.name))
			sb.append(name);
		if (this.manufacturer != null)
			sb.append(manufacturer);
		if (this.model != null)
			sb.append(model);
		if (this.description != null)
			sb.append(description);
		if (this.catalogNumber != null)
			sb.append(catalogNumber);
		if (this.uri != null)
			sb.append(uri);
		return sb.toString();
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getManufacturer() {
		return this.manufacturer;
	}

	@Override
	public String getModel() {
		return this.model;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatalogNumber() {
		return this.catalogNumber;
	}

	@Override
	public String getUri() {
		return this.uri;
	}
}
