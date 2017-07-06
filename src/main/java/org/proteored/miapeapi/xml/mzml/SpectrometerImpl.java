package org.proteored.miapeapi.xml.mzml;

import java.util.List;
import java.util.Map;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.AcquisitionParameters;
import org.proteored.miapeapi.cv.ms.InstrumentModel;
import org.proteored.miapeapi.cv.ms.InstrumentVendor;
import org.proteored.miapeapi.cv.ms.IonOpticsType;
import org.proteored.miapeapi.cv.ms.SpectrometerName;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import gnu.trove.map.hash.THashMap;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class SpectrometerImpl implements Spectrometer {

	private String catalogNumber = null;
	private String uri = null;
	private String description = null;
	private String model = null;
	private String manufacturer = null;
	private String name = null;
	private String version = null;
	private String customizations = null;
	private String parameters = null;
	private String comments = null;

	private final String[] SPECTROMETER_NAME_TEXT_LIST = { "spectrometer", "spectrometer name", "name",
			"InstrumentDescription" }; // use EXACT
	private final String[] SPECTROMETER_VERSION_TEXT_LIST = { "version" };
	private final String[] SPECTROMETER_MANUFACTURER_TEXT_LIST = { "manufacturer", "vendor" };
	private final String[] SPECTROMETER_MODEL_TEXT_LIST = { "model" };
	private final String[] SPECTROMETER_DESCRIPTION_TEXT_LIST = { "description" };
	private final String[] SPECTROMETER_CATALOG_TEXT_LIST = { "catalog" };
	private final String[] SPECTROMETER_URI_TEXT_LIST = { "uri", "reference" };
	private final String[] SPECTROMETER_CUSTOMIZATIONS_TEXT_LIST = { "customization" };
	private final String[] SPECTROMETER_PARAMETERS_TEXT_LIST = { "parameter" };
	private static final Accession INSTRUMENT_SERIAL_NUMBER_CV = new Accession("MS:1000529");

	public SpectrometerImpl(List<InstrumentConfiguration> instrumentConfigurationList,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularyManager cvManager) {
		Map<String, String> dicc = new THashMap<String, String>();

		// Create a paramGroup
		ParamGroup paramGroup = null;
		ParamGroup paramGroupTMP;
		for (InstrumentConfiguration instrumentConfiguration : instrumentConfigurationList) {
			paramGroupTMP = MzMLControlVocabularyXmlFactory.createParamGroup(instrumentConfiguration.getCvParam(),
					instrumentConfiguration.getUserParam(), instrumentConfiguration.getReferenceableParamGroupRef());
			paramGroup = MzMLControlVocabularyXmlFactory.mergeParamGroups(paramGroup, paramGroupTMP,
					referenceableParamGroupList);
		}

		// name
		this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup, referenceableParamGroupList,
				SpectrometerName.getInstance(cvManager));
		if (this.name == null)
			this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPECTROMETER_NAME_TEXT_LIST, MatchMode.EXACT);
		if (this.name == null)
			this.name = "Spectrometer";
		else
			dicc.put(name, name);

		// customizations
		final CVParam customizationCVTerm = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(paramGroup,
				referenceableParamGroupList, AcquisitionParameters.CUSTOMIZATION_CV);
		if (customizationCVTerm != null)
			this.customizations = customizationCVTerm.getValue();
		else
			this.customizations = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPECTROMETER_CUSTOMIZATIONS_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(customizations, customizations);

		// manufacturer
		this.manufacturer = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
				referenceableParamGroupList, InstrumentVendor.getInstance(cvManager));
		if (this.manufacturer == null)
			this.manufacturer = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPECTROMETER_MANUFACTURER_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(manufacturer, manufacturer);

		// model
		final String valueFromParamGroup = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
				referenceableParamGroupList, InstrumentModel.getInstance(cvManager));

		this.model = valueFromParamGroup;
		if (this.model == null)
			this.model = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPECTROMETER_MODEL_TEXT_LIST, MatchMode.ANYWHERE);
		if (model != null)
			if (model.equals(this.manufacturer))
				model = null;
		dicc.put(model, model);

		// description
		// Disabled to not capture InstrumentDescription=HCTultra ETD II" since
		// it should be captured in the name
		// this.description =
		// MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
		// paramGroup, referenceableParamGroupList,
		// SPECTROMETER_DESCRIPTION_TEXT_LIST,
		// MatchMode.ANYWHERE);
		// dicc.put(description, description);

		// catalog number
		this.catalogNumber = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPECTROMETER_CATALOG_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(catalogNumber, catalogNumber);

		// uri
		this.uri = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPECTROMETER_URI_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(uri, uri);

		// version
		this.version = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(paramGroup,
				referenceableParamGroupList, INSTRUMENT_SERIAL_NUMBER_CV);
		if (this.version == null)
			this.version = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
					referenceableParamGroupList, SPECTROMETER_VERSION_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(version, version);

		// parameters
		this.parameters = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
				referenceableParamGroupList, SPECTROMETER_PARAMETERS_TEXT_LIST, MatchMode.ANYWHERE);
		dicc.put(parameters, parameters);

		// if there is a CV that belongs to a MS_ION_OPTICS_NAME section, do not
		// include it in the Spectrometer section, so add it to the dicc
		String ionOpticsName = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
				referenceableParamGroupList, IonOpticsType.getInstance(cvManager));
		if (ionOpticsName != null)
			dicc.put(ionOpticsName, ionOpticsName);

		this.comments = MzMLControlVocabularyXmlFactory.parseAllParams(paramGroup, referenceableParamGroupList, dicc);
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
		return this.comments;
	}

	@Override
	public String getCatalogNumber() {
		return this.catalogNumber;
	}

	@Override
	public String getUri() {
		return this.uri;
	}

	@Override
	public String getCustomizations() {
		return this.customizations;
	}

}
