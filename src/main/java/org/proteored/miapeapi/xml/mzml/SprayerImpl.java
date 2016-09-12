package org.proteored.miapeapi.xml.mzml;

import java.util.List;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.InletType;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class SprayerImpl implements Equipment {
	public final static String SPRAYER = "Sprayer";

	private final String[] SPRAYER_NAME_TEXT_LIST = { "sprayer", "sprayer name", "inlet" }; // use
																							// EXACT
	private final String[] SPRAYER_VERSION_TEXT_LIST = { "sprayer version" };
	private final String[] SPRAYER_MANUFACTURER_TEXT_LIST = { "sprayer manufacturer" };
	private final String[] SPRAYER_MODEL_TEXT_LIST = { "sprayer model" };
	private final String[] SPRAYER_DESCRIPTION_TEXT_LIST = { "sprayer description" };
	private final String[] SPRAYER_CATALOG_TEXT_LIST = { "sprayer catalog", "sprayer serial number" };
	private final String[] SPRAYER_URI_TEXT_LIST = { "sprayer uri" };

	private String name = null;
	private String version = null;
	private String manufacturer = null;
	private String model = null;
	private String description = null;
	private String catalogNumber = null;
	private String uri = null;

	private String parameters = null;

	public SprayerImpl(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {

		// Add parsing when new CV term is available
		if (this.name == null)
			this.name = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPRAYER_NAME_TEXT_LIST, MatchMode.EXACT);
		if (this.name == null)
			this.name = this.SPRAYER;
		this.manufacturer = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, SPRAYER_MANUFACTURER_TEXT_LIST,
				MatchMode.ANYWHERE);
		this.model = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPRAYER_MODEL_TEXT_LIST, MatchMode.ANYWHERE);
		this.description = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, SPRAYER_DESCRIPTION_TEXT_LIST,
				MatchMode.ANYWHERE);
		this.catalogNumber = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
				paramGroup, referenceableParamGroupList, SPRAYER_CATALOG_TEXT_LIST,
				MatchMode.ANYWHERE);
		this.uri = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPRAYER_URI_TEXT_LIST, MatchMode.ANYWHERE);
		this.version = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPRAYER_VERSION_TEXT_LIST, MatchMode.ANYWHERE);

		// inlet type
		List<CVParam> inletCVs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(paramGroup,
				referenceableParamGroupList, InletType.getInstance(cvManager));
		if (inletCVs != null) {
			this.parameters = MzMLControlVocabularyXmlFactory.parseAllCvParams(inletCVs, null);
		}
	}

	public String getFullDescription() {
		StringBuilder sb = new StringBuilder();
		if (this.name != null && !SPRAYER.equals(this.name))
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
		return this.parameters;
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
