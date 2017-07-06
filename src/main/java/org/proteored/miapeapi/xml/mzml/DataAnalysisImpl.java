package org.proteored.miapeapi.xml.mzml;

import java.util.List;
import java.util.Map;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.DataProcessingParameters;
import org.proteored.miapeapi.cv.ms.InstrumentVendor;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.cv.ms.SoftwareManufacturer;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import gnu.trove.map.hash.THashMap;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ProcessingMethod;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.Software;

public class DataAnalysisImpl implements DataAnalysis {
	private final String[] PARAMETERS_FILE_TEXT_LIST = { "file" };
	private final String[] MANUFACTURER_TEXT_LIST = { "manufacturer", "vendor" };
	private final String[] CATALOG_TEXT_LIST = { "catalog", "serial number" };
	private final String[] URI_TEXT_LIST = { "uri", "ref", "web", "http" };
	private final String[] PARAMETERS_TEXT_LIST = { "parameters" };
	private final String[] NAME_TEXT_LIST = { "name", "AcquisitionProgram" };

	private final Accession SOFTWARE_VENDOR_CV = new Accession("MS:1001267");

	private String version = null;
	private String name = null;
	private String manufacturer = null;
	private String catalogNumber = null;
	private String uri = null;
	private String parameters = null;
	private String description = null;
	private String parametersLocation = null;

	public DataAnalysisImpl(ProcessingMethod processingMethod, Software software,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularyManager cvManager) {

		Map<String, String> diccSoftware = new THashMap<String, String>();
		ParamGroup paramGroupProcessingMethod = null;
		ParamGroup paramGroupSoftware = null;
		if (processingMethod != null) {
			paramGroupProcessingMethod = MzMLControlVocabularyXmlFactory.createParamGroup(processingMethod.getCvParam(),
					processingMethod.getUserParam(), processingMethod.getReferenceableParamGroupRef());
		}
		if (software != null) {
			paramGroupSoftware = MzMLControlVocabularyXmlFactory.createParamGroup(software.getCvParam(),
					software.getUserParam(), software.getReferenceableParamGroupRef());
		}
		// merge the param groups
		ParamGroup commonParamGroup = MzMLControlVocabularyXmlFactory.mergeParamGroups(paramGroupProcessingMethod,
				paramGroupSoftware, referenceableParamGroupList);

		// version
		if (software != null) {
			this.version = software.getVersion();
			diccSoftware.put(version, version);
		}
		// description
		this.description = MzMLControlVocabularyXmlFactory.getFullCVsFromParamGroup(commonParamGroup,
				referenceableParamGroupList, DataTransformation.getInstance(cvManager));
		diccSoftware.put(this.description, this.description);

		// name
		this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(commonParamGroup,
				referenceableParamGroupList, SoftwareName.getInstance(cvManager));
		if (name == null) {
			this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(commonParamGroup,
					referenceableParamGroupList, NAME_TEXT_LIST, MatchMode.ANYWHERE);
		}
		if (name == null)
			this.name = "Data analysis";
		else
			diccSoftware.put(name, name);

		// manufacturer / vendor
		this.manufacturer = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(commonParamGroup,
				referenceableParamGroupList, SoftwareManufacturer.getInstance(cvManager));
		if (this.manufacturer == null)
			this.manufacturer = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(commonParamGroup,
					referenceableParamGroupList, SOFTWARE_VENDOR_CV);
		if (this.manufacturer == null)
			this.manufacturer = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(commonParamGroup,
					referenceableParamGroupList, InstrumentVendor.getInstance(cvManager));
		if (this.manufacturer == null)
			this.manufacturer = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(commonParamGroup,
					referenceableParamGroupList, MANUFACTURER_TEXT_LIST, MatchMode.ANYWHERE);
		diccSoftware.put(manufacturer, manufacturer);

		// catalog number
		this.catalogNumber = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(commonParamGroup,
				referenceableParamGroupList, CATALOG_TEXT_LIST, MatchMode.ANYWHERE);
		diccSoftware.put(catalogNumber, catalogNumber);

		// uri / reference
		this.uri = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(commonParamGroup,
				referenceableParamGroupList, URI_TEXT_LIST, MatchMode.ANYWHERE);
		diccSoftware.put(uri, uri);

		// parameters file
		this.parametersLocation = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(commonParamGroup,
				referenceableParamGroupList, MSFileType.PARAMETER_FILE_CV);
		if (this.parametersLocation == null)
			this.parametersLocation = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(commonParamGroup,
					referenceableParamGroupList, PARAMETERS_FILE_TEXT_LIST, MatchMode.ANYWHERE);
		diccSoftware.put(parametersLocation, parametersLocation);

		// parameters
		StringBuilder sb = new StringBuilder();

		// data processing parameters
		List<CVParam> dataProcessingCVs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(commonParamGroup,
				referenceableParamGroupList, DataProcessingParameters.getInstance(cvManager));
		if (dataProcessingCVs != null) {
			for (CVParam cvParam : dataProcessingCVs) {
				if (!"".equals(sb.toString()))
					sb.append("\n");
				final String parsedCV = MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam);
				diccSoftware.put(parsedCV, parsedCV);
				sb.append(parsedCV);
			}
		}

		// by text
		if (!"".equals(sb.toString())) {
			final String valueFromParamGroupByName = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
					commonParamGroup, referenceableParamGroupList, PARAMETERS_TEXT_LIST,

					MatchMode.ANYWHERE);
			if (valueFromParamGroupByName != null)
				sb.append(valueFromParamGroupByName);
		}

		String parametersTemp = MzMLControlVocabularyXmlFactory.parseAllParams(commonParamGroup,
				referenceableParamGroupList, diccSoftware);
		if (parametersTemp != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append(parametersTemp);
		}
		if (!"".equals(sb.toString()))
			this.parameters = sb.toString();
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

		return null;
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
		return null;
	}

	@Override
	public String getCatalogNumber() {
		return this.catalogNumber;
	}

	@Override
	public String getURI() {
		return this.uri;
	}

	@Override
	public String getCustomizations() {
		return null;
	}

	@Override
	public String getParametersLocation() {
		return this.parametersLocation;
	}

}
