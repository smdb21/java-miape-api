package org.proteored.miapeapi.xml.mzml;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.DissociationMethod;
import org.proteored.miapeapi.cv.ms.GasType;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.ms.DataAnalysisBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.ms.MiapeMSXmlFactory;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzml.MzMLElement;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.Chromatogram;
import uk.ac.ebi.jmzml.model.mzml.DataProcessing;
import uk.ac.ebi.jmzml.model.mzml.DataProcessingList;
import uk.ac.ebi.jmzml.model.mzml.FileDescription;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfigurationList;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.Precursor;
import uk.ac.ebi.jmzml.model.mzml.PrecursorList;
import uk.ac.ebi.jmzml.model.mzml.ProcessingMethod;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.Run;
import uk.ac.ebi.jmzml.model.mzml.Sample;
import uk.ac.ebi.jmzml.model.mzml.SampleList;
import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;
import uk.ac.ebi.jmzml.model.mzml.ScanSettings;
import uk.ac.ebi.jmzml.model.mzml.ScanSettingsList;
import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.model.mzml.SoftwareList;
import uk.ac.ebi.jmzml.model.mzml.SourceFile;
import uk.ac.ebi.jmzml.model.mzml.SourceFileList;
import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.model.mzml.UserParam;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

public class MiapeMSDocumentImpl implements MiapeMSDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final MzMLUnmarshaller mzMLUnmarshaller;
	private final MzML mzML;
	private final User user;
	protected ControlVocabularyManager cvManager;
	private PersistenceManager dbManager;
	private final String projectName;
	private MSContact contact;
	private int id = -1;
	private final Set<Maldi> maldis = new THashSet<Maldi>();
	private final Set<Esi> esis = new THashSet<Esi>();
	private final Set<Other_IonSource> otherIonSources = new THashSet<Other_IonSource>();
	private final List<Analyser> analyzers = new ArrayList<Analyser>();
	private final Set<Spectrometer> spectrometers = new THashSet<Spectrometer>();
	// private final Set<IonOptic> ionOptics = new THashSet<IonOptic>();
	private final Set<Acquisition> acquisitions = new THashSet<Acquisition>();
	private final Set<ActivationDissociation> collisionCells = new THashSet<ActivationDissociation>();
	private final List<org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration> instrumentConfigurations = new ArrayList<org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration>();
	private Set<DataAnalysis> dataAnalysises = new THashSet<DataAnalysis>();
	private final List<ResultingData> resultingDatas = new ArrayList<ResultingData>();
	private final List<MSAdditionalInformation> additionalInformations = new ArrayList<MSAdditionalInformation>();
	private String standardXMLLocation = null;
	private final Map<String, String> diccSourceFiles = new THashMap<String, String>();
	private MiapeDate date;

	// Elements from mzML
	private FileDescription fileDescriptor;
	private ReferenceableParamGroupList referenceableParamGroupList; // optional
	private SoftwareList softwareList;
	private InstrumentConfigurationList instrumentConfigurationList;
	private DataProcessingList dataProcessingList;
	private ScanSettingsList scanSettingsList; // optional
	private SampleList sampleList; // optional
	private SourceFileList sourceFileList; // optional
	private ParamGroup fileContent;
	private MzMLObjectIterator<Spectrum> spectrumIterator;
	private MzMLObjectIterator<Chromatogram> chromatogramIterator;
	private Map<String, String> runAttributes;
	private final String mzMLFileName;

	public MiapeMSDocumentImpl(MzMLUnmarshaller mzMLUnmarshaller, ControlVocabularyManager cvManager,
			String mzMLFileName, String projectName) {
		mzML = null;
		this.mzMLUnmarshaller = mzMLUnmarshaller;
		user = null;
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.mzMLFileName = mzMLFileName;
		processMzML();
	}

	public MiapeMSDocumentImpl(MzML mzML, ControlVocabularyManager cvManager, String mzMLFileName, String projectName) {
		this.mzML = mzML;
		mzMLUnmarshaller = null;
		user = null;
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.mzMLFileName = mzMLFileName;

		processMzML();
	}

	public MiapeMSDocumentImpl(MzMLUnmarshaller mzMLUnmarshaller, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String user, String password, String mzMLFileName, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException {
		mzML = null;
		this.mzMLUnmarshaller = mzMLUnmarshaller;
		this.user = databaseManager.getUser(user, password);
		this.cvManager = cvManager;
		this.projectName = projectName;
		dbManager = databaseManager;
		this.mzMLFileName = mzMLFileName;

		processMzML();
	}

	public MiapeMSDocumentImpl(MzML mzML, PersistenceManager databaseManager, ControlVocabularyManager cvManager,
			String user, String password, String mzMLFileName, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException {
		this.mzML = mzML;
		mzMLUnmarshaller = null;
		this.user = databaseManager.getUser(user, password);
		this.cvManager = cvManager;
		this.projectName = projectName;
		dbManager = databaseManager;
		this.mzMLFileName = mzMLFileName;

		processMzML();
	}

	private void processMzML() {
		// clear static identifier counters
		MiapeXmlUtil.clearIdentifierCounters();
		log.info("processing elements");
		if (mzMLUnmarshaller != null)
			parseMzMLElementsByUnmarshaller();
		else
			parseMzMLElementsbyMzML();
		// contact
		processContact(fileDescriptor.getContact(), referenceableParamGroupList);
		// file content
		processFileContent(fileContent, referenceableParamGroupList);
		// InstrumentConfigurationList
		processInstrumentConfigurations(instrumentConfigurationList, softwareList, sourceFileList, scanSettingsList,
				referenceableParamGroupList);

		// DataProcessing list
		// Note, this call has to be necessarily after the
		// processInstrumentConfigurations in order to avoid redundancies over
		// the acquisition software and the processing software
		processDataProcessings(dataProcessingList, softwareList, referenceableParamGroupList);

		// run
		processRunHeader(runAttributes, sourceFileList, sampleList, referenceableParamGroupList);
		// spectrums and chromatograms
		processSpectrumsAndChromatograms(spectrumIterator, chromatogramIterator, referenceableParamGroupList,
				sourceFileList);
		log.info("end processing elements");
	}

	private void parseMzMLElementsbyMzML() {
		log.info("Parsing elements from mzML using mzML object");
		// File descriptor (fileContent, sourceFileList and Contacts)
		fileDescriptor = mzML.getFileDescription();
		// source file list
		sourceFileList = fileDescriptor.getSourceFileList();
		// file content
		fileContent = fileDescriptor.getFileContent();
		// ReferenceableParamGroup
		referenceableParamGroupList = mzML.getReferenceableParamGroupList();
		// Software list
		softwareList = mzML.getSoftwareList();
		// ScanSettings list
		scanSettingsList = mzML.getScanSettingsList();
		// InstrumentConfigurationList
		instrumentConfigurationList = mzML.getInstrumentConfigurationList();
		// DataProcessing list
		dataProcessingList = mzML.getDataProcessingList();
		// Sample list
		sampleList = mzML.getSampleList();
		// run attributes
		final Run run = mzML.getRun();
		if (run != null) {
			final Calendar startTimeStamp = run.getStartTimeStamp();
			if (startTimeStamp != null)
				runAttributes.put("StartTimeStamp", startTimeStamp.toString());
			final String sampleRef = run.getSampleRef();
			if (sampleRef != null)
				runAttributes.put("sampleRef", sampleRef);
			final String defaultSourceFileRef = run.getDefaultSourceFileRef();
			if (defaultSourceFileRef != null)
				runAttributes.put("defaultSourceFileRef", defaultSourceFileRef);
		}
		// run
		// this.run = null;
		// spectrums and chromatograms
		spectrumIterator = null;
		chromatogramIterator = null;

		log.info("parsing done");

	}

	private void parseMzMLElementsByUnmarshaller() {
		log.info("Parsing elements from mzML using unmarshaller");
		// File descriptor (fileContent, sourceFileList and Contacts)
		fileDescriptor = mzMLUnmarshaller.unmarshalFromXpath(MzMLElement.FileDescription.getXpath(),
				FileDescription.class);
		// source file list
		sourceFileList = fileDescriptor.getSourceFileList();
		// file content
		fileContent = fileDescriptor.getFileContent();
		// ReferenceableParamGroup
		referenceableParamGroupList = mzMLUnmarshaller.unmarshalFromXpath(
				MzMLElement.ReferenceableParamGroupList.getXpath(), ReferenceableParamGroupList.class);
		// Software list
		softwareList = mzMLUnmarshaller.unmarshalFromXpath(MzMLElement.SoftwareList.getXpath(), SoftwareList.class);
		// ScanSettings list
		scanSettingsList = mzMLUnmarshaller.unmarshalFromXpath(MzMLElement.ScanSettingsList.getXpath(),
				ScanSettingsList.class);
		// InstrumentConfigurationList
		instrumentConfigurationList = mzMLUnmarshaller.unmarshalFromXpath(
				MzMLElement.InstrumentConfigurationList.getXpath(), InstrumentConfigurationList.class);
		// DataProcessing list
		dataProcessingList = mzMLUnmarshaller.unmarshalFromXpath(MzMLElement.DataProcessingList.getXpath(),
				DataProcessingList.class);
		// Sample list
		sampleList = mzMLUnmarshaller.unmarshalFromXpath(MzMLElement.SampleList.getXpath(), SampleList.class);
		// run

		// this.run = mzMLUnmarshaller.unmarshalFromXpath("/run", Run.class);
		runAttributes = mzMLUnmarshaller.getSingleElementAttributes(MzMLElement.Run.getXpath());

		// spectrums and chromatograms
		spectrumIterator = mzMLUnmarshaller.unmarshalCollectionFromXpath(MzMLElement.Spectrum.getXpath(),
				Spectrum.class);
		chromatogramIterator = mzMLUnmarshaller.unmarshalCollectionFromXpath(MzMLElement.Chromatogram.getXpath(),
				Chromatogram.class);

		log.info("parsing done");
	}

	private void processFileContent(ParamGroup fileContent, ReferenceableParamGroupList referenceableParamGroupList) {
		addAdditionalInformationsFromParamGroup(fileContent, referenceableParamGroupList);
	}

	private void processRunHeader(Map<String, String> runAttributes, SourceFileList sourceFileList,
			SampleList sampleList, ReferenceableParamGroupList referenceableParamGroupList) {

		log.info("processing run header");
		// Default source file

		if (runAttributes != null && runAttributes.containsKey("defaultSourceFileRef")) {
			// if sourceFileRef!=null, add resulting data
			processSourceFileRef(runAttributes.get("defaultSourceFileRef"), sourceFileList,
					referenceableParamGroupList);
		} else if (sourceFileList != null) {
			// COLLAPSE RESULTING DATA
			List<ResultingData> resultingData = new ArrayList<ResultingData>();

			Map<String, List<ResultingDataImpl>> fileTypes = new THashMap<String, List<ResultingDataImpl>>();
			for (SourceFile sourceFile : sourceFileList.getSourceFile()) {
				if (!diccSourceFiles.containsKey(sourceFile.getId()))
					diccSourceFiles.put(sourceFile.getId(), sourceFile.getId());
				final ResultingDataImpl resData = new ResultingDataImpl(sourceFile, referenceableParamGroupList,
						cvManager);
				final String dataFileType = resData.getDataFileType();
				if (!fileTypes.containsKey(dataFileType)) {
					List<ResultingDataImpl> list = new ArrayList<ResultingDataImpl>();
					list.add(resData);
					fileTypes.put(dataFileType, list);
				} else {
					fileTypes.get(dataFileType).add(resData);
				}
			}
			for (List<ResultingDataImpl> resultingDatas : fileTypes.values()) {
				final ResultingDataImpl resData = resultingDatas.get(0);
				resData.setName(resultingDatas.size() + " resulting data files");
				this.resultingDatas.add(resData);
			}

		}
		// Start time
		if (runAttributes != null && runAttributes.containsKey("startTimeStamp")) {
			try {
				date = new MiapeDate(runAttributes.get("startTimeStamp"));
			} catch (IllegalMiapeArgumentException ex) {
				date = new MiapeDate(System.currentTimeMillis());
			}
		} else {
			date = new MiapeDate(System.currentTimeMillis());
		}

		// Samples
		// if (sampleList != null && sampleList.getSample() != null) {
		// for (Sample sample : sampleList.getSample()) {
		// this.additionalInformations.add(new
		// AdditionalInformationImpl(sample.getName()));
		// ParamGroup paramGroup =
		// MzMLControlVocabularyXmlFactory.createParamGroup(
		// sample.getCvParam(), sample.getUserParam(),
		// sample.getReferenceableParamGroupRef());
		// addAdditionalInformationsFromParamGroup(paramGroup,
		// referenceableParamGroupList);
		// }
		// }
		if (runAttributes != null && runAttributes.containsKey("sampleRef")) {
			Sample sample = searchSampleInSampleList(runAttributes.get("sampleRef"), sampleList);
			additionalInformations.add(new AdditionalInformationImpl(sample.getName()));
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(sample.getCvParam(),
					sample.getUserParam(), sample.getReferenceableParamGroupRef());
			addAdditionalInformationsFromParamGroup(paramGroup, referenceableParamGroupList);

		}
		log.info("end processing run header");

	}

	private void addAdditionalInformationsFromParamGroup(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing add info from param group");
		for (CVParam cvParam : paramGroup.getCvParam()) {
			additionalInformations.add(new AdditionalInformationImpl(cvParam));
		}
		for (UserParam userParam : paramGroup.getUserParam()) {
			additionalInformations.add(new AdditionalInformationImpl(userParam));
		}
		for (ReferenceableParamGroupRef paramRef : paramGroup.getReferenceableParamGroupRef()) {
			for (ReferenceableParamGroup refParamGroup : referenceableParamGroupList.getReferenceableParamGroup()) {
				if (refParamGroup.getId().equals(paramRef.getRef())) {
					for (CVParam cvParam : refParamGroup.getCvParam()) {
						additionalInformations.add(new AdditionalInformationImpl(cvParam));
					}
					for (UserParam userParam : refParamGroup.getUserParam()) {
						additionalInformations.add(new AdditionalInformationImpl(userParam));
					}
				}
			}

		}
		log.info("end processing add info from param group");

	}

	private Sample searchSampleInSampleList(String sampleRef, SampleList sampleList) {
		if (sampleRef == null || "".equals(sampleRef) || sampleList == null || sampleList.getCount().longValue() <= 0)
			return null;
		for (Sample sample : sampleList.getSample()) {
			if (sample.getId().equals(sampleRef))
				return sample;
		}
		return null;
	}

	private SourceFile searchSourceFileInSourceFileRefList(String sourceFileRef, SourceFileList sourceFileList) {
		if (sourceFileRef == null || "".equals(sourceFileRef) || sourceFileList == null
				|| sourceFileList.getCount().longValue() <= 0)
			return null;
		for (SourceFile sourceFile : sourceFileList.getSourceFile()) {
			if (sourceFile.getId().equals(sourceFileRef)) {
				return sourceFile;
			}
		}
		return null;
	}

	private void processDataProcessings(DataProcessingList dataProcessingList, SoftwareList softwareList,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing data processings");
		Map<String, String> softwareRefMap = new THashMap<String, String>();
		// for each processing method, create a DataAnalysis
		if (dataProcessingList != null) {
			for (DataProcessing dataProcessing : dataProcessingList.getDataProcessing()) {
				if (dataProcessing.getProcessingMethod() != null && dataProcessing.getProcessingMethod().size() > 0) {
					for (ProcessingMethod processingMethod : dataProcessing.getProcessingMethod()) {
						if (processingMethod.getSoftwareRef() != null)
							softwareRefMap.put(processingMethod.getSoftwareRef(), processingMethod.getSoftwareRef());
						Software processingSoftware = searchSoftwareInSoftwareList(processingMethod.getSoftwareRef(),
								softwareList);
						dataAnalysises.add(new DataAnalysisImpl(processingMethod, processingSoftware,
								referenceableParamGroupList, cvManager));

					}
				}
			}
		}
		// merge dataAnalysis that have the same name
		dataAnalysises = removeRedundantDataAnalysis(dataAnalysises);

		if (softwareList != null && softwareList.getSoftware() != null) {
			// if there is some software that was not in the dataAnalysis and it
			// is not in the acquisition software!, add a
			// new dataAnalysis
			for (Software software : softwareList.getSoftware()) {
				if (!softwareRefMap.containsKey(software.getId())) {
					final DataAnalysis dataAnalysis = new DataAnalysisImpl(null, software, referenceableParamGroupList,
							cvManager);
					boolean include = true;
					if (acquisitions != null) {
						for (Acquisition acquisition : acquisitions) {
							if (dataAnalysis.getName().equals(acquisition.getName()))
								include = false;
						}
					}
					if (include)
						dataAnalysises.add(dataAnalysis);
				}
			}
		}
		log.info("end processing data processings");

	}

	private Set<DataAnalysis> removeRedundantDataAnalysis(Set<DataAnalysis> dataAnalysisSet) {
		Set<DataAnalysis> ret = new THashSet<DataAnalysis>();
		Map<String, DataAnalysis> dataAnalysisDicc = new THashMap<String, DataAnalysis>();
		if (dataAnalysisSet != null) {
			for (DataAnalysis dataAnalysis : dataAnalysisSet) {
				if (!dataAnalysisDicc.containsKey(dataAnalysis.getName())) {
					dataAnalysisDicc.put(dataAnalysis.getName(), dataAnalysis);
				} else {
					DataAnalysis dataAnalysis2 = dataAnalysisDicc.get(dataAnalysis.getName());
					dataAnalysisDicc.remove(dataAnalysis.getName());
					dataAnalysisDicc.put(dataAnalysis.getName(), mergeDataAnalysis(dataAnalysis, dataAnalysis2));
				}
			}
		}
		for (String dataAnalysisName : dataAnalysisDicc.keySet()) {
			ret.add(dataAnalysisDicc.get(dataAnalysisName));
		}
		return ret;
	}

	private DataAnalysis mergeDataAnalysis(DataAnalysis dataAnalysis, DataAnalysis dataAnalysis2) {
		DataAnalysisBuilder ret = MiapeMSDocumentFactory.createDataAnalysisBuilder(dataAnalysis.getName());

		// catalogNumber
		StringBuilder catalogNumber = new StringBuilder();
		if (dataAnalysis.getCatalogNumber() != null)
			append(catalogNumber, dataAnalysis.getCatalogNumber());
		if (dataAnalysis2.getCatalogNumber() != null
				&& !dataAnalysis2.getCatalogNumber().equals(dataAnalysis.getCatalogNumber()))
			append(catalogNumber, dataAnalysis2.getCatalogNumber());
		if (!"".equals(catalogNumber.toString()))
			ret.catalogNumber(catalogNumber.toString());

		// comments
		StringBuilder comments = new StringBuilder();
		if (dataAnalysis.getComments() != null)
			append(comments, dataAnalysis.getComments());
		if (dataAnalysis2.getComments() != null && !dataAnalysis2.getComments().equals(dataAnalysis.getComments()))
			append(comments, dataAnalysis2.getComments());
		if (!"".equals(comments.toString()))
			ret.comments(comments.toString());

		// customizations
		StringBuilder customizations = new StringBuilder();
		if (dataAnalysis.getCustomizations() != null)
			append(customizations, dataAnalysis.getCustomizations());
		if (dataAnalysis2.getCustomizations() != null
				&& !dataAnalysis2.getCustomizations().equals(dataAnalysis.getCustomizations()))
			append(customizations, dataAnalysis2.getCustomizations());
		if (!"".equals(customizations.toString()))
			ret.customizations(customizations.toString());

		// description
		StringBuilder description = new StringBuilder();
		if (dataAnalysis.getDescription() != null)
			append(description, dataAnalysis.getDescription());
		if (dataAnalysis2.getDescription() != null
				&& !dataAnalysis2.getDescription().equals(dataAnalysis.getDescription()))
			append(description, dataAnalysis2.getDescription());
		if (!"".equals(description.toString()))
			ret.description(description.toString());

		// manufacturer
		StringBuilder manufacturer = new StringBuilder();
		if (dataAnalysis.getManufacturer() != null)
			append(manufacturer, dataAnalysis.getManufacturer());
		if (dataAnalysis2.getManufacturer() != null
				&& !dataAnalysis2.getManufacturer().equals(dataAnalysis.getManufacturer()))
			append(manufacturer, dataAnalysis2.getManufacturer());
		if (!"".equals(manufacturer.toString()))
			ret.manufacturer(manufacturer.toString());

		// model
		StringBuilder model = new StringBuilder();
		if (dataAnalysis.getModel() != null)
			append(model, dataAnalysis.getModel());
		if (dataAnalysis2.getModel() != null && !dataAnalysis2.getModel().equals(dataAnalysis.getModel()))
			append(model, dataAnalysis2.getModel());
		if (!"".equals(model.toString()))
			ret.model(model.toString());

		// parameters
		StringBuilder parameters = new StringBuilder();
		if (dataAnalysis.getParameters() != null)
			append(parameters, dataAnalysis.getParameters());
		if (dataAnalysis2.getParameters() != null
				&& !dataAnalysis2.getParameters().equals(dataAnalysis.getParameters()))
			append(parameters, dataAnalysis2.getParameters());
		if (!"".equals(parameters.toString()))
			ret.parameters(parameters.toString());

		// parametersLocation
		StringBuilder parametersLocation = new StringBuilder();
		if (dataAnalysis.getParametersLocation() != null)
			append(parametersLocation, dataAnalysis.getParametersLocation());
		if (dataAnalysis2.getParametersLocation() != null
				&& !dataAnalysis2.getParametersLocation().equals(dataAnalysis.getParametersLocation()))
			append(parametersLocation, dataAnalysis2.getParametersLocation());
		if (!"".equals(parametersLocation.toString()))
			ret.parametersLocation(parametersLocation.toString());

		// uri
		StringBuilder uri = new StringBuilder();
		if (dataAnalysis.getURI() != null)
			append(uri, dataAnalysis.getURI());
		if (dataAnalysis2.getURI() != null && !dataAnalysis2.getURI().equals(dataAnalysis.getURI()))
			append(uri, dataAnalysis2.getURI());
		if (!"".equals(uri.toString()))
			ret.uri(uri.toString());

		// version
		StringBuilder version = new StringBuilder();
		if (dataAnalysis.getVersion() != null)
			append(version, dataAnalysis.getVersion());
		if (dataAnalysis2.getVersion() != null && !dataAnalysis2.getVersion().equals(dataAnalysis.getVersion()))
			append(version, dataAnalysis2.getVersion());
		if (!"".equals(version.toString()))
			ret.version(version.toString());

		return ret.build();
	}

	private void append(StringBuilder stringBuilder, String cadena) {
		if (stringBuilder.toString() != null && !"".equals(stringBuilder.toString()))
			stringBuilder.append("\n");
		stringBuilder.append(cadena);
	}

	private void processSpectrumsAndChromatograms(MzMLObjectIterator<Spectrum> spectrumIterator,
			MzMLObjectIterator<Chromatogram> chromatogramIterator,
			ReferenceableParamGroupList referenceableParamGroupList, SourceFileList sourceFileList) {
		log.info("processing spectrums and chromatograms");

		Map<String, String> diccCollCell = new THashMap<String, String>();
		// TODO by default, the collision cell has this name:
		String name = "collision cell";
		String gasType = null;
		String gasPressure = null;
		String gasPressureUnit = null;
		String activationType = null;
		if (spectrumIterator != null) {

			while (spectrumIterator.hasNext()) {
				// read next spectrum from XML file
				Spectrum spectrum = spectrumIterator.next();

				// if sourceFileRef!=null, add resulting data
				processSourceFileRef(spectrum.getSourceFileRef(), sourceFileList, referenceableParamGroupList);

				// Scans
				final ScanList scanlist = spectrum.getScanList();
				if (scanlist != null) {
					for (Scan scan : scanlist.getScan()) {
						// if sourceFileRef!=null, add resulting data
						processSourceFileRef(scan.getSourceFileRef(), sourceFileList, referenceableParamGroupList);
					}
				}
				// Precursor
				final PrecursorList precursorList = spectrum.getPrecursorList();
				if (precursorList != null && precursorList.getCount().longValue() > 0) {
					for (Precursor precursor : precursorList.getPrecursor()) {
						// if sourceFileRef!=null, add resulting data
						processSourceFileRef(precursor.getSourceFileRef(), sourceFileList, referenceableParamGroupList);

						// initialize variables
						gasType = null;
						gasPressure = null;
						gasPressureUnit = null;
						activationType = null;

						ParamGroup activationParamGroup = precursor.getActivation();
						// activation type
						activationType = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(activationParamGroup,
								referenceableParamGroupList, DissociationMethod.getInstance(cvManager));

						// gas type
						// Firstly, search <cvparam name="collision gas"
						// value"helium"/>
						gasType = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(
								activationParamGroup, referenceableParamGroupList, GasType.COLLISION_GAS);
						// if not found, search a CV allowed in MS_GAS_TYPE
						// section
						if (gasType == null) {
							List<CVParam> gasTypeCVs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
									activationParamGroup, referenceableParamGroupList, GasType.getInstance(cvManager));
							if (gasTypeCVs != null) {
								gasType = MzMLControlVocabularyXmlFactory.parseAllCvParams(gasTypeCVs, null);
							}
						}

						// gas pressure
						CVParam gasPressureCV = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(
								activationParamGroup, referenceableParamGroupList, GasType.GAS_PRESSURE_CV);
						if (gasPressureCV != null) {
							gasPressure = gasPressureCV.getValue();
							gasPressureUnit = gasPressureCV.getUnitName();
						}
					}
					String temp = gasType + gasPressure + gasPressureUnit + activationType;
					if (!"".equals(temp) && !diccCollCell.containsKey(temp)) {
						collisionCells.add(
								new CollisionCellImpl(name, gasType, gasPressure, gasPressureUnit, activationType));
						diccCollCell.put(temp, temp);
					}
				}
			}
		}
		if (chromatogramIterator != null) {
			while (chromatogramIterator.hasNext()) {
				// read next spectrum from XML file
				Chromatogram chromatogram = chromatogramIterator.next();
				// resulting data
				if (chromatogram != null && chromatogram.getCvParam() != null && !chromatogram.getCvParam().isEmpty())
					resultingDatas.add(new ResultingDataImpl(chromatogram, referenceableParamGroupList, cvManager));

				final Precursor precursor = chromatogram.getPrecursor();
				if (precursor != null) {
					// if sourceFileRef!=null, add resulting data
					processSourceFileRef(precursor.getSourceFileRef(), sourceFileList, referenceableParamGroupList);

					// initialize variables
					gasType = null;
					gasPressure = null;
					gasPressureUnit = null;
					activationType = null;

					ParamGroup activationParamGroup = precursor.getActivation();
					// activation type
					activationType = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(activationParamGroup,
							referenceableParamGroupList, DissociationMethod.getInstance(cvManager));

					// gas type
					// Firstly, search <cvparam name="collision gas"
					// value"helium"/>
					gasType = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(activationParamGroup,
							referenceableParamGroupList, GasType.COLLISION_GAS);
					// if not found, search a CV allowed in MS_GAS_TYPE section
					if (gasType == null) {
						List<CVParam> gasTypeCVs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
								activationParamGroup, referenceableParamGroupList, GasType.getInstance(cvManager));
						if (gasTypeCVs != null) {
							gasType = MzMLControlVocabularyXmlFactory.parseAllCvParams(gasTypeCVs, null);
						}
					}

					// gas pressure
					CVParam gasPressureCV = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(activationParamGroup,
							referenceableParamGroupList, GasType.GAS_PRESSURE_CV);
					if (gasPressureCV != null) {
						gasPressure = gasPressureCV.getValue();
						gasPressureUnit = gasPressureCV.getUnitName();
					}
				}
				String temp = gasType + gasPressure + gasPressureUnit + activationType;
				if (!"".equals(temp) && !diccCollCell.containsKey(temp)) {
					collisionCells
							.add(new CollisionCellImpl(name, gasType, gasPressure, gasPressureUnit, activationType));
					diccCollCell.put(temp, temp);
				}
			}
		}
		log.info("end processing spectrums and chromatograms");

	}

	private void processSourceFileRef(String sourceFileRef, SourceFileList sourceFileList,
			ReferenceableParamGroupList referenceableParamGroupList) {
		// log.info("processing source file refs");

		if (sourceFileRef != null && !"".equals(sourceFileRef)) {
			SourceFile sourceFile = searchSourceFileInSourceFileRefList(sourceFileRef, sourceFileList);
			// in order to not getting the same source file several times
			if (sourceFile != null && !diccSourceFiles.containsKey(sourceFile.getId())) {
				diccSourceFiles.put(sourceFile.getId(), sourceFile.getId());
				resultingDatas.add(new ResultingDataImpl(sourceFile, referenceableParamGroupList, cvManager));
			}
		}
		// log.info("end processing source file refs");

	}

	private void processAcquisitions(Software software, SourceFileList sourceFileList,
			ScanSettingsList scanSettingsList, ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing acquisitions");

		acquisitions.add(new AcquisitionImpl(software, scanSettingsList, sourceFileList, referenceableParamGroupList,
				cvManager));

		log.info("end processing acquisitions");

	}

	private ScanSettings searchScanSettingsInScanSettingsList(String scanSettingsRef,
			ScanSettingsList scanSettingsList) {
		if (scanSettingsRef == null || "".equals(scanSettingsRef) || scanSettingsList == null
				|| scanSettingsList.getCount().longValue() <= 0)
			return null;
		for (ScanSettings scanSettings : scanSettingsList.getScanSettings()) {
			if (scanSettingsRef.equals(scanSettings.getId()))
				return scanSettings;
		}
		return null;
	}

	private Software searchSoftwareInSoftwareList(String softwareRef, SoftwareList softwareList) {
		if (softwareRef == null || "".equals(softwareRef) || softwareList == null
				|| softwareList.getCount().longValue() <= 0)
			return null;
		for (Software software : softwareList.getSoftware()) {
			if (softwareRef.equals(software.getId()))
				return software;
		}
		return null;
	}

	private void processInstrumentConfigurations(InstrumentConfigurationList instrumentConfigurationList,
			SoftwareList softwareList, SourceFileList sourceFileList, ScanSettingsList scanSettingsList,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing insturment config");
		Map<String, Software> diccSoftware = new THashMap<String, Software>();
		// foreach instrument configuration, create an spectrometer, ion optic
		// and acquisition
		if (instrumentConfigurationList != null && instrumentConfigurationList.getCount().longValue() > 0) {
			// parse spectrometers
			processSpectrometers(instrumentConfigurationList.getInstrumentConfiguration(), referenceableParamGroupList);
			for (InstrumentConfiguration instrumentConfiguration : instrumentConfigurationList
					.getInstrumentConfiguration()) {

				instrumentConfigurations.add(new InstrumentConfigurationImpl(instrumentConfiguration,
						referenceableParamGroupList, cvManager));

				// parse ion optics
				processIonOptics(instrumentConfiguration, referenceableParamGroupList);

				ScanSettings scanSettings = null;
				Software software = null;
				// get referenced scanSettings
				// This has been commented because some files not have the
				// reference, but have the scan setting
				/*
				 * if (instrumentConfiguration.getScanSettingsRef() != null &&
				 * !"".equals(instrumentConfiguration.getScanSettingsRef())) {
				 * scanSettings = this.searchScanSettingsInScanSettingsList(
				 * instrumentConfiguration.getScanSettingsRef(),
				 * scanSettingsList); }
				 */
				// get referenced software

				String softwareRef = instrumentConfiguration.getSoftwareRef();
				if (softwareRef != null && !"".equals(softwareRef) && !diccSoftware.containsKey(softwareRef)) {
					software = searchSoftwareInSoftwareList(softwareRef, softwareList);
					diccSoftware.put(softwareRef, software);
					// parse acquisitions
					processAcquisitions(software, sourceFileList, scanSettingsList, referenceableParamGroupList);
				}

			}
		}
		log.info("end processing insturment config");

	}

	private void processIonOptics(InstrumentConfiguration instrumentConfiguration,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing ion optics");

		if (instrumentConfiguration != null) {
			// Create a paramGroup
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					instrumentConfiguration.getCvParam(), instrumentConfiguration.getUserParam(),
					instrumentConfiguration.getReferenceableParamGroupRef());

			// Search for a CV that belongs to a MS_ION_OPTICS_NAME section
			// List<CVParam> ionOpticsCVList =
			// MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
			// paramGroup, referenceableParamGroupList,
			// IonOpticsType.getInstance(cvManager));
			// if (ionOpticsCVList != null) {
			// for (CVParam cvParam : ionOpticsCVList) {
			// this.ionOptics.add(new IonOpticsImpl(cvParam));
			// }
			// } else {
			// // if not found in instrumentconfiguration, search in analyzer
			// // components
			// if (instrumentConfiguration.getComponentList() != null) {
			// final List<AnalyzerComponent> analyzerList =
			// instrumentConfiguration
			// .getComponentList().getAnalyzer();
			// if (analyzerList != null) {
			// for (AnalyzerComponent analyzerComponent : analyzerList) {
			// paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
			// analyzerComponent.getCvParam(),
			// analyzerComponent.getUserParam(),
			// analyzerComponent.getReferenceableParamGroupRef());
			// ionOpticsCVList =
			// MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
			// paramGroup, referenceableParamGroupList,
			// IonOpticsType.getInstance(cvManager));
			// if (ionOpticsCVList != null) {
			// for (CVParam cvParam : ionOpticsCVList) {
			// this.ionOptics.add(new IonOpticsImpl(cvParam));
			// }
			// }
			// }
			// }
			// }
			// }
		}
		log.info("end processing ion optics");

	}

	private void processSpectrometers(List<InstrumentConfiguration> instrumentConfigurationList,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing spectrometers");

		if (instrumentConfigurationList != null) {
			final Spectrometer spectrometer = new SpectrometerImpl(instrumentConfigurationList,
					referenceableParamGroupList, cvManager);
			if (spectrometer != null)
				spectrometers.add(spectrometer);
		}
		log.info("end processing spectrometers");

	}

	private void processContact(List<ParamGroup> contacts, ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing contacts");

		if (contacts != null && contacts.size() > 0) {
			log.info("there are contacts to process");
			contact = new ContactImpl(contacts, referenceableParamGroupList, user, cvManager);
		}
		log.info("end processing contacts");

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getVersion() {
		StringBuilder sb = new StringBuilder();
		if (mzMLUnmarshaller != null) {

			final String mzMLId = mzMLUnmarshaller.getMzMLId();
			if (mzMLId != null)
				sb.append(mzMLId);
			final String mzMLVersion = mzMLUnmarshaller.getMzMLVersion();
			if (mzMLVersion != null && !mzMLVersion.isEmpty()) {
				if (!sb.toString().isEmpty())
					sb.append(" ");
				sb.append(mzMLVersion);
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public Project getProject() {
		return new ProjectImpl(projectName, user, dbManager);
	}

	@Override
	public User getOwner() {
		return user;
	}

	@Override
	public String getName() {
		if (mzMLFileName != null)
			return "MIAPE MS from '" + mzMLFileName + "'";
		return "MIAPE MS from mzML file";
	}

	@Override
	public MiapeDate getDate() {
		if (date != null)
			return date;
		return new MiapeDate(System.currentTimeMillis());
	}

	@Override
	public Date getModificationDate() {
		return new Date(System.currentTimeMillis());
	}

	@Override
	public Boolean getTemplate() {
		return Boolean.FALSE;
	}

	@Override
	public String getAttachedFileLocation() {
		return standardXMLLocation;
	}

	public void setAttachedFileLocation(String fileURL) {
		standardXMLLocation = fileURL;

	}

	public void addResultingData(ResultingData resultingData) {
		resultingDatas.add(resultingData);
	}

	public void setResultingDatas(List<ResultingData> resultingData) {
		resultingDatas.clear();
		resultingDatas.addAll(resultingData);
	}

	@Override
	public ValidationReport getValidationReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager == null)
			throw new MiapeDatabaseException("The persistance method is not defined.");
		id = dbManager.getMiapeMSPersistenceManager().store(this);
		return id;
	}

	@Override
	public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager == null)
			throw new MiapeDatabaseException("The persistance method is not defined.");
		if (id > 0) {
			dbManager.getMiapeMSPersistenceManager().deleteById(id, userName, password);
		} else
			throw new MiapeDatabaseException("The MIAPE is not stored yet!");

	}

	@Override
	public MiapeXmlFile<MiapeMSDocument> toXml() {
		return MiapeMSXmlFactory.getFactory().toXml(this, cvManager);
	}

	@Override
	public MSContact getContact() {
		return contact;
	}

	/*
	 * @Override public Set<Quantitation> getQuantitations() { Auto-generated
	 * method stub return null; }
	 */

	@Override
	public Set<Spectrometer> getSpectrometers() {
		if (spectrometers != null && !spectrometers.isEmpty())
			return spectrometers;
		return null;
	}

	@Override
	public Set<Acquisition> getAcquisitions() {
		if (acquisitions != null && !acquisitions.isEmpty())
			return acquisitions;
		return null;
	}

	@Override
	public List<ResultingData> getResultingDatas() {
		if (resultingDatas != null && !resultingDatas.isEmpty())
			return resultingDatas;
		return null;
	}

	@Override
	public Set<DataAnalysis> getDataAnalysis() {
		if (dataAnalysises != null && !dataAnalysises.isEmpty())
			return dataAnalysises;
		return null;
	}

	@Override
	public List<MSAdditionalInformation> getAdditionalInformations() {
		if (additionalInformations != null && !additionalInformations.isEmpty())
			return additionalInformations;
		return null;
	}

	@Override
	public List<org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration> getInstrumentConfigurations() {
		if (instrumentConfigurations != null && !instrumentConfigurations.isEmpty())
			return instrumentConfigurations;
		return null;
	}

}
