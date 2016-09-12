package org.proteored.miapeapi.test;

import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;
import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.Substance;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;
import org.proteored.miapeapi.interfaces.ge.ElectrophoresisProtocol;
import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionEquipment;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionSoftware;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;
import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;
import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.ge.Protocol;
import org.proteored.miapeapi.interfaces.ge.Sample;
import org.proteored.miapeapi.interfaces.ge.SampleApplication;


public class MiapeGeMocker {
	public static final String SAMPLE_DESCRIPTION = "SampleDescription";
	public static final String SAMPLE_NAME = "SampleName";
	public static final Double DIMENSION_MATRIX_Z = Double.valueOf(12);
	public static final Double DIMENSION_MATRIX_Y = Double.valueOf(11);
	public static final Double DIMENSION_MATRIX_X = Double.valueOf(10);
	public static final String LANE_NUMBER = "L";
	public static final String LANE_DESCRIPTION = "LaneDescription";
	public static final String LANE_NAME = "LaneName";
	public static final String INTERDIMENSION_PROCESS_PROTOCOL = "interdimensionProcessProtocol";
	public static final String INTERDIMENSION_PROCESS_NAME = "interdimensionProcessName";
	public static final String ELECTROPHORESIS_CONDITIONS = "ElectrophoresisConditions";
	public static final String ELECTROPHORESIS_PROTOCOL_URL = "electrophoresisProtocolURL";
	public static final String ELECTROPHORESIS_NAME = "ElectrophoresisName";
	public static final String SEPARATION = "Separation";
	public static final String DIMENSION_NAME = "DimensionName";
	public static final String DIMENSION_DIMENSION = "Dimension";
	public static final String PROTOCOL_DESCRIPTION = "ProtocolDescription";
	public static final String PROTOCOL_NAME = "ProtocolName";
	public static final String TRANSFER_MEDIUM = "TransferMedium";
	public static final String ID_PROTOCOL = "IDProtocol";
	public static final String INDIRECT_DETECTION_NAME = "indirectDetectionName";
	public static final String DETECTION_MEDIUM = "DetectionMedium";

	public static final Integer CALIBRATION = Integer.valueOf(15);
	public static final String IMAGE_ACQUISITION_EQUIPMENT_PARAMETERES_URL = "imageAcquisitionEquipmentParameteresURL";
	public static final String IMAGE_ACQUISITION_EQUIPMENT_TYPE = "imageAcquisitionEquipmentType";
	public static final String IMAGE_ACQUISITION_PROTOCOL = "ImageAcquisitionProtocol";
	public static final String IMAGE_ACQUISITION_NAME = "ImageAcquisitionName";
	public static final String ELECTROPHORESIS_TYPE = "ElectrophoresisType";
	public static final String PROTOCOL = "Protocol";

	public static final String SAMPLE_APPLICATION_NAME = "sampleApplicationName";
	public static final String SAMPLE_APPLICATION_DESCRIPTION = "sampleApplicationDescription";
	public static final String MATRIX_TYPE = "MatrixType";
	public static final String PH_RANGE_TYPE = "PhRangeType";
	public static final String PH_RANGE_L = "PhRangeL";
	public static final String PH_RANGE_H = "PhRangeH";
	public static final String MW_RANGE_UNIT = "MwRangeUnit";
	public static final String MW_RANGE_TYPE = "MwRangeType";
	public static final String MW_RANGE_L = "MwRangeL";
	public static final String MW_RANGE_H = "MwRangeH";
	public static final String MANUFACTURE = "Manufacture";
	public static final String DIMENSIONS_UNIT = "DimensionsUnit";
	public static final String COMPOSED = "Composed";
	public static final String BISACRY = "Bisacry";
	public static final String ACRYLAMIDE_MANUFACTURER = "AcrylamideManufacturer";
	public static final String ACRYLAMIDE_CONCENTRATION = "AcrylamideConcentration";
	public static final String ACRY = "acry";
	public static final String MATRIX_NAME = "MatrixNameVEr";
	public static final String DIRECT_DETECTION_NAME = "directDetectionName";

	public static final Set<Substance> MOCK_SUBSTANCES = MiapeMocker.mockSubstances(2);
	public static final Set<Substance> MATRIX_SUBSTANCES = MiapeMocker.mockSubstances(5);
	public static final Set<Agent> MOCK_ADDITIONAL_MATRIX_SUBSTANCES = MiapeMocker.mockAdditionalMatrixSubstances(2);

	private static final Set<Buffer> MATRIX_BUFFERS = MiapeMocker.mockBuffers(1);
	private static final Set<Buffer> MATRIX_POLYMERIZATION_BUFFERS =  MiapeMocker.mockBuffers(2);
	private static final Set<Buffer> MATRIX_ADD_BUFFERS = MiapeMocker.mockBuffers(3);
	private static final Set<Buffer> MATRIX_POL_BUFFERS =MiapeMocker.mockBuffers(4);
	private static final ImageAcquisition IMAGE_ACQUISITION = mockImageAcquisition(0);
	private static final DirectDetection DIRECT_DETECTION = mockDirectDetection(0);
	public static final Set<Buffer> BUFFERS = MiapeMocker.mockBuffers(0);
	public static final Set<Equipment> DETECTION_EQUIPMENTS = mockDetectionEquipments(1);

	private static final Set<DirectDetection> DIRECT_DETECTIONS = mockDirectDetections();
	private static final Set<ImageAcquisition> IMAGE_ACQUISITIONS = mockImageAcquisitions();
	private static Set<ImageGelElectrophoresis> IMAGES;
	private static final Set<GEAdditionalInformation> ADDIDITIONAL_INFORMATIONS = mockAdditionalInformations();
	private static final Set<IndirectDetection> INDIRECT_DETECTIONS = mockIndirectDetections();
	private static Set<Protocol> PROTOCOLS ;
	private static Set<Sample> SAMPLES ;
	private static Sample SAMPLE;
	private static Sample SAMPLE2;
	private static GelMatrix GEL_MATRIX;


	public static final String LOWEST_PI = "LowestPi";
	public static final String LOWEST_MW = "LowestMw";
	public static final String H_STREAKING = "HStreaking";
	public static final String ADDITIONAL_INFORMATION_NAME = "additionalInformationName";
	public static final String V_STREAKING = "VStreaking";
	public static final String URL = "Url";
	public static final String HIGHEST_PI = "highestPi";
	public static final String HIGHEST_MW = "HighestMw";
	public static final String COMMENTS = "Comments";
	public static final String BASIC = "Basic";
	public static final String BACKGROUND = "Background";
	public static final String ADDINFONAME = "ADDINFONAME";
	public static final String ADDINFOVALUE = "ADDINFOVALUE";






	public static MiapeGEDocument mockGEMiapeDocument() {

		MiapeGEDocument miape = Mockito.mock(MiapeGEDocument.class);
		MiapeMocker.mockMiapeDocument(miape);
		GEContact contact_ge = MiapeMocker.mockGEContact();
		Mockito.when(miape.getContact()).thenReturn(contact_ge);
		SAMPLES = mockSamples();
		Mockito.when(miape.getSamples()).thenReturn(SAMPLES);
		Mockito.when(miape.getElectrophoresisType()).thenReturn(ELECTROPHORESIS_TYPE);
		Mockito.when(miape.getDirectDetections()).thenReturn(DIRECT_DETECTIONS);
		Mockito.when(miape.getImageAcquisitions()).thenReturn(IMAGE_ACQUISITIONS);
		Mockito.when(miape.getIndirectDetections()).thenReturn(INDIRECT_DETECTIONS);
		PROTOCOLS = mockProtocols();
		Mockito.when(miape.getProtocols()).thenReturn(PROTOCOLS);
		IMAGES = mockImages();
		Mockito.when(miape.getImages()).thenReturn(IMAGES);
		Mockito.when(miape.getAdditionalInformations()).thenReturn(ADDIDITIONAL_INFORMATIONS);



		return miape;
	}

	private static Set<GEAdditionalInformation> mockAdditionalInformations() {
		Set<GEAdditionalInformation> result = new HashSet<GEAdditionalInformation>();
		result.add(mockAdditionalInformation(0));
		return result;
	}

	private static GEAdditionalInformation mockAdditionalInformation(int i) {
		GEAdditionalInformation additionalInformation = Mockito.mock(GEAdditionalInformation.class);
		Mockito.when(additionalInformation.getName()).thenReturn(ADDINFONAME + i);
		Mockito.when(additionalInformation.getValue()).thenReturn(ADDINFOVALUE + i);
		return additionalInformation;	
	}

	private static Set<Sample> mockSamples() {
		Set<Sample> result = new HashSet<Sample>();
		SAMPLE = mockSample(0);
		result.add(SAMPLE);
		SAMPLE2 = mockSample(1);
		result.add(SAMPLE2);
		return result;
	}

	private static Sample mockSample(int i) {
		Sample sample = Mockito.mock(Sample.class);
		Mockito.when(sample.getName()).thenReturn(SAMPLE_NAME + i);
		Mockito.when(sample.getDescription()).thenReturn(SAMPLE_DESCRIPTION + i);
		return sample;
	}

	private static Lane mockLane(int i) {
		Lane lane = Mockito.mock(Lane.class);
		Mockito.when(lane.getName()).thenReturn(LANE_NAME);
		Mockito.when(lane.getDescription()).thenReturn(LANE_DESCRIPTION);
		Mockito.when(lane.getLaneNumber()).thenReturn(LANE_NUMBER);
		if (i==0) {
			Mockito.when(lane.getReferencedSample()).thenReturn(SAMPLE);
		}else{
			Mockito.when(lane.getReferencedSample()).thenReturn(SAMPLE2);
		}
		return lane;
	}

	private static Set<Protocol> mockProtocols() {
		Set<Protocol> result = new HashSet<Protocol>();
		result.add(mockProtocol(0));
		return result;
	}

	private static Protocol mockProtocol(int i) {
		Protocol protocol = Mockito.mock(Protocol.class);
		Mockito.when(protocol.getName()).thenReturn(PROTOCOL_NAME + i);
		Mockito.when(protocol.getDescription()).thenReturn(PROTOCOL_DESCRIPTION + i);
		Set<Dimension> dimensions = mockDimensions();
		Mockito.when(protocol.getDimensions()).thenReturn(dimensions);
		Set<InterdimensionProcess> processes = mockInterdimensionProcesses();
		Mockito.when(protocol.getInterdimensionProcesses()).thenReturn(processes);

		return protocol;
	}

	private static Set<InterdimensionProcess> mockInterdimensionProcesses() {
		Set<InterdimensionProcess> result = new HashSet<InterdimensionProcess>();
		result.add(mockInterdimensionProcess(0));
		return result;
	}

	private static InterdimensionProcess mockInterdimensionProcess(int i) {
		InterdimensionProcess interdimensionProcess =  Mockito.mock(InterdimensionProcess.class);
		Mockito.when(interdimensionProcess.getName()).thenReturn(INTERDIMENSION_PROCESS_NAME + i);
		Mockito.when(interdimensionProcess.getProtocol()).thenReturn(INTERDIMENSION_PROCESS_PROTOCOL + i);
		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(MiapeMocker.mockBuffer(20));
		Mockito.when(interdimensionProcess.getInterdimensionBuffers()).thenReturn(buffers);

		Set<Equipment> equipments = new HashSet<Equipment>();
		equipments.add(MiapeMocker.mockEquipment(21));
		Mockito.when(interdimensionProcess.getInterdimensionEquipments()).thenReturn(equipments);

		Set<Agent> substances = new HashSet<Agent>();
		substances.add(MiapeMocker.mockAgent(22));
		Mockito.when(interdimensionProcess.getInterdimensionReagents()).thenReturn(substances);

		return interdimensionProcess;
	}

	private static Set<Dimension> mockDimensions() {
		Set<Dimension> result = new HashSet<Dimension>();
		result.add(mockDimension(0));
		return result;
	}

	private static Dimension mockDimension(int i) {
		Dimension dimension = Mockito.mock(Dimension.class);
		Mockito.when(dimension.getDimension()).thenReturn(DIMENSION_DIMENSION + i);
		Mockito.when(dimension.getName()).thenReturn(DIMENSION_NAME + i);
		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(MiapeMocker.mockBuffer(12));
		Mockito.when(dimension.getLoadingBuffers()).thenReturn(buffers);
		Mockito.when(dimension.getSeparationMethod()).thenReturn(SEPARATION + i);
		Set<GelMatrix> matrixes = new HashSet<GelMatrix>();
		GEL_MATRIX = mockGelMatrix(0);
		matrixes.add(GEL_MATRIX);
		Mockito.when(dimension.getMatrixes()).thenReturn(matrixes);
		Set<ElectrophoresisProtocol> electrophoresisProtocols = mockElectrophoresisProtocols();
		Mockito.when(dimension.getElectrophoresisProtocols()).thenReturn(electrophoresisProtocols);

		return dimension;
	}

	private static Set<ElectrophoresisProtocol> mockElectrophoresisProtocols() {
		Set<ElectrophoresisProtocol> result = new HashSet<ElectrophoresisProtocol>();
		result.add(mockElectrophoresisProtocol(0));
		return result;
	}

	private static ElectrophoresisProtocol mockElectrophoresisProtocol(int i) {
		ElectrophoresisProtocol electrophoresisProtocol = Mockito.mock(ElectrophoresisProtocol.class);
		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(MiapeMocker.mockBuffer(15));
		Mockito.when(electrophoresisProtocol.getAdditionalBuffers()).thenReturn(buffers);
		Set<Buffer> buffers2 = new HashSet<Buffer>();
		buffers2.add(MiapeMocker.mockBuffer(16));
		Mockito.when(electrophoresisProtocol.getRunningBuffers()).thenReturn(buffers2);
		Mockito.when(electrophoresisProtocol.getElectrophoresisConditions()).thenReturn(ELECTROPHORESIS_CONDITIONS + i);
		Mockito.when(electrophoresisProtocol.getName()).thenReturn(ELECTROPHORESIS_NAME + i);
		return electrophoresisProtocol;
	}

	private static Set<IndirectDetection> mockIndirectDetections() {
		Set<IndirectDetection> result = new HashSet<IndirectDetection>();
		result.add(mockIndirectDetection(0));
		return result;
	}

	private static IndirectDetection mockIndirectDetection(int i) {
		IndirectDetection indirectDetection = Mockito.mock(IndirectDetection.class);


		Set<IndirectDetectionAgent> additionalAgents = new HashSet<IndirectDetectionAgent>();
		additionalAgents.add(MiapeMocker.mockIndirectDetectionAgent(6));
		Mockito.when(indirectDetection.getAdditionalAgents()).thenReturn(additionalAgents);

		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(MiapeMocker.mockBuffer(8));
		Mockito.when(indirectDetection.getBuffers()).thenReturn(buffers);

		Set<IndirectDetectionAgent> detectionAgents = new HashSet<IndirectDetectionAgent>();
		detectionAgents.add(MiapeMocker.mockIndirectDetectionAgent(7));
		Mockito.when(indirectDetection.getAgents()).thenReturn(detectionAgents);

		Set<Equipment> equipments = new HashSet<Equipment>();
		equipments.add(MiapeMocker.mockEquipment(9));
		Mockito.when(indirectDetection.getDetectionEquipments()).thenReturn(equipments);

		Mockito.when(indirectDetection.getDetectionMedium()).thenReturn(DETECTION_MEDIUM + i);
		Mockito.when(indirectDetection.getName()).thenReturn(INDIRECT_DETECTION_NAME + i);
		Mockito.when(indirectDetection.getProtocol()).thenReturn(ID_PROTOCOL + i);
		Mockito.when(indirectDetection.getTransferMedium()).thenReturn(TRANSFER_MEDIUM + i);

		return indirectDetection;
	}

	private static Set<ImageGelElectrophoresis> mockImages() {
		Set<ImageGelElectrophoresis> result = new HashSet<ImageGelElectrophoresis>();
		result.add(mockImageGelElectropheresis(0));
		return result;
	}

	private static ImageGelElectrophoresis mockImageGelElectropheresis(int i) {
		ImageGelElectrophoresis image = Mockito.mock(ImageGelElectrophoresis.class);
		MiapeMocker.mockImage(i, image);
		Mockito.when(image.getReferencedGelMatrix()).thenReturn(GEL_MATRIX);

		return image;
	}

	private static Set<Software> mockSoftwares() {
		Set<Software> result = new HashSet<Software>();
		result.add(MiapeMocker.mockSoftware(0));
		return result;
	}
	private static Set<ImageAcquisitionSoftware> mockImageAcquisitionSoftwares() {
		Set<ImageAcquisitionSoftware> result = new HashSet<ImageAcquisitionSoftware>();
		result.add(MiapeMocker.mockImageAcquisitionSoftware(0));
		return result;
	}

	private static ImageAcquisitionEquipment mockImageAcquisitionEquipment(int i) {
		ImageAcquisitionEquipment  imageAcquisitionEquipment = Mockito.mock(ImageAcquisitionEquipment.class);
		Mockito.when(imageAcquisitionEquipment.getCalibration()).thenReturn(CALIBRATION + i);
		Mockito.when(imageAcquisitionEquipment.getType()).thenReturn(IMAGE_ACQUISITION_EQUIPMENT_TYPE + i);
		Mockito.when(imageAcquisitionEquipment.getParametersUrl()).thenReturn(IMAGE_ACQUISITION_EQUIPMENT_PARAMETERES_URL + i);
		MiapeMocker.addEquipmentData(i, imageAcquisitionEquipment);
		return imageAcquisitionEquipment;
	}

	private static ImageAcquisition mockImageAcquisition(int i) {
		ImageAcquisition imageAcquisition = Mockito.mock(ImageAcquisition.class);
		Mockito.when(imageAcquisition.getName()).thenReturn(IMAGE_ACQUISITION_NAME + i);
		Mockito.when(imageAcquisition.getProtocol()).thenReturn(IMAGE_ACQUISITION_PROTOCOL + i);

		Mockito.when(imageAcquisition.getReferencedGelMatrix()).thenReturn(GEL_MATRIX);
		Set<ImageAcquisitionEquipment> imageAcquisitionEquipments = new HashSet<ImageAcquisitionEquipment>(); 
		ImageAcquisitionEquipment mockImageAcquisitionEquipment = mockImageAcquisitionEquipment(0);

		imageAcquisitionEquipments.add(mockImageAcquisitionEquipment);

		Mockito.when(imageAcquisition.getImageAcquisitionEquipments()).thenReturn(imageAcquisitionEquipments);
		Set<ImageAcquisitionSoftware> softwares = mockImageAcquisitionSoftwares();
		Mockito.when(imageAcquisition.getImageAcquisitionSoftwares()).thenReturn(softwares);

		return imageAcquisition;
	}

	private static Set<ImageAcquisition> mockImageAcquisitions() {
		Set<ImageAcquisition> result = new HashSet<ImageAcquisition>();
		result.add(IMAGE_ACQUISITION);
		return result;
	}




	private static Set<DirectDetection> mockDirectDetections() {
		Set<DirectDetection> result = new HashSet<DirectDetection>();
		result.add(DIRECT_DETECTION);
		return result;
	}

	private static DirectDetection mockDirectDetection(int i) {
		DirectDetection directDetection = Mockito.mock(DirectDetection.class);
		Mockito.when(directDetection.getName()).thenReturn(DIRECT_DETECTION_NAME + i);
		Mockito.when(directDetection.getProtocol()).thenReturn(PROTOCOL + i);
		Set<DirectDetectionAgent> additionalAgents = new HashSet<DirectDetectionAgent>();
		additionalAgents.add(MiapeMocker.mockDirectDetectionAgent(0));

		Set<DirectDetectionAgent> detectionAgents = new HashSet<DirectDetectionAgent>();
		detectionAgents.add(MiapeMocker.mockDirectDetectionAgent(1));

		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(MiapeMocker.mockBuffer(5));

		Set<Equipment> equipments = new HashSet<Equipment>();
		equipments.add(MiapeMocker.mockEquipment(5));

		Mockito.when(directDetection.getAdditionalAgents()).thenReturn(additionalAgents);
		Mockito.when(directDetection.getAgents()).thenReturn(detectionAgents);
		Mockito.when(directDetection.getBuffers()).thenReturn(buffers);
		Mockito.when(directDetection.getDetectionEquipments()).thenReturn(equipments);
		return directDetection;
	}

	private static Set<Equipment> mockDetectionEquipments(int i) {
		Set<Equipment> equipments = new HashSet<Equipment>();
		equipments.add(MiapeMocker.mockEquipment(i));
		return equipments;
	}

	public static GelMatrix mockGelMatrix(int i) {
		GelMatrix matrix = Mockito.mock(GelMatrix.class);
		Mockito.when(matrix.getName()).thenReturn(MATRIX_NAME + i);
		Mockito.when(matrix.getAcry()).thenReturn(ACRY + i);
		Mockito.when(matrix.getAcrylamideConcentration()).thenReturn(ACRYLAMIDE_CONCENTRATION + i);
		Mockito.when(matrix.getAdditionalMatrixBuffers()).thenReturn(MATRIX_BUFFERS);
		Mockito.when(matrix.getAdditionalMatrixSubstances()).thenReturn(MOCK_ADDITIONAL_MATRIX_SUBSTANCES);
		Mockito.when(matrix.getBisacry()).thenReturn(BISACRY + i);
		Mockito.when(matrix.getComposed()).thenReturn(COMPOSED + i);
		Mockito.when(matrix.getDimensionsUnit()).thenReturn(DIMENSIONS_UNIT + i);
		Mockito.when(matrix.getDimensionsX()).thenReturn(DIMENSION_MATRIX_X);
		Mockito.when(matrix.getDimensionsY()).thenReturn(DIMENSION_MATRIX_Y);
		Mockito.when(matrix.getDimensionsZ()).thenReturn(DIMENSION_MATRIX_Z);
		Mockito.when(matrix.getGelManufacture()).thenReturn(MANUFACTURE + i);
		Mockito.when(matrix.getMwRangeH()).thenReturn(MW_RANGE_H + i);
		Mockito.when(matrix.getMwRangeL()).thenReturn(MW_RANGE_L + i);
		Mockito.when(matrix.getMwRangeType()).thenReturn(MW_RANGE_TYPE + i);
		Mockito.when(matrix.getMwRangeUnit()).thenReturn(MW_RANGE_UNIT + i);
		Mockito.when(matrix.getPhRangeH()).thenReturn(PH_RANGE_H + i);
		Mockito.when(matrix.getPhRangeL()).thenReturn(PH_RANGE_L + i);
		Mockito.when(matrix.getPhRangeType()).thenReturn(PH_RANGE_TYPE + i);
		Mockito.when(matrix.getPolymerizationMatrixBuffers()).thenReturn(MATRIX_POLYMERIZATION_BUFFERS);
		Mockito.when(matrix.getType()).thenReturn(MATRIX_TYPE + i);
		Mockito.when(matrix.getAdditionalMatrixSubstances()).thenReturn(MOCK_ADDITIONAL_MATRIX_SUBSTANCES);
		Mockito.when(matrix.getAdditionalMatrixBuffers()).thenReturn(MATRIX_ADD_BUFFERS);
		Mockito.when(matrix.getPolymerizationMatrixBuffers()).thenReturn(MATRIX_POL_BUFFERS);
		Set<SampleApplication> sampleApplication = new HashSet<SampleApplication>();
		sampleApplication.add(mockSampleApplication(0));
		Mockito.when(matrix.getSampleApplications()).thenReturn(sampleApplication);
		return matrix;
	}



	private static SampleApplication mockSampleApplication(int i) {
		SampleApplication sampleApplication = Mockito.mock(SampleApplication.class);
		Mockito.when(sampleApplication.getDescription()).thenReturn(SAMPLE_APPLICATION_DESCRIPTION + i);
		Mockito.when(sampleApplication.getName()).thenReturn(SAMPLE_APPLICATION_NAME + i);
		Set<Lane> lanes = new HashSet<Lane>();
		lanes.add(mockLane(0));
		lanes.add(mockLane(1));
		Mockito.when(sampleApplication.getLanes()).thenReturn(lanes);
		return sampleApplication;
	}


}
