package org.proteored.miapeapi.xml.mzml;

import java.util.HashMap;
import java.util.List;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.AcquisitionParameters;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.cv.ms.SoftwareManufacturer;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.ScanSettings;
import uk.ac.ebi.jmzml.model.mzml.ScanSettingsList;
import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.model.mzml.SourceFile;
import uk.ac.ebi.jmzml.model.mzml.SourceFileList;
import uk.ac.ebi.jmzml.model.mzml.SourceFileRef;
import uk.ac.ebi.jmzml.model.mzml.SourceFileRefList;
import uk.ac.ebi.jmzml.model.mzml.TargetList;

public class AcquisitionImpl implements Acquisition {
	private final String[] ACQUISITION_PARAMETERS_FILE_TEXT_LIST = { "file" };
	private final String[] ACQUISITION_TRANSITION_FILE_TEXT_LIST = { "transition file" };

	private final String[] ACQUISITION_MANUFACTURER_TEXT_LIST = { "manufacturer", "vendor" };
	private final String[] ACQUISITION_CATALOG_TEXT_LIST = { "catalog", "serial number" };
	private final String[] ACQUISITION_URI_TEXT_LIST = { "uri", "ref", "web", "http" };
	private final String[] ACQUISITION_PARAMETERS_TEXT_LIST = { "parameters" };
	private final String[] ACQUISITION_NAME_TEXT_LIST = { "name", "AcquisitionProgram" };
	private final Accession SOFTWARE_VENDOR_CV = new Accession("MS:1001267");

	private String version = null;
	private String name = null;
	private String manufacturer = null;
	private String description = null;
	private String catalogNumber = null;
	private String parameters = null;
	private String parametersFile = null;
	private String transitionFile = null;
	private String uri = null;
	private String targetList = null;
	private final ControlVocabularyManager cvManager;

	public AcquisitionImpl(Software software, ScanSettingsList scanSettingsList,
			SourceFileList sourceFileList, ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;

		HashMap<String, String> diccSoftware = new HashMap<String, String>();
		HashMap<String, String> diccScanSettings = new HashMap<String, String>();
		if (software != null) {
			ParamGroup softwareParamGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					software.getCvParam(), software.getUserParam(),
					software.getReferenceableParamGroupRef());

			// version
			this.version = software.getVersion();
			diccSoftware.put(version, version);

			// name
			this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(softwareParamGroup,
					referenceableParamGroupList, SoftwareName.getInstance(cvManager));
			if (name == null) {
				this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
						softwareParamGroup, referenceableParamGroupList,
						ACQUISITION_NAME_TEXT_LIST, MatchMode.ANYWHERE);
			}
			if (name == null)
				this.name = "Acquisition";
			else
				diccSoftware.put(name, name);

			// manufacturer / vendor
			this.manufacturer = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(
					softwareParamGroup, referenceableParamGroupList,
					SoftwareManufacturer.getInstance(cvManager));
			if (this.manufacturer == null)
				this.manufacturer = MzMLControlVocabularyXmlFactory
						.getValueFromParamGroupByAccession(softwareParamGroup,
								referenceableParamGroupList, SOFTWARE_VENDOR_CV);
			if (this.manufacturer == null)
				this.manufacturer = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
						softwareParamGroup, referenceableParamGroupList,
						ACQUISITION_MANUFACTURER_TEXT_LIST, MatchMode.ANYWHERE);
			diccSoftware.put(manufacturer, manufacturer);

			// catalog number
			this.catalogNumber = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
					softwareParamGroup, referenceableParamGroupList, ACQUISITION_CATALOG_TEXT_LIST,
					MatchMode.ANYWHERE);
			diccSoftware.put(catalogNumber, catalogNumber);

			// uri / reference
			this.uri = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
					softwareParamGroup, referenceableParamGroupList, ACQUISITION_URI_TEXT_LIST,
					MatchMode.ANYWHERE);
			diccSoftware.put(uri, uri);

			// parameters
			StringBuilder sb = new StringBuilder();
			List<CVParam> cvList = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
					softwareParamGroup, referenceableParamGroupList,
					AcquisitionParameters.getInstance(cvManager));
			if (cvList != null) {
				for (CVParam cvParam : cvList) {
					if (!"".equals(sb.toString()))
						sb.append("\n");
					sb.append(MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam));
				}
			}
			if (!"".equals(sb.toString()))
				this.parameters = sb.toString();
			if (this.parameters == null)
				this.parameters = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
						softwareParamGroup, referenceableParamGroupList,
						ACQUISITION_PARAMETERS_TEXT_LIST, MatchMode.ANYWHERE);

			// transition file
			this.transitionFile = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
					softwareParamGroup, referenceableParamGroupList,
					ACQUISITION_TRANSITION_FILE_TEXT_LIST, MatchMode.ANYWHERE);

			// description
			this.description = MzMLControlVocabularyXmlFactory.parseAllParams(softwareParamGroup,
					referenceableParamGroupList, diccSoftware);
		}

		// It is supposed that in the instrument configurator should be a
		// reference to the scanSettings, but anyway, we are going to parse all
		// in the same acquisition
		if (scanSettingsList != null) {
			for (ScanSettings scanSettings : scanSettingsList.getScanSettings()) {

				ParamGroup scanSettingParamGroup = MzMLControlVocabularyXmlFactory
						.createParamGroup(scanSettings.getCvParam(), scanSettings.getUserParam(),
								scanSettings.getReferenceableParamGroupRef());
				// parameters file
				final SourceFileRefList sourceFileRefList = scanSettings.getSourceFileRefList();
				if (sourceFileRefList != null && sourceFileRefList.getCount().longValue() > 0) {
					for (SourceFileRef sourceFileRef : sourceFileRefList.getSourceFileRef()) {
						// SourceFile sf = sourceFileRef.getRef();
						String sourceFileid = sourceFileRef.getRef();
						SourceFile sourceFile = searchSourceFileInSourceFileList(sourceFileid,
								sourceFileList);
						if (sourceFile != null)
							this.parametersFile = sourceFile.getName() + " in "
									+ sourceFile.getLocation();
					}

				}
				if (this.parametersFile == null) {
					this.parametersFile = MzMLControlVocabularyXmlFactory
							.getValueFromParamGroupByAccession(scanSettingParamGroup,
									referenceableParamGroupList, MSFileType.PARAMETER_FILE_CV);
				}
				if (this.parametersFile == null) {
					this.parametersFile = MzMLControlVocabularyXmlFactory
							.getValueFromParamGroupByName(scanSettingParamGroup,
									referenceableParamGroupList,
									ACQUISITION_PARAMETERS_FILE_TEXT_LIST, MatchMode.ANYWHERE);
				}
				diccScanSettings.put(parametersFile, parametersFile);

				String transitionFileTemp = MzMLControlVocabularyXmlFactory
						.getValueFromParamGroupByName(scanSettingParamGroup,
								referenceableParamGroupList, ACQUISITION_TRANSITION_FILE_TEXT_LIST,
								MatchMode.ANYWHERE);
				if (this.transitionFile != null) {
					diccScanSettings.put(this.transitionFile, this.transitionFile);
					diccScanSettings.put(transitionFileTemp, transitionFileTemp);
					this.transitionFile += "\n" + transitionFileTemp;
				} else {
					this.transitionFile = transitionFileTemp;
					diccScanSettings.put(transitionFileTemp, transitionFileTemp);
				}

				// target list
				StringBuilder sb = new StringBuilder();
				TargetList mzMLTargetList = scanSettings.getTargetList();
				if (mzMLTargetList != null) {
					if (mzMLTargetList.getTarget() != null) {
						for (ParamGroup targetParamGroup : mzMLTargetList.getTarget()) {
							if (!"".equals(sb.toString()))
								sb.append("\n");
							sb.append(MzMLControlVocabularyXmlFactory.parseAllParams(
									targetParamGroup, referenceableParamGroupList, null));
						}
					}
				}
				if (!"".equals(sb.toString()))
					this.targetList = sb.toString();

				// parameters
				String paramTemp = MzMLControlVocabularyXmlFactory.parseAllParams(
						scanSettingParamGroup, referenceableParamGroupList, diccScanSettings);
				if (paramTemp != null)
					if (parameters != null)
						parameters = parameters + "\n" + paramTemp;
					else
						parameters = paramTemp;

			}
		}
	}

	private SourceFile searchSourceFileInSourceFileList(String sourceFileid,
			SourceFileList sourceFileList) {
		if (sourceFileid == null || "".equals(sourceFileid) || sourceFileList == null
				|| sourceFileList.getCount().longValue() <= 0)
			return null;
		for (SourceFile sourceFile : sourceFileList.getSourceFile()) {
			if (sourceFile.getId().equals(sourceFileid))
				return sourceFile;
		}
		return null;
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
		return uri;
	}

	@Override
	public String getCustomizations() {
		return null;
	}

	@Override
	public String getParameterFile() {
		return this.parametersFile;
	}

	@Override
	public String getTransitionListFile() {
		return this.transitionFile;
	}

	@Override
	public String getTargetList() {
		return this.targetList;
	}

}
