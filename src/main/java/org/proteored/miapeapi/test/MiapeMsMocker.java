package org.proteored.miapeapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.DissociationMethod;
import org.proteored.miapeapi.cv.ms.IonMode;
import org.proteored.miapeapi.cv.ms.IonOpticsType;
import org.proteored.miapeapi.cv.ms.PressureUnit;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.spring.SpringHandler;

import gnu.trove.set.hash.THashSet;

public class MiapeMsMocker {
	public static final ControlVocabularyManager cvManager = SpringHandler.getInstance("spring-test.xml")
			.getCVManager();
	public static final String PEAK_LIST_GENERATION_MASS_TOLERANCE_UNIT = "peakListGenerationMassToleranceUnit";
	public static final String COLLISION_CELL_PRESSURE = "CollisionCellPressure";
	public static final String COLLISION_CELL_PRESSURE_UNIT = PressureUnit.getInstance(cvManager).getFirstCVTerm()
			.getPreferredName();
	public static final String COLLISION_CELL_ACTIVATION_TYPE = DissociationMethod.getInstance(cvManager)
			.getFirstCVTerm().getPreferredName();
	public static final String PEAK_LIST_GENERATION_GENERAL_PARAMETERS = "peakListGenerationGeneralParameters";
	public static final String MALDI_WAVE_LENGTH = "MaldiWaveLength";
	public static final String PEAK_LIST_GENERATION_SCAN_PRODUCTION_TYPE = "peakListGenerationScanProductionType";
	public static final String PEAK_LIST_GENERATION_ACQUISITION_NUMBER = "peakListGenerationAcquisitionNumber";
	public static final String PEAK_LIST_GENERATION_REFERENCE_NUMBERS = "peakListGenerationReferenceNumbers";
	public static final String PEAK_LIST_GENERATION_MASS_TOLERANCE = "peakListGenerationMassTolerance";
	public static final String PEAK_LIST_GENERATION_MASS_TYPE = "peakListGenerationMassType";
	public static final String PEAK_LIST_GENERATION_DATA_TYPE = "peakListGenerationDataType";

	public static final String PEAK_LIST_GENERATION_MIN_NUM_IONS = "peakListGenerationMinNumIons";
	public static final String PEAK_LIST_GENERATION_NUM_IONS = "peakListGenerationNumIons";
	public static final String PEAK_LIST_GENERATION_SN_THRESHOLD = "peakListGenerationSNThreshold";
	public static final String SPECTRUM_DESCRIPTION_PRECURSOR_CHARGE = "spectrumDescriptionPrecursorCharge";
	public static final String SPECTRUM_DESC_PRECURSOR_MZ = "SpectrumDescPrecursorMz";
	public static final String PLG_PARAMETERS = "PLGParameters";
	public static final String PLG_PARAMETERS_URL = "PLGParameters";
	public static final String PEAK_LIST_GENERATION_NAME = "peakListGenerationName";
	public static final String SPECTRUM_TARGET = "SpectrumTarget";
	public static final String FULL_SPECTRUM_URL = "FullSpectrumUrl";

	public static final String SPECTRUM_DESCRIPTION_MS_LEVEL = "MS_LEVEL";
	public static final String ION_MODE = IonMode.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String SPECTRUM_DESCRIPTION_NAME = "SpectrumDescriptionName";
	public static final String SPECTRA_DESCRIPTION_RAW_FILE_URL = "SpectraDescriptionRawFileUrl";
	public static final String SPECTRA_DESCRIPTION_RAW_FILE_DESCRIPTION = "SpectraDescriptionRawFileDescription";
	public static final String SPECTRA_DESCRIPTION_ADDITIONAL_URL = "SpectraDescriptionAdditionalUrl";
	public static final String SPECTRA_DESCRIPTION_NAME = "SpectraDescriptionName";
	public static final String ESI_PARAMETERS = "EsiParameters";
	public static final String ESI_SUPPLY_TYPE = "Static";
	public static final String ESI_NAME = "EsiName";
	public static final String TRAP_FINAL_MASS_SPECTOMETRY = "trapFinalMassSpectometry";
	public static final String ANALYZER_NAME = "TrapName";
	public static final String IONOPTICNAME = IonOpticsType.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String COLLISION_CELL_URL = "CollisionCellUrl";
	public static final String COLLISION_CELL_GAS = "CollisionCellGas";
	public static final String COLLISION_CELL_NAME = "CollisionCellName";
	public static final String MALDI_VOLTAGE = "MaldiVoltage";
	public static final String MALDI_DISSOCIATION = "MaldiPostSourceDecay";
	public static final String MALDI_DISSOCIATION_SUMMARY = "Dissociation summary";
	public static final String MALDI_PLATE_TYPE = "MaldiPlateType";
	public static final String MALDI_PARAMETERS = "MaldiParameters";
	public static final String MALDI_MATRIX = "MaldiMatrix";
	public static final String MALDI_LASER = "MaldiLaser";
	public static final String MALDI_EXTRACTION = "Delayed extraction";
	public static final String MALDI_DEPOSITION = "MaldiDeposition";
	public static final String MALDI_NAME = "MaldiName";
	public static final String TOF_NAME = "tofName";
	public static final String QUANTATION_SAMPLE_NUMBER = "QuantationSampleNumber";
	public static final String QUANTITATION_PROTOCOL = "QuantitationProtocol";
	public static final String QUANTITATION_NORMALIZATION = "QuantitationNormalization";
	public static final String QUANTITATION_FILE_TYPE = "QUANTITATIONFileType";
	public static final String QUANTITATION_DATA_URL = "QuantitationDataUrl";
	public static final String QUANTITATION_APPROACH = "QuantitationApproach";
	public static final String QUANTITATION_NAME = "quantitationName";
	public static final String DETECTOR_TYPE = "DetectorType";
	public static final String DETECTOR_SENSITIVITY = "DetectorSensitivity";
	public static final String DETECTOR_NAME = "DetectorName";

	private static final Set<DataAnalysis> PEAK_LIST_GENERATIONS = mockPeakListGenerations();
	private static final Equipment ESI_SPRAYER1 = MiapeMocker.mockEquipment(21);
	private static final Equipment ESI_INTERFACE1 = MiapeMocker.mockEquipment(11);
	private static final List<Maldi> MALDIS = mockMaldis();
	/*
	 * private static final Set<Quantitation> QUANTITATIONS =
	 * mockQuantitations();
	 */

	private static final Set<Spectrometer> SPECTROMETERS = mockSpectrometers();
	private static final Set<ActivationDissociation> COLLISION_CELLS = mockCollisionCells();
	private static final Set<Acquisition> CONTROL_ANALYSIS_SOFTWARES = mockControlAnalysisSoftwares();
	private static final List<Analyser> ANALYZERS = mockAnalyzers();
	// private static final Set<IonOptic> IONOPTICS = mockIonOptics();
	private static final List<InstrumentConfiguration> INSTRUMENT_CONFIGURATIONS = mockInstrumentConfigurations();
	private static final List<Esi> ESIS = mockEsis();
	private static final List<Other_IonSource> OTHER_ION_SOURCES = mockOther_IonSources();
	private static final List<ResultingData> RESULTING_DATA = mockResultingDatas();
	private static final List<MSAdditionalInformation> ADDITIONAL_INFORMATIONS = mockAdditionalInformations();

	public static final String ADDINFO_NAME = "ADDINFO_NAME";
	public static final String ADDINFO_VALUE = "ADDINFO_VALUE";
	public static final String ION_SOURCE_NAME = "OTHER_ION_SOURCE";
	public static final String ION_SOURCE_PARAMETERS = "OTHER_ION_SOURCE_PARAMETERS";
	public static final String PLG_CATALOGNUMBER = "PLG_Catalognumber";
	public static final String PLG_COMMENTS = "PLG_Comments";
	public static final String PLG_CUSTOMIZATIONS = "PLG_Customizations";
	public static final String PLG_DESCRIPTION = "PLG_Description";
	public static final String PLG_MANUFACTURER = "PLG_Manufacturer";
	public static final String PLG_MODEL = "PLG_model";
	public static final String PLG_URI = "PLG_URI";
	public static final String PLG_VERSION = "PLG_Version";
	public static final String RESULTING_DATA_NAME = "RS_name";
	public static final String RESULTING_DATA_ADDURI = "RS_adduri";
	public static final String RESULTING_DATA_RAWFILETYPE = "RS_rawfiletype";
	public static final String RESULTING_DATA_RAWFILEURI = "RS_rawfileuri";
	public static final String RESULTING_DATA_SRM_DESCRIPTOR = "RS_SRM_Descriptor";
	public static final String RESULTING_DATA_SRM_TYPE = "RS_srmtype";
	public static final String RESULTING_DATA_SRM_URI = "RS_srmuri";
	public static final String REFLECTRON = "Reflectron";
	private static final String IC_NAME = "Instrument configuration";

	public static MiapeMSDocument mockMSMiapeDocument() {

		MiapeMSDocument miape = Mockito.mock(MiapeMSDocument.class);
		MiapeMocker.mockMiapeDocument(miape);
		MSContact contact = MiapeMocker.mockMSContact();
		Mockito.when(miape.getContact()).thenReturn(contact);

		/* Mockito.when(miape.getQuantitations()).thenReturn(QUANTITATIONS); */
		Mockito.when(miape.getSpectrometers()).thenReturn(SPECTROMETERS);
		Mockito.when(miape.getInstrumentConfigurations()).thenReturn(INSTRUMENT_CONFIGURATIONS);
		Mockito.when(miape.getAcquisitions()).thenReturn(CONTROL_ANALYSIS_SOFTWARES);
		Mockito.when(miape.getResultingDatas()).thenReturn(RESULTING_DATA);
		Mockito.when(miape.getDataAnalysis()).thenReturn(PEAK_LIST_GENERATIONS);
		Mockito.when(miape.getAdditionalInformations()).thenReturn(ADDITIONAL_INFORMATIONS);
		return miape;
	}

	private static List<InstrumentConfiguration> mockInstrumentConfigurations() {
		List<InstrumentConfiguration> result = new ArrayList<InstrumentConfiguration>();
		result.add(mockInstrumentConfiguration());
		return result;
	}

	private static InstrumentConfiguration mockInstrumentConfiguration() {
		InstrumentConfiguration ic = Mockito.mock(InstrumentConfiguration.class);
		Mockito.when(ic.getAnalyzers()).thenReturn(ANALYZERS);
		Mockito.when(ic.getActivationDissociations()).thenReturn(COLLISION_CELLS);
		Mockito.when(ic.getEsis()).thenReturn(ESIS);
		// Mockito.when(ic.getIonOptics()).thenReturn(IONOPTICS);
		Mockito.when(ic.getMaldis()).thenReturn(MALDIS);
		Mockito.when(ic.getName()).thenReturn(IC_NAME);
		Mockito.when(ic.getOther_IonSources()).thenReturn(OTHER_ION_SOURCES);
		return ic;

	}

	private static List<ResultingData> mockResultingDatas() {
		List<ResultingData> result = new ArrayList<ResultingData>();
		result.add(MiapeMocker.mockResultingData(0));
		return result;
	}

	private static List<MSAdditionalInformation> mockAdditionalInformations() {
		List<MSAdditionalInformation> result = new ArrayList<MSAdditionalInformation>();
		result.add(MiapeMocker.mockMSAdditionalInformation(0));
		return result;
	}

	private static Set<Spectrometer> mockSpectrometers() {
		Set<Spectrometer> result = new THashSet<Spectrometer>();
		result.add(MiapeMocker.mockSpectrometer(0));
		return result;
	}

	/*
	 * private static Set<Quantitation> mockQuantitations() { Set<Quantitation>
	 * result = new THashSet<Quantitation>(); result.add(mockQuantitation(0));
	 * return result; } private static Quantitation mockQuantitation(int i) {
	 * Quantitation quantitation = Mockito.mock(Quantitation.class);
	 * Mockito.when(quantitation.getName()).thenReturn(QUANTITATION_NAME + i);
	 * Mockito.when(quantitation.getApproach()).thenReturn(QUANTITATION_APPROACH
	 * + i);
	 * Mockito.when(quantitation.getDataUrl()).thenReturn(QUANTITATION_DATA_URL
	 * + i); Mockito.when(quantitation.getFileType()).thenReturn(
	 * QUANTITATION_FILE_TYPE + i);
	 * Mockito.when(quantitation.getNormalization()).thenReturn(
	 * QUANTITATION_NORMALIZATION + i);
	 * Mockito.when(quantitation.getProtocol()).thenReturn(QUANTITATION_PROTOCOL
	 * + i); Mockito.when(quantitation.getSampleNumber()).thenReturn(
	 * QUANTATION_SAMPLE_NUMBER + i);
	 * Mockito.when(quantitation.getQuantitationSoftwares()).thenReturn(new
	 * HashSet<Software>() { private static final long serialVersionUID = 1L; {
	 * add(QUANTITATION_SOFTWARES); } }); return quantitation; }
	 */

	private static List<Maldi> mockMaldis() {
		List<Maldi> result = new ArrayList<Maldi>();
		result.add(mockMaldi(0));
		return result;
	}

	private static Maldi mockMaldi(int i) {
		Maldi maldi = Mockito.mock(Maldi.class);
		Mockito.when(maldi.getName()).thenReturn(MALDI_NAME + i);
		Mockito.when(maldi.getExtraction()).thenReturn(MALDI_EXTRACTION);
		Mockito.when(maldi.getLaser()).thenReturn(MALDI_LASER + i);
		Mockito.when(maldi.getMatrix()).thenReturn(MALDI_MATRIX + i);
		Mockito.when(maldi.getLaserParameters()).thenReturn(MALDI_PARAMETERS + i);
		Mockito.when(maldi.getPlateType()).thenReturn(MALDI_PLATE_TYPE + i);
		Mockito.when(maldi.getDissociation()).thenReturn(MALDI_DISSOCIATION + i);
		Mockito.when(maldi.getLaserWaveLength()).thenReturn(MALDI_WAVE_LENGTH + i);
		Mockito.when(maldi.getDissociationSummary()).thenReturn(MALDI_DISSOCIATION_SUMMARY + i);

		return maldi;
	}

	private static Set<ActivationDissociation> mockCollisionCells() {
		Set<ActivationDissociation> result = new THashSet<ActivationDissociation>();
		result.add(mockCollisionCell(0));
		return result;
	}

	private static ActivationDissociation mockCollisionCell(int i) {
		ActivationDissociation collisionCell = Mockito.mock(ActivationDissociation.class);
		Mockito.when(collisionCell.getName()).thenReturn(COLLISION_CELL_NAME + i);
		Mockito.when(collisionCell.getGasType()).thenReturn(COLLISION_CELL_GAS + i);
		Mockito.when(collisionCell.getGasPressure()).thenReturn(COLLISION_CELL_PRESSURE + i);
		Mockito.when(collisionCell.getPressureUnit()).thenReturn(COLLISION_CELL_PRESSURE_UNIT + i);
		Mockito.when(collisionCell.getActivationType()).thenReturn(COLLISION_CELL_ACTIVATION_TYPE + i);

		return collisionCell;
	}

	private static Set<Acquisition> mockControlAnalysisSoftwares() {
		Set<Acquisition> result = new THashSet<Acquisition>();
		result.add(mockControlAnalysisSoftware(0));
		return result;
	}

	private static Acquisition mockControlAnalysisSoftware(int i) {
		Acquisition software = Mockito.mock(Acquisition.class);
		Mockito.when(software.getName()).thenReturn(MiapeMocker.SOFTWARE_NAME + i);
		Mockito.when(software.getCatalogNumber()).thenReturn(MiapeMocker.SOFTWARE_CATALOG_NUMBER + i);
		Mockito.when(software.getComments()).thenReturn(MiapeMocker.SOFTWARE_COMMENTS + i);
		Mockito.when(software.getCustomizations()).thenReturn(MiapeMocker.SOFTWARE_CUSTOMIZATIONS + i);
		Mockito.when(software.getDescription()).thenReturn(MiapeMocker.SOFTWARE_DESCRIPTION + i);
		Mockito.when(software.getManufacturer()).thenReturn(MiapeMocker.SOFTWARE_MANUFACTURER + i);
		Mockito.when(software.getModel()).thenReturn(MiapeMocker.SOFTWARE_MODEL + i);
		Mockito.when(software.getParameterFile()).thenReturn(MiapeMocker.SOFTWARE_PARAMETER_FILE + i);
		Mockito.when(software.getParameters()).thenReturn(MiapeMocker.SOFTWARE_PARAMETERS + i);
		Mockito.when(software.getURI()).thenReturn(MiapeMocker.SOFTWARE_URI + i);
		Mockito.when(software.getVersion()).thenReturn(MiapeMocker.SOFTWARE_VERSION + i);

		return software;
	}

	private static List<Analyser> mockAnalyzers() {
		List<Analyser> result = new ArrayList<Analyser>();
		result.add(mockAnalyzer(0));
		return result;
	}

	private static Analyser mockAnalyzer(int i) {
		Analyser trap = Mockito.mock(Analyser.class);
		Mockito.when(trap.getName()).thenReturn(ANALYZER_NAME + i);
		Mockito.when(trap.getReflectron()).thenReturn(REFLECTRON + i);
		return trap;
	}

	// private static Set<IonOptic> mockIonOptics() {
	// Set<IonOptic> result = new THashSet<IonOptic>();
	// result.add(mockIonOptic(0));
	// return result;
	// }
	//
	// private static IonOptic mockIonOptic(int i) {
	// IonOptic orbi = Mockito.mock(IonOptic.class);
	// Mockito.when(orbi.getName()).thenReturn(IONOPTICNAME + i);
	// return orbi;
	// }

	private static List<Other_IonSource> mockOther_IonSources() {
		List<Other_IonSource> result = new ArrayList<Other_IonSource>();
		result.add(mockOther_IonSource(0));
		return result;
	}

	private static Other_IonSource mockOther_IonSource(int i) {
		Other_IonSource ionSource = Mockito.mock(Other_IonSource.class);
		Mockito.when(ionSource.getName()).thenReturn(ION_SOURCE_NAME + i);
		Mockito.when(ionSource.getParameters()).thenReturn(ION_SOURCE_PARAMETERS + i);
		return ionSource;
	}

	private static List<Esi> mockEsis() {
		List<Esi> result = new ArrayList<Esi>();
		result.add(mockEsi(0));
		return result;
	}

	private static Esi mockEsi(int i) {

		Esi esi = Mockito.mock(Esi.class);
		Mockito.when(esi.getName()).thenReturn(ESI_NAME + i);

		Mockito.when(esi.getParameters()).thenReturn(ESI_PARAMETERS + i);

		Answer<Set<Equipment>> interfaces = new Answer<Set<Equipment>>() {
			@SuppressWarnings("serial")
			@Override
			public Set<Equipment> answer(InvocationOnMock invocation) throws Throwable {
				return new THashSet<Equipment>() {
					{
						add(ESI_INTERFACE1);
					}
				};
			}
		};

		Answer<Set<Equipment>> sprayers = new Answer<Set<Equipment>>() {
			@SuppressWarnings("serial")
			@Override
			public Set<Equipment> answer(InvocationOnMock invocation) throws Throwable {
				return new THashSet<Equipment>() {
					{
						add(ESI_SPRAYER1);
					}
				};
			}
		};
		Mockito.when(esi.getInterfaces()).thenAnswer(interfaces);
		Mockito.when(esi.getSprayers()).thenAnswer(sprayers);

		Mockito.when(esi.getSupplyType()).thenReturn(ESI_SUPPLY_TYPE);
		return esi;
	}

	private static Set<DataAnalysis> mockPeakListGenerations() {
		Set<DataAnalysis> result = new THashSet<DataAnalysis>();
		result.add(mockDataAnalysis(0));
		return result;
	}

	private static DataAnalysis mockDataAnalysis(int i) {
		DataAnalysis peakListGeneration = Mockito.mock(DataAnalysis.class);
		Mockito.when(peakListGeneration.getName()).thenReturn(PEAK_LIST_GENERATION_NAME + i);
		Mockito.when(peakListGeneration.getCatalogNumber()).thenReturn(PLG_CATALOGNUMBER + i);
		Mockito.when(peakListGeneration.getComments()).thenReturn(PLG_COMMENTS + i);
		Mockito.when(peakListGeneration.getCustomizations()).thenReturn(PLG_CUSTOMIZATIONS + i);
		Mockito.when(peakListGeneration.getDescription()).thenReturn(PLG_DESCRIPTION + i);
		Mockito.when(peakListGeneration.getManufacturer()).thenReturn(PLG_MANUFACTURER + i);
		Mockito.when(peakListGeneration.getModel()).thenReturn(PLG_MODEL + i);
		Mockito.when(peakListGeneration.getParameters()).thenReturn(PLG_PARAMETERS + i);
		Mockito.when(peakListGeneration.getParametersLocation()).thenReturn(PLG_PARAMETERS_URL + i);
		Mockito.when(peakListGeneration.getURI()).thenReturn(PLG_URI + i);
		Mockito.when(peakListGeneration.getVersion()).thenReturn(PLG_VERSION + i);

		return peakListGeneration;
	}

}
