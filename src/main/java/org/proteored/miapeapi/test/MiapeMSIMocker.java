package org.proteored.miapeapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mockito.Mockito;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.cv.msi.DatabaseName;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.cv.msi.SearchType;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.spring.SpringHandler;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class MiapeMSIMocker {
	public static final ControlVocabularyManager cvManager = SpringHandler.getInstance().getCVManager();
	public static final String SCORE_VALUE = "34";
	public static final String RAWFILETYPE = MSFileType.getInstance(cvManager).getPossibleValues().get(0)
			.getPreferredName();
	public static final String PARAMETER_VALUE = "ParameterValue";
	public static final String PARAMETER_NAME = "ParameterName";
	public static final String CLEAVAGE_NAME = "CleavageName";
	public static final String NUM_ENTRIES = "NumEntries";
	public static final String MASS_TOLERANCE_PMF_UNIT = "MassTolerancePmfUnit";
	public static final String PRECURSOR_MASS_TOLERANCE_UNIT = "PrecursorMassToleranceUnit";
	public static final String FRAGMENT_MASS_TOLERANCE_UNIT = "FragmentMassToleranceUnit";
	public static final String FRAGMENT_MASS_TOLERANCE = "FragmentMassTolerance";
	private static Set<InputParameter> INPUT_PARAMETERS;
	private static final Algorithm SCORING_ALGORITHM = MiapeMocker.mockAlgorithm(0);
	public final static String PEPTIDE_MODIFICATION_NAME = PeptideModificationName.getInstance(cvManager)
			.getPossibleValues().get(0).getPreferredName();
	public static final Double AVG_DELTA = 18.0;
	public static final Double MONO_DELTA = 18.1;
	public static final int MODIFICATION_POSITION = 4;
	public static final String REPLACEMENT_RESIDUE = "M";
	public static final String RESIDUES = "M";
	public static final Double NEUTRAL_LOSS = 63.998285;
	public static final Set<PeptideModification> MODIFICATIONS = MockMSIPeptideModifications();

	public static IdentifiedProtein IDENTIFIED_PROTEIN;
	public static final Software QUANTITATION_SOFTWARE = MiapeMocker.mockSoftware(0);

	public static final Set<Validation> VALIDATIONS = mockValidations();
	public static final Set<Software> MSI_SOFTWARES = mockMsiSoftwares();

	public static Set<InputData> INPUT_DATAS = mockInputDatas(); // antes de
																	// input_data_sets
	public static Set<InputDataSet> INPUT_DATA_SETS = mockInputDataSets();

	private static final List<IdentifiedPeptide> IDENTIFIED_PEPTIDES = mockIdentifiedPeptides();
	public static final Map<String, IdentifiedProtein> IDENTIFIED_PROTEINS = mockProteins();
	public static final Set<IdentifiedProteinSet> IDENTIFIED_PROTEIN_SETS = mockProteinSets();

	public static final String VALIDATION_RESULTS = "ValidationResults";

	public static final String VALIDATION_NAME = "ValidationName";
	public static final String REPLICATES_NUMBER = "ReplicatesNumber";
	public static final String ION_QUANTITATION_NAME = "IonQuantitationName";
	public static final String MEASUREMENT = "Measurement";
	public static final String EXPERIMENTAL_PROTOCOL = "ExperimentalProtocol";
	public static final String ERROR_ANALYSIS = "ErrorAnalysis";
	public static final String CONTROL_RESULTS = "ControlResults";
	public static final String ACCEPTANCE_CRITERIA = "AcceptanceCriteria";
	public static final String TAXONOMY = "taxonomy";
	public static final String MISSCLEAVAGES = "Misscleavages";
	public static final String MIN_SCORE = "MinScore";
	public static final String MASS_TOLERANCE_PMF = "MassTolerancePmf";
	public static final String MASS_TOLERANCE_MS = "MassToleranceMs";
	public static final String INPUT_PARAMETER_NAME = "InputParameterName";
	public static final String DATABASE_URI = "DatabaseURI";
	public static final String SEQUENCE_NUMBER = "SequenceNumber";
	public static final String DATABASE_VERSION = "DatabaseVersion";
	public static final String DATABASE_NAME = "DatabaseName";
	public static final String DATABASE_DESCRIPTION = "DatabaseDescription";
	public static final String DATE = "Feb 2007";
	public static final String CLEAVAGE_RULES = "CleavageRules";
	public static final String APARAMETERS = "Aparameters";
	public static final String ADDITIONAL_CLEAVAGES = "AdditionalCleavages";
	public static final String AA_MODIF = "AaModif";
	public static final String SOURCE_DATA_URL = "SourceDataUrl";
	public static final String INPUT_DATA_NAME = "InputDataName";
	public static final String INPUT_DATA_DESCRIPTION = "InputDataDescription";

	public static Boolean PROTEIN_VALIDATION_STATUS;
	public static final String PROTEIN_VALIDATION_TYPE = "ValidationType";
	public static final String PROTEIN_VALIDATION_VALUE = "ValidationValue";

	public static Set<ProteinScore> PROTEIN_SCORES;
	public static final String PEPTIDE_NUMBER = "PeptideNumber";
	public static final String MATCHED_NUMBER = "MatchedNumber";
	public static final String UNMATCHED_NUMBER = "UnmatchedNumber";

	public static final String PROTEIN_DESCRIPTION = "ProteinDescription";
	public static final String COVERAGE = "Coverage";
	public static final String ADDITIONAL_INFORMATION = "AdditionalInformation";
	public static final String ACCESSION = "Accession";
	public static final String PROTEIN_SET_NAME = "ProteinSetName";
	public static final String PROTEIN_FILE_URL = "ProteinFileUrl";
	public static final String SPECTRUM_Ref = "435";
	public static final String SEQUENCE = "Sequence";
	public static Set<PeptideScore> PEPTIDE_SCORES;

	public static final String MASS_DESVIATION = "MassDesviation";
	public static final String CHARGE = "Charge";
	public static final String IDENTIFIED_PEPTIDE_SET_NAME = "IdentifiedPeptideSetName";
	public static final String FILE_URL = "FileURL";
	public static final String GENERATED_FILES_URL = "GeneratedFilesURL";
	public static final String GENERATED_FILES_DESCRIPTION = "GeneratedFilesDescription";
	private static final Set<MSIAdditionalInformation> ADDITIONAL_INFORMATIONS = MockMSIAdditionalInformations();
	public static final String ADDINFONAME = "ADDINFONAME";
	public static final String ADDINFOVALUE = "ADDINFOVALUE";
	public static final String SEARCHTYPE = SearchType.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String PROTEIN_SET_LOCATION = "http://locationofproteinset.com";
	public static final String INPUT_DATA_SET_NAME = "INPUT_DATA_SET_NAME";
	public static final String GLOBAL_THRESHOLDS = "Glocal threshold <0";

	private static InputParameter INPUT_PARAMETER = mockInputParameter(0);
	private static InputParameter mockedInputParameter;
	private static MiapeMSDocument mockedMSMiapeDocument = MiapeMsMocker.mockMSMiapeDocument();
	public static IdentifiedPeptide IDENTIFIED_PEPTIDE1;
	public static IdentifiedPeptide IDENTIFIED_PEPTIDE2;

	public static MiapeMSIDocument mockMSIMiapeDocument() {

		MiapeMSIDocument document = Mockito.mock(MiapeMSIDocument.class);
		MiapeMocker.mockMiapeDocument(document);
		MSContact contact = MiapeMocker.mockMSContact();
		Mockito.when(document.getContact()).thenReturn(contact);

		Mockito.when(document.getGeneratedFilesDescription()).thenReturn(GENERATED_FILES_DESCRIPTION);
		Mockito.when(document.getGeneratedFilesURI()).thenReturn(GENERATED_FILES_URL);

		Mockito.when(document.getIdentifiedProteinSets()).thenReturn(IDENTIFIED_PROTEIN_SETS);

		INPUT_PARAMETERS = mockInputParameters();

		Mockito.when(document.getInputDataSets()).thenReturn(INPUT_DATA_SETS);
		Mockito.when(document.getInputParameters()).thenReturn(INPUT_PARAMETERS);
		Mockito.when(document.getSoftwares()).thenReturn(MSI_SOFTWARES);
		Mockito.when(document.getValidations()).thenReturn(VALIDATIONS);
		Mockito.when(document.getAdditionalInformations()).thenReturn(ADDITIONAL_INFORMATIONS);
		Mockito.when(document.getIdentifiedPeptides()).thenReturn(IDENTIFIED_PEPTIDES);

		// Creo la lista de proteins
		List<IdentifiedProtein> proteinList = new ArrayList<IdentifiedProtein>();
		proteinList.add(IDENTIFIED_PROTEIN);
		// asigno las relaciones entre la proteina y los peptidos
		for (IdentifiedPeptide pep : IDENTIFIED_PEPTIDES) {
			Mockito.when(pep.getIdentifiedProteins()).thenReturn(proteinList);
		}
		// Assigno los peptidos a la proteina
		Mockito.when(IDENTIFIED_PROTEIN.getIdentifiedPeptides()).thenReturn(IDENTIFIED_PEPTIDES);

		int miapeMSID = mockedMSMiapeDocument.getId();
		Mockito.when(document.getMSDocumentReference()).thenReturn(miapeMSID);
		return document;
	}

	private static Set<ProteinScore> mockMSIProteinScores() {
		Set<ProteinScore> result = new THashSet<ProteinScore>();
		result.add(mockProteinScore());
		return result;
	}

	private static Set<PeptideScore> mockMSIPeptideScores() {
		Set<PeptideScore> result = new THashSet<PeptideScore>();
		result.add(mockPeptideScore());
		return result;
	}

	private static ProteinScore mockProteinScore() {
		ProteinScore result = Mockito.mock(ProteinScore.class);

		Mockito.when(result.getName()).thenReturn(Score.getInstance(cvManager).getFirstCVTerm().getPreferredName());

		Mockito.when(result.getValue()).thenReturn(SCORE_VALUE);
		return result;
	}

	private static PeptideScore mockPeptideScore() {
		PeptideScore result = Mockito.mock(PeptideScore.class);

		Mockito.when(result.getName()).thenReturn(Score.getInstance(cvManager).getFirstCVTerm().getPreferredName());
		Mockito.when(result.getValue()).thenReturn("34");
		return result;
	}

	private static Set<PeptideModification> MockMSIPeptideModifications() {
		Set<PeptideModification> result = new THashSet<PeptideModification>();
		result.add(mockPeptideModification());
		return result;
	}

	private static PeptideModification mockPeptideModification() {
		PeptideModification result = Mockito.mock(PeptideModification.class);

		Mockito.when(result.getName()).thenReturn(PEPTIDE_MODIFICATION_NAME);
		Mockito.when(result.getAvgDelta()).thenReturn(AVG_DELTA);
		Mockito.when(result.getMonoDelta()).thenReturn(MONO_DELTA);
		Mockito.when(result.getPosition()).thenReturn(MODIFICATION_POSITION);
		Mockito.when(result.getReplacementResidue()).thenReturn(REPLACEMENT_RESIDUE);
		Mockito.when(result.getResidues()).thenReturn(RESIDUES);
		Mockito.when(result.getNeutralLoss()).thenReturn(NEUTRAL_LOSS);
		return result;
	}

	private static Set<InputDataSet> mockInputDataSets() {
		Set<InputDataSet> result = new THashSet<InputDataSet>();

		result.add(mockInputDataSet());
		return result;
	}

	private static InputDataSet mockInputDataSet() {
		InputDataSet result = Mockito.mock(InputDataSet.class);
		Mockito.when(result.getName()).thenReturn(INPUT_DATA_SET_NAME);
		Mockito.when(result.getInputDatas()).thenReturn(INPUT_DATAS);
		return result;
	}

	private static Set<IdentifiedProteinSet> mockProteinSets() {
		Set<IdentifiedProteinSet> result = new THashSet<IdentifiedProteinSet>();
		result.add(mockIdentifiedProteinSet());
		return result;
	}

	private static IdentifiedProteinSet mockIdentifiedProteinSet() {
		IdentifiedProteinSet proteinSet = Mockito.mock(IdentifiedProteinSet.class);
		Mockito.when(proteinSet.getFileLocation()).thenReturn(PROTEIN_SET_LOCATION);
		Mockito.when(proteinSet.getName()).thenReturn(PROTEIN_SET_NAME);
		Map<String, IdentifiedProtein> identifiedProteinList = IDENTIFIED_PROTEINS;
		Mockito.when(proteinSet.getIdentifiedProteins()).thenReturn(identifiedProteinList);
		Mockito.when(proteinSet.getInputParameter()).thenReturn(INPUT_PARAMETER);
		Mockito.when(proteinSet.getInputDataSets()).thenReturn(INPUT_DATA_SETS);

		return proteinSet;
	}

	private static Set<MSIAdditionalInformation> MockMSIAdditionalInformations() {
		Set<MSIAdditionalInformation> result = new THashSet<MSIAdditionalInformation>();
		result.add(mockAdditionalInformation(0));
		return result;
	}

	private static MSIAdditionalInformation mockAdditionalInformation(int i) {
		MSIAdditionalInformation hibAddInfo = Mockito.mock(MSIAdditionalInformation.class);
		Mockito.when(hibAddInfo.getName()).thenReturn(ADDINFONAME + i);
		Mockito.when(hibAddInfo.getValue()).thenReturn(ADDINFOVALUE + i);
		return hibAddInfo;
	}

	private static Set<Validation> mockValidations() {
		Set<Validation> result = new THashSet<Validation>();
		result.add(mockValidation(0));
		return result;
	}

	private static Validation mockValidation(int i) {
		Validation result = Mockito.mock(Validation.class);
		Mockito.when(result.getName()).thenReturn(VALIDATION_NAME + 0);
		Mockito.when(result.getGlobalThresholds()).thenReturn(GLOBAL_THRESHOLDS + 0);

		Mockito.when(result.getStatisticalAnalysisResults()).thenReturn(VALIDATION_RESULTS + 0);
		Set<Software> softwares = mockValidationSoftwares();
		Set<PostProcessingMethod> algorithms = mockValidationMethods();
		Mockito.when(result.getPostProcessingMethods()).thenReturn(algorithms);
		Mockito.when(result.getPostProcessingSoftwares()).thenReturn(softwares);

		return result;
	}

	private static Set<Software> mockValidationSoftwares() {
		Set<Software> result = new THashSet<Software>();
		result.add(MiapeMocker.mockSoftware(0));
		return result;
	}

	private static Set<PostProcessingMethod> mockValidationMethods() {
		Set<PostProcessingMethod> result = new THashSet<PostProcessingMethod>();
		result.add(MiapeMocker.mockValidationAlgorithm(2));
		return result;
	}

	private static Set<Software> mockMsiSoftwares() {
		Set<Software> result = new THashSet<Software>();
		result.add(MiapeMocker.mockSoftware(1));
		return result;
	}

	private static Set<InputParameter> mockInputParameters() {
		Set<InputParameter> result = new THashSet<InputParameter>();
		result.add(INPUT_PARAMETER);
		return result;
	}

	private static InputParameter mockInputParameter(int i) {
		mockedInputParameter = Mockito.mock(InputParameter.class);
		Mockito.when(mockedInputParameter.getAaModif()).thenReturn(AA_MODIF + i);
		Mockito.when(mockedInputParameter.getAdditionalCleavages()).thenReturn(ADDITIONAL_CLEAVAGES + i);

		Mockito.when(mockedInputParameter.getCleavageRules()).thenReturn(CLEAVAGE_RULES + i);
		Set<Database> databases = mockDatabases();
		Mockito.when(mockedInputParameter.getDatabases()).thenReturn(databases);
		Mockito.when(mockedInputParameter.getPrecursorMassTolerance()).thenReturn(MASS_TOLERANCE_MS + i);
		Mockito.when(mockedInputParameter.getPmfMassTolerance()).thenReturn(MASS_TOLERANCE_PMF + i);
		Mockito.when(mockedInputParameter.getFragmentMassTolerance()).thenReturn(FRAGMENT_MASS_TOLERANCE + i);

		Mockito.when(mockedInputParameter.getPrecursorMassToleranceUnit())
				.thenReturn(PRECURSOR_MASS_TOLERANCE_UNIT + i);
		Mockito.when(mockedInputParameter.getPmfMassToleranceUnit()).thenReturn(MASS_TOLERANCE_PMF_UNIT + i);
		Mockito.when(mockedInputParameter.getFragmentMassToleranceUnit()).thenReturn(FRAGMENT_MASS_TOLERANCE_UNIT + i);

		Mockito.when(mockedInputParameter.getMinScore()).thenReturn(MIN_SCORE + i);
		Mockito.when(mockedInputParameter.getMisscleavages()).thenReturn(MISSCLEAVAGES + i);
		Mockito.when(mockedInputParameter.getName()).thenReturn(INPUT_PARAMETER_NAME + i);
		Mockito.when(mockedInputParameter.getTaxonomy()).thenReturn(TAXONOMY + i);
		Mockito.when(mockedInputParameter.getNumEntries()).thenReturn(NUM_ENTRIES + i);
		Mockito.when(mockedInputParameter.getCleavageName()).thenReturn(CLEAVAGE_NAME + i);
		Set<AdditionalParameter> addParameters = mockParameters();
		Mockito.when(mockedInputParameter.getAdditionalParameters()).thenReturn(addParameters);
		Mockito.when(mockedInputParameter.getSearchType()).thenReturn(SEARCHTYPE);

		return mockedInputParameter;
	}

	private static Set<AdditionalParameter> mockParameters() {
		Set<AdditionalParameter> additionalParameters = new THashSet<AdditionalParameter>();
		AdditionalParameter addParameter = Mockito.mock(AdditionalParameter.class);
		Mockito.when(addParameter.getName()).thenReturn(PARAMETER_NAME);
		Mockito.when(addParameter.getValue()).thenReturn(PARAMETER_VALUE);
		additionalParameters.add(addParameter);
		return additionalParameters;
	}

	private static Set<Algorithm> mockScoringAlgorithms() {
		Set<Algorithm> result = new THashSet<Algorithm>();
		result.add(SCORING_ALGORITHM);
		return result;
	}

	private static Set<Database> mockDatabases() {
		Set<Database> result = new THashSet<Database>();
		Database db = mockDatabase(0);
		result.add(db);
		return result;
	}

	private static Database mockDatabase(int i) {
		Database result = Mockito.mock(Database.class);
		Mockito.when(result.getDate()).thenReturn(DATE);
		Mockito.when(result.getDescription()).thenReturn(DATABASE_DESCRIPTION + i);
		Mockito.when(result.getName())
				.thenReturn(DatabaseName.getInstance(cvManager).getPossibleValues().get(0).getPreferredName());
		Mockito.when(result.getNumVersion()).thenReturn(DATABASE_VERSION + i);
		Mockito.when(result.getSequenceNumber()).thenReturn(SEQUENCE_NUMBER + i);
		Mockito.when(result.getUri()).thenReturn(DATABASE_URI + i);
		return result;
	}

	private static Set<InputData> mockInputDatas() {
		Set<InputData> result = new THashSet<InputData>();
		result.add(mockInputData(0));
		return result;
	}

	private static InputData mockInputData(int i) {
		InputData result = Mockito.mock(InputData.class);
		Mockito.when(result.getDescription()).thenReturn(INPUT_DATA_DESCRIPTION + i);
		Mockito.when(result.getName()).thenReturn(INPUT_DATA_NAME + i);
		Mockito.when(result.getSourceDataUrl()).thenReturn(SOURCE_DATA_URL + i);

		Mockito.when(result.getMSFileType()).thenReturn(RAWFILETYPE + i);

		return result;
	}

	private static Map<String, IdentifiedProtein> mockProteins() {
		Map<String, IdentifiedProtein> result = new THashMap<String, IdentifiedProtein>();
		IDENTIFIED_PROTEIN = mockIdentifiedProtein(0);
		result.put(IDENTIFIED_PROTEIN.getAccession(), IDENTIFIED_PROTEIN);
		return result;
	}

	private static IdentifiedProtein mockIdentifiedProtein(int i) {
		IdentifiedProtein result = Mockito.mock(IdentifiedProtein.class);
		Mockito.when(result.getAccession()).thenReturn(ACCESSION + i);
		Mockito.when(result.getAdditionalInformation()).thenReturn(ADDITIONAL_INFORMATION + i);
		Mockito.when(result.getCoverage()).thenReturn(COVERAGE + i);
		Mockito.when(result.getDescription()).thenReturn(PROTEIN_DESCRIPTION + i);
		Mockito.when(result.getPeaksMatchedNumber()).thenReturn(MATCHED_NUMBER + i);
		Mockito.when(result.getUnmatchedSignals()).thenReturn(UNMATCHED_NUMBER + i);
		Mockito.when(result.getPeptideNumber()).thenReturn(PEPTIDE_NUMBER + i);
		PROTEIN_SCORES = mockMSIProteinScores();
		Mockito.when(result.getScores()).thenReturn(PROTEIN_SCORES);
		PROTEIN_VALIDATION_STATUS = Boolean.valueOf(true);
		Mockito.when(result.getValidationStatus()).thenReturn(PROTEIN_VALIDATION_STATUS);
		Mockito.when(result.getValidationType()).thenReturn(PROTEIN_VALIDATION_TYPE);
		Mockito.when(result.getValidationValue()).thenReturn(PROTEIN_VALIDATION_VALUE);

		return result;
	}

	private static List<IdentifiedPeptide> mockIdentifiedPeptides() {
		List<IdentifiedPeptide> result = new ArrayList<IdentifiedPeptide>();

		IDENTIFIED_PEPTIDE1 = mockIdentifiedPeptide(0);
		result.add(IDENTIFIED_PEPTIDE1);
		IDENTIFIED_PEPTIDE2 = mockIdentifiedPeptide(1);
		result.add(IDENTIFIED_PEPTIDE2);

		return result;
	}

	private static IdentifiedPeptide mockIdentifiedPeptide(int i) {
		IdentifiedPeptide result = Mockito.mock(IdentifiedPeptide.class);
		Mockito.when(result.getCharge()).thenReturn(CHARGE + i);
		Mockito.when(result.getMassDesviation()).thenReturn(MASS_DESVIATION + i);
		Mockito.when(result.getModifications()).thenReturn(MODIFICATIONS);
		PEPTIDE_SCORES = mockMSIPeptideScores();
		Mockito.when(result.getScores()).thenReturn(PEPTIDE_SCORES);
		Mockito.when(result.getSequence()).thenReturn(SEQUENCE + i);
		Mockito.when(result.getSpectrumRef()).thenReturn(SPECTRUM_Ref);

		return result;
	}
}
