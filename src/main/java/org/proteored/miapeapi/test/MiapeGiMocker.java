package org.proteored.miapeapi.test;

import java.util.Set;

import org.mockito.Mockito;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.AnalysisDesign;
import org.proteored.miapeapi.interfaces.gi.DataAnalysis;
import org.proteored.miapeapi.interfaces.gi.DataExtraction;
import org.proteored.miapeapi.interfaces.gi.DataReporting;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;
import org.proteored.miapeapi.interfaces.gi.GIAdditionalInformation;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.ImagePreparationStep;
import org.proteored.miapeapi.interfaces.gi.ImageProcessing;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;
import org.proteored.miapeapi.interfaces.gi.Matching;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;

import gnu.trove.set.hash.THashSet;

public class MiapeGiMocker {

	public static final String IMAGE2_URI = "http://image2.com";
	public static final String IMAGE1_URI = "http://image1.com";
	public static Set<ImageGelInformatics> IMAGES = mockImages();
	public static final String PREPARATION_TYPE = "preparationType";
	public static final String SORDER = "sorder";
	public static final String PREPARATION_PARAMETERS = "preparationParameters";
	public static final String PREPARATION_NAME = "preparationName";
	public static final String TABLE_URI = "TableUri";
	public static final String SATURATED = "Saturated";
	public static final String RESULTS_URI = "ResultsUri";
	public static final String RESULTS_DESCRIPTION = "ResultsDescription";
	public static final String NUMBER_MATCHED = "NMat";
	public static final String NUMBER_DETECTED = "NDe";
	public static final String DATA_REPORTING_NAME = "dataReportingName";
	public static final String MATCHES_URI = "MatchesUri";
	public static final String MATCHES_LIST = "MatchesList";
	public static final String MATCHING_STEP_ORDER = "sOrder";
	public static final String REFERENCE_IMAGE_URI = "ReferenceImageUri";
	public static final String MATCHING_NAME = "matchingName";
	public static final String LANDMARKS = "Landmarks";
	public static final String MATCHING_EDITING = "matchingEditing";
	public static final String FEATURE_LIST = "FeatureList";
	public static final String FEATURE_URI = "FeatureURI";

	public static final String DYNAMIC_RANGE = "dynamicRange";
	private static final DataExtraction DATA_EXTRACTION = mockDataExtraction(0);

	public static final String COEFFICIENT_VARIANCE_AVERAGE = "c%";
	public static final String COEFFICIENT_VARIANCE30 = "3%";
	public static final String COEFFICIENT_VARIANCE20 = "2%";
	public static final String COEFFICIENT_VARIANCE10 = "10%";
	public static final String FEATURE_QUANTITATION_TYPE = "featureQuantitationType";
	public static final String FEATURE_QUANTITATION_STEP_ORDER = "stepor";
	public static final String FEATURE_QUANTITATION_NAME = "featureQuantitationName";
	public static final String FEATURE_DETECTION_STEP_ORDER = "Order";
	public static final String FEATURE_DETECTION_NAME = "featureDetectionName";
	public static final String EDITING = "Editing";
	public static final String DATA_EXTRACTION_OUTPUT_IMAGE_URI = "dataExtractionOutputImageUri";
	public static final String DATA_EXTRACTION_NAME = "dataExtractionName";
	public static final String DATA_EXTRACTION_INPUT_IMAGE_URI = "dataExtractionInputImageUri";
	public static final String ID_IMAGE = "IdImage";
	public static final String REFERENCE = "reference";
	public static final Algorithm ALGORITHM8 = MiapeMocker.mockAlgorithm(8);
	public static final String DATA_ANALYSIS_TYPE = "dataAnalysisType";
	public static final String DATA_ANALYSIS_PARAMETERS = "dataAnalysisParameters";
	public static final String DATA_ANALYSIS_NAME = "dataAnalysisName";
	public static final String INTENT = "Intent";
	public static final String INPUT_DATA = "inputData";
	public static final String STEP_ORDER = "StepO";
	public static final String ANALYSIS_PROCESSING_STEP_NAME = "analysisProcessingStepName";
	public static final String ANALYSIS_PROCESSING_TYPE = "analysisProcessingType";
	public static final String ANALYSIS_PROCESSING_STEP_ORDER = "StepOrder";
	public static final String IMAGE_PROCESSING_NAME = "analysisProcessingName";
	public static final String INPUT_IMAGE_URI = "InputImageUri";
	public static final String WITHIN_SAMPLE_STANDARD = "WithinSampleStandard";
	public static final String ANALYSIS_DESIGN_TYPE = "AnalysisDesignType";
	public static final String STANDARD = "Standard";
	public static final String REPLICATES = "Replicates";
	public static final String ANALYSIS_DESIGN_NAME = "analysisDesignName";
	public static final String GROUPS = "Groups";
	public static final String EXTERNAL_STANDARD = "ExternalStandard";
	public static final String GI_ELECTROPHORESIS_TYPE = "GiElectrophoresisType";

	private static final Set<ImagePreparationStep> PREPARATIONS = mockPreparations();

	private static final Set<ImageAnalysisSoftware> IMAGE_PROCESSING_SOFTWARES = mockAnalysisProcessingSoftwares();
	private static final Set<Algorithm> DATA_ANALYSIS_TRANSFORMATIONS = mockDataAnalysisTransformations();
	private static final Set<ImageAnalysisSoftware> DATA_ANALYSIS_SOFTWARES = mockDataAnalysisSoftwares();
	private static final Set<ImageAnalysisSoftware> ANALYSIS_SOFTWARES = mockAnalysisSoftwares();

	public static final Set<AnalysisDesign> ANALYSIS_DESIGNS = mockAnalysisDesigns();
	private static final Set<ImageProcessing> IMAGE_PROCESSINGS = mockImageProcessings();
	private static final Set<DataAnalysis> DATA_ANALYSISES = mockDataAnalysises();
	private static final Set<DataReporting> DATA_REPORTINGS = mockDataReportings();
	private static final Set<DataExtraction> DATA_EXTRACTIONS = mockDataExtractions();
	private static final Set<ImageAnalysisSoftware> PREPARATION_SOFTWARES = mockPreparationSoftwares();
	public static final String ADDINFO_NAME = "ADDINFONAME";
	public static final String ADDINFO_VALUE = "ADDINFOVALUE";
	private static final Set<GIAdditionalInformation> ADDITIONAL_INFORMATIONS = mockAdditionalInformations();
	public static final String ALGORITHM_CATALOG_NUMBER = "AlgorithmCatalognumber";
	public static final String ALGORITHM_COMMENTS = "AlgorithmCommnets";
	public static final String ALGORITHM_DESCRIPTION = "AlgorithmDescription";
	public static final String ALGORITHM_MANUFACTURER = "AlgorithmManufacturer";
	public static final String ALGORITHM_MODEL = "AlgorithmModel";
	public static final String ALGORITHM_PARAMETERS = "AlgorithmPArameters";
	public static final String ALGORITHM_URI = "AlgorithmURI";
	public static final String ALGORITHM_VERSION = "AlgorithmVersion";
	public static Set<String> PROCESSED_IMAGE_URIS;
	public static ImageGelInformatics IMAGE_1;
	public static ImageGelInformatics IMAGE_2;

	public static MiapeGIDocument mockGIMiapeDocument() {

		MiapeGIDocument miape = Mockito.mock(MiapeGIDocument.class);
		MiapeMocker.mockMiapeDocument(miape);

		GEContact contact_gi = MiapeMocker.mockGEContact();
		Mockito.when(miape.getContact()).thenReturn(contact_gi);
		Mockito.when(miape.getElectrophoresisType()).thenReturn(GI_ELECTROPHORESIS_TYPE);
		Mockito.when(miape.getReference()).thenReturn(REFERENCE);
		Mockito.when(miape.getAnalysisDesigns()).thenReturn(ANALYSIS_DESIGNS);
		Mockito.when(miape.getImageProcessings()).thenReturn(IMAGE_PROCESSINGS);
		Mockito.when(miape.getImageAnalysisSoftwares()).thenReturn(ANALYSIS_SOFTWARES);
		Mockito.when(miape.getDataAnalysises()).thenReturn(DATA_ANALYSISES);
		Mockito.when(miape.getDataExtractions()).thenReturn(DATA_EXTRACTIONS);
		Mockito.when(miape.getDataReportings()).thenReturn(DATA_REPORTINGS);
		MiapeGEDocument document = MiapeGeMocker.mockGEMiapeDocument();
		/*
		 * final int id = document.getId();
		 * Mockito.when(miape.getGEDocumentReference()).thenReturn(id);
		 */

		Mockito.when(miape.getImages()).thenReturn(IMAGES);

		Mockito.when(miape.getImagePreparationSteps()).thenReturn(PREPARATIONS);
		Mockito.when(miape.getImagePreparationSoftwares()).thenReturn(PREPARATION_SOFTWARES);
		Mockito.when(miape.getAdditionalInformations()).thenReturn(ADDITIONAL_INFORMATIONS);
		return miape;
	}

	private static Set<GIAdditionalInformation> mockAdditionalInformations() {
		Set<GIAdditionalInformation> result = new THashSet<GIAdditionalInformation>();
		result.add(MiapeMocker.mockGIAdditionalInformation(0));
		return result;
	}

	private static Set<ImageAnalysisSoftware> mockPreparationSoftwares() {
		Set<ImageAnalysisSoftware> result = new THashSet<ImageAnalysisSoftware>();
		result.add(MiapeMocker.mockImageAnalysisSoftware(29));
		return result;
	}

	private static Set<ImagePreparationStep> mockPreparations() {
		Set<ImagePreparationStep> result = new THashSet<ImagePreparationStep>();
		result.add(mockPreparation(0));
		return result;
	}

	private static ImagePreparationStep mockPreparation(int i) {
		ImagePreparationStep preparation = Mockito.mock(ImagePreparationStep.class);
		Mockito.when(preparation.getName()).thenReturn(PREPARATION_NAME + i);
		Mockito.when(preparation.getParameters()).thenReturn(PREPARATION_PARAMETERS + i);
		Mockito.when(preparation.getStepOrder()).thenReturn(SORDER + i);
		Mockito.when(preparation.getType()).thenReturn(PREPARATION_TYPE + i);
		return preparation;
	}

	private static Set<ImageGelInformatics> mockImages() {
		Set<ImageGelInformatics> result = new THashSet<ImageGelInformatics>();
		IMAGE_1 = mockImage(1);
		IMAGE_2 = mockImage(2);
		result.add(IMAGE_1);
		result.add(IMAGE_2);
		return result;
	}

	private static Set<DataReporting> mockDataReportings() {
		Set<DataReporting> result = new THashSet<DataReporting>();
		result.add(mockDataReporting(0));
		return result;
	}

	private static DataReporting mockDataReporting(int i) {
		DataReporting dataReporting = Mockito.mock(DataReporting.class);
		Mockito.when(dataReporting.getFeatureList()).thenReturn(FEATURE_LIST + i);
		Mockito.when(dataReporting.getFeatureURI()).thenReturn(FEATURE_URI + i);
		Mockito.when(dataReporting.getMatchesList()).thenReturn(MATCHES_LIST + i);
		Mockito.when(dataReporting.getMatchesURI()).thenReturn(MATCHES_URI + i);
		Mockito.when(dataReporting.getName()).thenReturn(DATA_REPORTING_NAME + i);
		Mockito.when(dataReporting.getResultsDescription()).thenReturn(RESULTS_DESCRIPTION + i);
		Mockito.when(dataReporting.getResultsURI()).thenReturn(RESULTS_URI + i);

		return dataReporting;
	}

	private static Set<DataExtraction> mockDataExtractions() {
		Set<DataExtraction> result = new THashSet<DataExtraction>();
		result.add(DATA_EXTRACTION);
		return result;
	}

	private static DataExtraction mockDataExtraction(int i) {
		DataExtraction dataExtraction = Mockito.mock(DataExtraction.class);
		Mockito.when(dataExtraction.getInputImages()).thenReturn(IMAGES);
		PROCESSED_IMAGE_URIS = new THashSet<String>();
		PROCESSED_IMAGE_URIS.add(IMAGE1_URI);
		Mockito.when(dataExtraction.getInputImageUris()).thenReturn(PROCESSED_IMAGE_URIS);
		Mockito.when(dataExtraction.getName()).thenReturn(DATA_EXTRACTION_NAME + i);
		Set<FeatureDetection> featureDetections = new THashSet<FeatureDetection>();
		featureDetections.add(mockFeatureDetection(0));
		Mockito.when(dataExtraction.getFeatureDetections()).thenReturn(featureDetections);
		Set<FeatureQuantitation> quantitations = new THashSet<FeatureQuantitation>();
		quantitations.add(mockFeatureQuantitation(0));
		Mockito.when(dataExtraction.getFeatureQuantitations()).thenReturn(quantitations);
		Set<Matching> matchings = new THashSet<Matching>();
		matchings.add(mockMatching(0));
		Mockito.when(dataExtraction.getMatchings()).thenReturn(matchings);

		return dataExtraction;
	}

	private static Matching mockMatching(int i) {
		Matching matching = Mockito.mock(Matching.class);
		Mockito.when(matching.getEditing()).thenReturn(MATCHING_EDITING + i);
		Mockito.when(matching.getReferenceImage()).thenReturn(IMAGE_1);
		Mockito.when(matching.getLandmarks()).thenReturn(LANDMARKS + i);
		Mockito.when(matching.getName()).thenReturn(MATCHING_NAME + i);
		Mockito.when(matching.getCatalogNumber()).thenReturn(MiapeMocker.ALGORITHM_C_ATALOG_NUMBER + i);
		Mockito.when(matching.getStepOrder()).thenReturn(MATCHING_STEP_ORDER + i);
		Mockito.when(matching.getReferenceImage()).thenReturn(IMAGE_1);
		return matching;
	}

	private static FeatureQuantitation mockFeatureQuantitation(int i) {
		FeatureQuantitation featureQuantitation = Mockito.mock(FeatureQuantitation.class);
		Mockito.when(featureQuantitation.getName()).thenReturn(FEATURE_QUANTITATION_NAME + i);
		Mockito.when(featureQuantitation.getStepOrder()).thenReturn(FEATURE_QUANTITATION_STEP_ORDER + i);
		Mockito.when(featureQuantitation.getType()).thenReturn(FEATURE_QUANTITATION_TYPE + i);
		Set<Algorithm> normalizations = new THashSet<Algorithm>();
		Algorithm algorithm14 = MiapeMocker.mockAlgorithm(14);
		Algorithm algorithm13 = MiapeMocker.mockAlgorithm(13);
		Algorithm algorithm12 = MiapeMocker.mockAlgorithm(12);
		normalizations.add(algorithm14);
		Set<Algorithm> backgrounds = new THashSet<Algorithm>();
		backgrounds.add(algorithm13);
		Set<Algorithm> algorithms = new THashSet<Algorithm>();
		algorithms.add(algorithm12);
		Mockito.when(featureQuantitation.getFeatureQuantitationAlgorithms()).thenReturn(algorithms);
		Mockito.when(featureQuantitation.getFeatureQuantitationBackgrounds()).thenReturn(backgrounds);
		Mockito.when(featureQuantitation.getFeatureQuantitationNormalizations()).thenReturn(normalizations);

		return featureQuantitation;
	}

	private static FeatureDetection mockFeatureDetection(int i) {
		FeatureDetection featureDetection = Mockito.mock(FeatureDetection.class);
		Mockito.when(featureDetection.getEditing()).thenReturn(EDITING + i);
		Mockito.when(featureDetection.getName()).thenReturn(FEATURE_DETECTION_NAME + i);
		Mockito.when(featureDetection.getStepOrder()).thenReturn(FEATURE_DETECTION_STEP_ORDER + i);
		Mockito.when(featureDetection.getCatalogNumber()).thenReturn(MiapeMocker.ALGORITHM_C_ATALOG_NUMBER);
		Mockito.when(featureDetection.getComments()).thenReturn(MiapeMocker.ALGORITHM_COMMENTS);
		Mockito.when(featureDetection.getDescription()).thenReturn(MiapeMocker.ALGORITHM_DESCRIPTION);

		return featureDetection;
	}

	private static Set<DataAnalysis> mockDataAnalysises() {
		Set<DataAnalysis> result = new THashSet<DataAnalysis>();
		result.add(mockDataAnalysis(0));
		return result;
	}

	private static DataAnalysis mockDataAnalysis(int i) {
		DataAnalysis dataAnalysis = Mockito.mock(DataAnalysis.class);
		Mockito.when(dataAnalysis.getDataAnalysisSoftwares()).thenReturn(DATA_ANALYSIS_SOFTWARES);
		Mockito.when(dataAnalysis.getDataAnalysisTransformations()).thenReturn(DATA_ANALYSIS_TRANSFORMATIONS);
		Mockito.when(dataAnalysis.getInputData()).thenReturn(INPUT_DATA + i);
		Mockito.when(dataAnalysis.getIntent()).thenReturn(INTENT + i);
		Mockito.when(dataAnalysis.getName()).thenReturn(DATA_ANALYSIS_NAME + i);
		Mockito.when(dataAnalysis.getParameters()).thenReturn(DATA_ANALYSIS_PARAMETERS + i);
		Mockito.when(dataAnalysis.getType()).thenReturn(DATA_ANALYSIS_TYPE + i);

		return dataAnalysis;
	}

	private static Set<Algorithm> mockDataAnalysisTransformations() {
		Set<Algorithm> result = new THashSet<Algorithm>();
		result.add(MiapeMocker.mockAlgorithm(9));
		return result;
	}

	private static Set<ImageAnalysisSoftware> mockDataAnalysisSoftwares() {
		Set<ImageAnalysisSoftware> result = new THashSet<ImageAnalysisSoftware>();
		result.add(MiapeMocker.mockImageAnalysisSoftware(9));
		return result;
	}

	private static Set<ImageAnalysisSoftware> mockAnalysisSoftwares() {
		Set<ImageAnalysisSoftware> result = new THashSet<ImageAnalysisSoftware>();
		result.add(MiapeMocker.mockImageAnalysisSoftware(8));
		return result;
	}

	private static Set<ImageProcessing> mockImageProcessings() {
		Set<ImageProcessing> result = new THashSet<ImageProcessing>();
		result.add(mockImageProcessing(0));
		return result;
	}

	private static ImageProcessing mockImageProcessing(int i) {
		ImageProcessing imageProcessing = Mockito.mock(ImageProcessing.class);
		Mockito.when(imageProcessing.getInputImages()).thenReturn(IMAGES);
		Mockito.when(imageProcessing.getName()).thenReturn(IMAGE_PROCESSING_NAME + i);
		Mockito.when(imageProcessing.getImageProcessingSoftwares()).thenReturn(IMAGE_PROCESSING_SOFTWARES);
		Set<ImageProcessingStep> steps = new THashSet<ImageProcessingStep>();
		steps.add(mockAnalysisProcessingStep(0));

		Mockito.when(imageProcessing.getImageProcessingSteps()).thenReturn(steps);

		return imageProcessing;
	}

	private static ImageProcessingStep mockAnalysisProcessingStep(int i) {
		ImageProcessingStep analysisProcessingStep = Mockito.mock(ImageProcessingStep.class);
		Mockito.when(analysisProcessingStep.getCatalogNumber()).thenReturn(ALGORITHM_CATALOG_NUMBER);
		Mockito.when(analysisProcessingStep.getComments()).thenReturn(ALGORITHM_COMMENTS);
		Mockito.when(analysisProcessingStep.getDescription()).thenReturn(ALGORITHM_DESCRIPTION);
		Mockito.when(analysisProcessingStep.getManufacturer()).thenReturn(ALGORITHM_MANUFACTURER);
		Mockito.when(analysisProcessingStep.getModel()).thenReturn(ALGORITHM_MODEL);
		Mockito.when(analysisProcessingStep.getParameters()).thenReturn(ALGORITHM_PARAMETERS);
		Mockito.when(analysisProcessingStep.getURI()).thenReturn(ALGORITHM_URI);
		Mockito.when(analysisProcessingStep.getVersion()).thenReturn(ALGORITHM_VERSION);
		Mockito.when(analysisProcessingStep.getName()).thenReturn(ANALYSIS_PROCESSING_STEP_NAME + i);
		Mockito.when(analysisProcessingStep.getStepOrder()).thenReturn(STEP_ORDER + i);

		return analysisProcessingStep;
	}

	private static Set<ImageAnalysisSoftware> mockAnalysisProcessingSoftwares() {
		Set<ImageAnalysisSoftware> result = new THashSet<ImageAnalysisSoftware>();
		result.add(MiapeMocker.mockImageAnalysisSoftware(0));
		return result;
	}

	private static Set<AnalysisDesign> mockAnalysisDesigns() {
		Set<AnalysisDesign> result = new THashSet<AnalysisDesign>();
		result.add(mockAnalysisDesign(0));
		return result;
	}

	private static AnalysisDesign mockAnalysisDesign(int i) {
		AnalysisDesign analysisDesign = Mockito.mock(AnalysisDesign.class);
		Mockito.when(analysisDesign.getExternalStandard()).thenReturn(EXTERNAL_STANDARD + i);
		Mockito.when(analysisDesign.getGroups()).thenReturn(GROUPS + i);
		Mockito.when(analysisDesign.getName()).thenReturn(ANALYSIS_DESIGN_NAME + i);
		Mockito.when(analysisDesign.getReplicates()).thenReturn(REPLICATES + i);
		Mockito.when(analysisDesign.getStandard()).thenReturn(STANDARD + i);
		Mockito.when(analysisDesign.getType()).thenReturn(ANALYSIS_DESIGN_TYPE + i);
		Mockito.when(analysisDesign.getWithinSampleStandard()).thenReturn(WITHIN_SAMPLE_STANDARD + i);

		return analysisDesign;
	}

	private static ImageGelInformatics mockImage(int i) {
		ImageGelInformatics image = Mockito.mock(ImageGelInformatics.class);
		Mockito.when(image.getBitDepth()).thenReturn(MiapeMocker.BIT_DEPTH + i);
		Mockito.when(image.getDimensionUnit()).thenReturn(MiapeMocker.DIMENSION_UNIT + i);
		Mockito.when(image.getDimensionX()).thenReturn(MiapeMocker.DIMENSION_X + i);
		Mockito.when(image.getDimensionY()).thenReturn(MiapeMocker.DIMENSION_Y + i);
		Mockito.when(image.getFormat()).thenReturn(MiapeMocker.FORMAT + i);
		Mockito.when(image.getLocation()).thenReturn(MiapeMocker.LOCATION + i);
		Mockito.when(image.getName()).thenReturn(MiapeMocker.IMAGE_NAME + i);
		Mockito.when(image.getOrientation()).thenReturn(MiapeMocker.ORIENTATION + i);
		Mockito.when(image.getResolution()).thenReturn(MiapeMocker.RESOLUTION + i);
		Mockito.when(image.getResolutionUnit()).thenReturn(MiapeMocker.RESOLUTION_UNIT + i);
		Mockito.when(image.getType()).thenReturn(MiapeMocker.IMAGE_TYPE + i);
		return image;
	}

}
