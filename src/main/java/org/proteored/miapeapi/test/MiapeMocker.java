package org.proteored.miapeapi.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ge.BufferComponentName;
import org.proteored.miapeapi.cv.ge.ContactPositionGE;
import org.proteored.miapeapi.cv.ge.ImageAcquisitionSoftwareName;
import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.BufferComponent;
import org.proteored.miapeapi.interfaces.Contact;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.Image;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.Substance;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionSoftware;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;
import org.proteored.miapeapi.interfaces.gi.GIAdditionalInformation;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.spring.SpringHandler;

public class MiapeMocker {
	public static final ControlVocabularyManager cvManager = SpringHandler
			.getInstance().getCVManager();
	public static final String NEW_CONTACT_EMAIL = "ContactEmail"
			+ System.currentTimeMillis();
	public static final String NEW_PROJECT_NAME = "NEW_PROJECT_NAME"
			+ System.currentTimeMillis();
	public static final String CONTACT_POSITION_MS = ContactPositionMS
			.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String CONTACT_POSITION_GE = ContactPositionGE
			.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String CONTACT_TELEPHONE = "ContactTelephone";
	public static final String CONTACT_NAME = "ContactName";
	public static final String CONTACT_LOCALTY = "ContactLocalty";
	public static final String CONTACT_LAST_N_AME = "ContactLastNAme";
	public static final String CONTACT_INSTITUTION = "ContactInstitution";
	public static final String CONTACT_FAX = "ContactFax";
	public static final String CONTACT_EMAIL_GE = "NEWContactEmail_1234_GE";
	public static final String CONTACT_EMAIL_MS = "NEWContactEmail_1233_MS";
	public static final String CONTACT_DEPARTMENT = "ContactDepartment";
	public static final String CONTACT_CP = "ContactCP";
	public static final String CONTACT_COUNTRY = "ContactCountry";
	public static final String CONTACT_ADDRESS = "ContactAddress";

	public static final String ALGORITHM_VERSION = "AlgorithmVersion";
	public static final String ALGORITHM_URI = "AlgorithmUri";
	public static final String ALGORITHM_PARAMETERS = "AlgorithmParameters";
	public static final String ALGORITHM_MODEL = "AlgorithmModel";
	public static final String ALGORITHM_MANUFACTURER = "AlgorithmManufacturer";
	public static final String ALGORITHM_DESCRIPTION = "AlgorithmDescription";
	public static final String ALGORITHM_COMMENTS = "AlgorithmComments";
	public static final String ALGORITHM_C_ATALOG_NUMBER = "AlgorithmCAtalogNumber";
	public static final String ALGORITHM_NAME = "AlgorithmName";

	public static final Integer LABORATORY_ORDEr = Integer.valueOf(10);
	public static final String LABORATORY_URL = "LaboratoryUrl";
	public static final String LABORATORY_TELEPHONE = "LaboratoryTelephone";
	public static final String LABORATORY_STATUS = "A";
	public static final String LABORATORY_SERVICE_DESCRIPTION = "LaboratoryServiceDescription";
	public static final String LABORATORY_NIF = "LaboratoryNif";
	public static final String LABORATORY_ICON = "LaboratoryIcon";
	public static final String LABORATORY_EMAIL = "LaboratoryEmail";
	public static final String LABORATORY_CODE = "LaboratoryCode";
	public static final boolean LABORATORY_ASSOCIATED = true;
	public static final boolean LABORATORY_ADMIN_SEND = true;
	public static final String LABORATORY_ADMIN_EMAIL = "LaboratoryAdminEmail";
	public static final String LABORATORY_ADDRESS = "LaboratoryAddress";
	public static final String LABORATORY_NAME = "LaboratoryName";
	public static final Integer PROFILE_RANK = Integer.valueOf(8);
	public static final String PROFILE_DESCRIPTION = "ProfileDescription";
	public static final String PROFILE_NAME = "ProfileName";
	public static final String SCOPE_URL = "ScopeUrl";
	public static final String SCOPE_TELEPHONE = "ScopeTelephone";
	public static final String SCOPE_EMAIL = "ScopeEmail";
	public static final String SCOPE_CODE = "ScopeCode";
	public static final String SCOPE_ADDRESS = "ScopeAddress";
	public static final String SCOPE_NAME = "ScopeName";
	public static final String USER_WEB = "UserWeb";
	public static final String USERNAME = "testUser";
	public static final String PASSWORD = "test";

	public static final String NEW_USER_USER = "testUser"
			+ System.currentTimeMillis();
	public static final String TELEPHONE_NUMBER = "TelephoneNumber";
	public static final String USER_STATUS = "1";
	public static final String NODE_NAME = "NodeName";
	public static final String NODE_URL = "NodeUrl";
	public static final String NODE_TELEPHONE_NUMBER = "NodeTelephoneNumber";
	public static final Integer NODE_NUMBER = Integer.valueOf(26);
	public static final User NODE_MANAGER = mockUser();
	public static final Integer ID_SCOPE = Integer.valueOf(23);
	public static final String NODE_EMAIL = "nodeEmail";
	public static final String NODE_CODE = "NodeCode";
	public static final String NODE_ADDRESS = "NodeAddress";
	public static final String USER_MANAGER_ROLE = "UserManagerRole";
	public static final Integer USER_MANAGER = Integer.valueOf(48);
	public static final String USER_LAST_NAME = "UserLastName";
	public static final String USER_FAX_NUMBER = "USerFaxNumber";
	public static final String USER_EMAIL = "UserEmail";
	public static final Integer CONTACT_PERSON = Integer.valueOf(107);
	public static final String USER_ADDRESS = "UserAddress";
	public static final MiapeDate PROJECT_DATE = new MiapeDate(new Date());
	public static final String PROJECT_COMMENTS = "ProjectComments";
	public static final Boolean TEMPLATE = Boolean.FALSE;
	public static final String PRIDE_URL = "PrideUrl";
	public static final Date MODIFICATION_DATE = new Date();
	public static final MiapeDate DATE = new MiapeDate(new Date());
	public static final String VERSION1 = "Version1";

	public static final String MIAPE_NAME = "MiapeName";
	public static final String PROJECT_NAME = "projectTest";
	public static final String USER_NAME = "User Name";
	public static final String SOFTWARE_NAME = "SoftwareName";
	public static final String SOFTWARE_VERSION = "SoftwareVersion";
	public static final String SOFTWARE_URI = "SoftwareURI";
	public static final String SOFTWARE_PARAMETERS = "SoftwareParameters";
	public static final String SOFTWARE_PARAMETER_FILE = "SoftwareParameterFile";
	public static final String SOFTWARE_MODEL = "SoftwareModel";
	public static final String SOFTWARE_MANUFACTURER = "SoftwareManufacturer";
	public static final String SOFTWARE_DESCRIPTION = "SoftwareDescription";
	public static final String SOFTWARE_CUSTOMIZATIONS = "SoftwareCustomizations";
	public static final String SOFTWARE_COMMENTS = "SoftwareComments";
	public static final String SOFTWARE_CATALOG_NUMBER = "SoftwareCatalogNumber";

	public static final String EQUIPMENT_VERSION = "equipmentVersion";
	public static final String EQUIPMENT_URI = "EquipmentURI";
	public static final String EQUIPMENT_PARAMETERS = "EquipmentParameters";
	public static final String EQUIPMENT_MODEL = "DeviceModel";
	public static final String EQUIPMENT_MANUFACTURER = "EquipmentManufacturer";
	public static final String EQUIPMENT_DESCRIPTION = "EquipmentDescription";
	public static final String EQUIPMENT_COMMENTS = "EquipmentComments";
	public static final String EQUIPMENT_CATALOG_NUMBER = "EquipmentCatalogNumber";
	public static final String EQUIPMENT_NAME = "Applied Biosystems instrument model";

	public static final String VOLUME_UNIT = "VolumeUnit";
	public static final String VOLUME = "Volume";
	public static final String TYPE = "Type";
	public static final String MASS_UNIT = "MassUnit";
	public static final String MASS = "Mass";
	public static final String DESCRIPTION = "Description";
	public static final String CONCENTRATION_UNIT = "ConcentrationUnit";
	public static final String CONCENTRATION = "Concentration";
	public static final String SUBSTANCE_NAME = "SubstanceName";
	public static final String COMPONENT_NAME = BufferComponentName
			.getInstance(cvManager).getFirstCVTerm().getPreferredName();

	public static final String BUFFER_TYPE = "BufferType";
	public static final String BUFFER_DESCRIPTION = "BufferDescription";
	public static final String BUFFER_NAME = "BufferName";

	public static final String RESOLUTION_UNIT = "ResolutionUnit";
	public static final String RESOLUTION = "Resolution";
	public static final String ORIENTATION = "Orientation";
	public static final String IMAGE_NAME = "ImageName";
	public static final String LOCATION = "Location";
	public static final String FORMAT = "Format";
	public static final String DIMENSION_Y = "DimensionY";
	public static final String DIMENSION_X = "DimensionX";
	public static final String DIMENSION_UNIT = "DimensionUnit";
	public static final String BIT_DEPTH = "BitDepth";
	public static final String IMAGE_ACQUISITION_SOFTWARE_NAME = ImageAcquisitionSoftwareName
			.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String IMAGE_ANALYSIS_SOFTWARE_NAME = org.proteored.miapeapi.cv.gi.ImageAnalysisSoftwareName
			.getInstance(cvManager).getFirstCVTerm().getPreferredName();
	public static final String CUSTOMIZATIONS = "Customizations";
	public static final String IMAGE_TYPE = "ImageType";
	private static final User TESTUSER = mockTestUser();

	public static void mockMiapeDocument(MiapeDocument miape) {
		Project project = mockProject();
		User owner = mockOldUser();

		String name = MIAPE_NAME;

		Mockito.when(miape.getName()).thenReturn(name);
		Mockito.when(miape.getProject()).thenReturn(project);
		Mockito.when(miape.getOwner()).thenReturn(owner);
		Mockito.when(miape.getVersion()).thenReturn(VERSION1);
		Mockito.when(miape.getDate()).thenReturn(DATE);
		Mockito.when(miape.getModificationDate()).thenReturn(MODIFICATION_DATE);
		Mockito.when(miape.getAttachedFileLocation()).thenReturn(PRIDE_URL);
		Mockito.when(miape.getTemplate()).thenReturn(TEMPLATE);
	}

	public static Contact mockNewContact() {
		MSContact contact = mockMSContact();
		Mockito.when(contact.getEmail()).thenReturn(NEW_CONTACT_EMAIL);

		return contact;
	}

	public static User mockOldUser() {
		User user = mockUser();
		Mockito.when(user.getUserName()).thenReturn(USERNAME);
		Mockito.when(user.getPassword()).thenReturn(PASSWORD);
		return user;
	}

	public static User mockNewUser() {
		User user = mockUser();
		Mockito.when(user.getUserName()).thenReturn(NEW_USER_USER);
		return user;
	}

	public static User mockUser() {
		User user = Mockito.mock(User.class);
		Mockito.when(user.getName()).thenReturn(USER_NAME);
		Mockito.when(user.getAddress()).thenReturn(USER_ADDRESS);
		Mockito.when(user.getContactPerson()).thenReturn(CONTACT_PERSON);
		Mockito.when(user.getEmail()).thenReturn(USER_EMAIL);
		Mockito.when(user.getFaxNumber()).thenReturn(USER_FAX_NUMBER);
		Mockito.when(user.getLastName()).thenReturn(USER_LAST_NAME);
		Mockito.when(user.getManager()).thenReturn(USER_MANAGER);
		Mockito.when(user.getManagerRole()).thenReturn(USER_MANAGER_ROLE);
		Mockito.when(user.getStatus()).thenReturn(USER_STATUS);
		Mockito.when(user.getTelephoneNumber()).thenReturn(TELEPHONE_NUMBER);
		Mockito.when(user.getWeb()).thenReturn(USER_WEB);
		Mockito.when(user.getUserName()).thenReturn(USERNAME);
		Mockito.when(user.getPassword()).thenReturn(PASSWORD);

		return user;
	}

	public static User mockTestUser() {
		User user = Mockito.mock(User.class);
		Mockito.when(user.getName()).thenReturn(USER_NAME);
		Mockito.when(user.getAddress()).thenReturn(USER_ADDRESS);
		Mockito.when(user.getContactPerson()).thenReturn(CONTACT_PERSON);
		Mockito.when(user.getEmail()).thenReturn(USER_EMAIL);
		Mockito.when(user.getFaxNumber()).thenReturn(USER_FAX_NUMBER);
		Mockito.when(user.getLastName()).thenReturn(USER_LAST_NAME);
		Mockito.when(user.getManager()).thenReturn(USER_MANAGER);
		Mockito.when(user.getManagerRole()).thenReturn(USER_MANAGER_ROLE);
		Mockito.when(user.getStatus()).thenReturn(USER_STATUS);
		Mockito.when(user.getTelephoneNumber()).thenReturn(TELEPHONE_NUMBER);
		Mockito.when(user.getWeb()).thenReturn(USER_WEB);
		Mockito.when(user.getUserName()).thenReturn(USERNAME);
		Mockito.when(user.getPassword()).thenReturn(PASSWORD);

		return user;
	}

	public static Project mockProject() {
		Project project = Mockito.mock(Project.class);
		Mockito.when(project.getName()).thenReturn(PROJECT_NAME);
		Mockito.when(project.getComments()).thenReturn(PROJECT_COMMENTS);
		Mockito.when(project.getDate()).thenReturn(PROJECT_DATE);
		Mockito.when(project.getOwner()).thenReturn(NODE_MANAGER);
		return project;
	}

	public static Project mockNewProject() {
		Project project = mockProject();
		Mockito.when(project.getName()).thenReturn(NEW_PROJECT_NAME);
		Mockito.when(project.getOwner()).thenReturn(TESTUSER);
		return project;
	}

	public static Algorithm mockAlgorithm(int i) {
		Algorithm algorithm = Mockito.mock(Algorithm.class);
		addAlgorithm(i, algorithm);
		return algorithm;
	}

	public static PostProcessingMethod mockValidationAlgorithm(int i) {
		PostProcessingMethod algorithm = Mockito
				.mock(PostProcessingMethod.class);
		addAlgorithm(i, algorithm);
		return algorithm;
	}

	private static void addAlgorithm(int i, Algorithm algorithm) {
		Mockito.when(algorithm.getName()).thenReturn(ALGORITHM_NAME + i);
		Mockito.when(algorithm.getCatalogNumber()).thenReturn(
				ALGORITHM_C_ATALOG_NUMBER + i);
		Mockito.when(algorithm.getComments())
				.thenReturn(ALGORITHM_COMMENTS + i);
		Mockito.when(algorithm.getDescription()).thenReturn(
				ALGORITHM_DESCRIPTION + i);
		Mockito.when(algorithm.getManufacturer()).thenReturn(
				ALGORITHM_MANUFACTURER + i);
		Mockito.when(algorithm.getModel()).thenReturn(ALGORITHM_MODEL + i);
		Mockito.when(algorithm.getParameters()).thenReturn(
				ALGORITHM_PARAMETERS + i);
		Mockito.when(algorithm.getURI()).thenReturn(ALGORITHM_URI + i);
		Mockito.when(algorithm.getVersion()).thenReturn(ALGORITHM_VERSION + i);
	}

	public static Software mockSoftware(int i) {
		Software software = Mockito.mock(Software.class);
		addSoftware(i, software);

		return software;
	}

	public static ImageAnalysisSoftware mockImageAnalysisSoftware(int i) {
		ImageAnalysisSoftware software = Mockito
				.mock(ImageAnalysisSoftware.class);
		Mockito.when(software.getName()).thenReturn(
				IMAGE_ANALYSIS_SOFTWARE_NAME);
		addSoftware(i, software);

		return software;
	}

	public static ImageAcquisitionSoftware mockImageAcquisitionSoftware(int i) {
		ImageAcquisitionSoftware software = Mockito
				.mock(ImageAcquisitionSoftware.class);
		Mockito.when(software.getName()).thenReturn(
				IMAGE_ACQUISITION_SOFTWARE_NAME);
		addSoftware(i, software);

		return software;
	}

	private static void addSoftware(int i, Software software) {
		Mockito.when(software.getName()).thenReturn(SOFTWARE_NAME + i);
		Mockito.when(software.getCatalogNumber()).thenReturn(
				SOFTWARE_CATALOG_NUMBER + i);
		Mockito.when(software.getComments()).thenReturn(SOFTWARE_COMMENTS + i);
		Mockito.when(software.getCustomizations()).thenReturn(
				SOFTWARE_CUSTOMIZATIONS + i);
		Mockito.when(software.getDescription()).thenReturn(
				SOFTWARE_DESCRIPTION + i);
		Mockito.when(software.getManufacturer()).thenReturn(
				SOFTWARE_MANUFACTURER + i);
		Mockito.when(software.getModel()).thenReturn(SOFTWARE_MODEL + i);
		Mockito.when(software.getParameters()).thenReturn(
				SOFTWARE_PARAMETERS + i);
		Mockito.when(software.getURI()).thenReturn(SOFTWARE_URI + i);
		Mockito.when(software.getVersion()).thenReturn(SOFTWARE_VERSION + i);
	}

	public static Equipment mockEquipment(int i) {
		Equipment equipment = Mockito.mock(Equipment.class);
		addEquipmentData(i, equipment);
		return equipment;
	}

	public static Spectrometer mockSpectrometer(int i) {
		Spectrometer spectrometer = Mockito.mock(Spectrometer.class);
		Mockito.when(spectrometer.getCustomizations()).thenReturn(
				CUSTOMIZATIONS + i);
		Mockito.when(spectrometer.getName()).thenReturn(EQUIPMENT_NAME + i);
		addEquipmentData(i, spectrometer);
		return spectrometer;
	}

	public static void addEquipmentData(int i, Equipment equipment) {
		Mockito.when(equipment.getName()).thenReturn(EQUIPMENT_NAME + i);
		Mockito.when(equipment.getCatalogNumber()).thenReturn(
				EQUIPMENT_CATALOG_NUMBER + i);
		Mockito.when(equipment.getComments())
				.thenReturn(EQUIPMENT_COMMENTS + i);
		Mockito.when(equipment.getDescription()).thenReturn(
				EQUIPMENT_DESCRIPTION + i);
		Mockito.when(equipment.getManufacturer()).thenReturn(
				EQUIPMENT_MANUFACTURER + i);
		Mockito.when(equipment.getModel()).thenReturn(EQUIPMENT_MODEL + i);
		Mockito.when(equipment.getParameters()).thenReturn(
				EQUIPMENT_PARAMETERS + i);
		Mockito.when(equipment.getUri()).thenReturn(EQUIPMENT_URI + i);
		Mockito.when(equipment.getVersion()).thenReturn(EQUIPMENT_VERSION + i);
	}

	public static Set<Substance> mockSubstances(int i) {
		Set<Substance> substances = new HashSet<Substance>();
		Substance substance = MiapeMocker.mockSubstance(i);
		substances.add(substance);
		return substances;
	}

	public static Set<Agent> mockAdditionalMatrixSubstances(int i) {
		Set<Agent> substances = new HashSet<Agent>();
		Agent substance = MiapeMocker.mockAgent(i);
		substances.add(substance);
		return substances;
	}

	public static Agent mockAgent(int i) {
		Agent substance = Mockito.mock(Agent.class);
		Mockito.when(substance.getName()).thenReturn(SUBSTANCE_NAME + i);
		Mockito.when(substance.getConcentration())
				.thenReturn(CONCENTRATION + i);
		Mockito.when(substance.getConcentrationUnit()).thenReturn(
				CONCENTRATION_UNIT + i);
		Mockito.when(substance.getDescription()).thenReturn(DESCRIPTION + i);
		Mockito.when(substance.getMass()).thenReturn(MASS + i);
		Mockito.when(substance.getMassUnit()).thenReturn(MASS_UNIT + i);
		Mockito.when(substance.getType()).thenReturn(TYPE + i);
		Mockito.when(substance.getVolume()).thenReturn(VOLUME + i);
		Mockito.when(substance.getVolumeUnit()).thenReturn(VOLUME_UNIT + i);
		return substance;
	}

	public static Substance mockSubstance(int i) {
		Substance substance = Mockito.mock(Substance.class);
		Mockito.when(substance.getName()).thenReturn(SUBSTANCE_NAME + i);
		Mockito.when(substance.getConcentration())
				.thenReturn(CONCENTRATION + i);
		Mockito.when(substance.getConcentrationUnit()).thenReturn(
				CONCENTRATION_UNIT + i);
		Mockito.when(substance.getDescription()).thenReturn(DESCRIPTION + i);
		Mockito.when(substance.getMass()).thenReturn(MASS + i);
		Mockito.when(substance.getMassUnit()).thenReturn(MASS_UNIT + i);
		Mockito.when(substance.getType()).thenReturn(TYPE + i);
		Mockito.when(substance.getVolume()).thenReturn(VOLUME + i);
		Mockito.when(substance.getVolumeUnit()).thenReturn(VOLUME_UNIT + i);
		return substance;
	}

	public static BufferComponent mockBufferComponent(int i) {
		BufferComponent substance = Mockito.mock(BufferComponent.class);
		Mockito.when(substance.getName()).thenReturn(COMPONENT_NAME);
		Mockito.when(substance.getConcentration())
				.thenReturn(CONCENTRATION + i);
		Mockito.when(substance.getConcentrationUnit()).thenReturn(
				CONCENTRATION_UNIT + i);
		Mockito.when(substance.getDescription()).thenReturn(DESCRIPTION + i);
		Mockito.when(substance.getMass()).thenReturn(MASS + i);
		Mockito.when(substance.getMassUnit()).thenReturn(MASS_UNIT + i);
		Mockito.when(substance.getType()).thenReturn(TYPE + i);
		Mockito.when(substance.getVolume()).thenReturn(VOLUME + i);
		Mockito.when(substance.getVolumeUnit()).thenReturn(VOLUME_UNIT + i);
		return substance;
	}

	public static DirectDetectionAgent mockDirectDetectionAgent(int i) {
		DirectDetectionAgent agent = Mockito.mock(DirectDetectionAgent.class);
		Mockito.when(agent.getName()).thenReturn(SUBSTANCE_NAME + i);
		Mockito.when(agent.getConcentration()).thenReturn(CONCENTRATION + i);
		Mockito.when(agent.getConcentrationUnit()).thenReturn(
				CONCENTRATION_UNIT + i);
		Mockito.when(agent.getDescription()).thenReturn(DESCRIPTION + i);
		Mockito.when(agent.getMass()).thenReturn(MASS + i);
		Mockito.when(agent.getMassUnit()).thenReturn(MASS_UNIT + i);
		Mockito.when(agent.getType()).thenReturn(TYPE + i);
		Mockito.when(agent.getVolume()).thenReturn(VOLUME + i);
		Mockito.when(agent.getVolumeUnit()).thenReturn(VOLUME_UNIT + i);
		return agent;
	}

	public static IndirectDetectionAgent mockIndirectDetectionAgent(int i) {
		IndirectDetectionAgent agent = Mockito
				.mock(IndirectDetectionAgent.class);
		Mockito.when(agent.getName()).thenReturn(SUBSTANCE_NAME + i);
		Mockito.when(agent.getConcentration()).thenReturn(CONCENTRATION + i);
		Mockito.when(agent.getConcentrationUnit()).thenReturn(
				CONCENTRATION_UNIT + i);
		Mockito.when(agent.getDescription()).thenReturn(DESCRIPTION + i);
		Mockito.when(agent.getMass()).thenReturn(MASS + i);
		Mockito.when(agent.getMassUnit()).thenReturn(MASS_UNIT + i);
		Mockito.when(agent.getType()).thenReturn(TYPE + i);
		Mockito.when(agent.getVolume()).thenReturn(VOLUME + i);
		Mockito.when(agent.getVolumeUnit()).thenReturn(VOLUME_UNIT + i);
		return agent;
	}

	public static Set<Buffer> mockBuffers(int i) {
		Set<Buffer> buffers = new HashSet<Buffer>();
		buffers.add(mockBuffer(i));
		return buffers;
	}

	public static Buffer mockBuffer(int i) {
		Buffer buffer = Mockito.mock(Buffer.class);
		Mockito.when(buffer.getName()).thenReturn(BUFFER_NAME + i);
		Mockito.when(buffer.getDescription())
				.thenReturn(BUFFER_DESCRIPTION + i);
		Mockito.when(buffer.getType()).thenReturn(BUFFER_TYPE + i);
		Set<BufferComponent> substances = new HashSet<BufferComponent>();
		substances.add(mockBufferComponent(88));
		Mockito.when(buffer.getComponents()).thenReturn(substances);

		return buffer;
	}

	public static void mockImage(int i, Image image) {
		Mockito.when(image.getBitDepth()).thenReturn(BIT_DEPTH + i);
		Mockito.when(image.getDimensionUnit()).thenReturn(DIMENSION_UNIT + i);
		Mockito.when(image.getDimensionX()).thenReturn(DIMENSION_X + i);
		Mockito.when(image.getDimensionY()).thenReturn(DIMENSION_Y + i);
		Mockito.when(image.getFormat()).thenReturn(FORMAT + i);
		Mockito.when(image.getLocation()).thenReturn(LOCATION + i);
		Mockito.when(image.getName()).thenReturn(IMAGE_NAME + i);
		Mockito.when(image.getOrientation()).thenReturn(ORIENTATION + i);
		Mockito.when(image.getResolution()).thenReturn(RESOLUTION + i);
		Mockito.when(image.getResolutionUnit()).thenReturn(RESOLUTION_UNIT + i);
	}

	public static MSContact mockMSContact() {
		MSContact contact = Mockito.mock(MSContact.class);
		Mockito.when(contact.getAddress()).thenReturn(
				MiapeMocker.CONTACT_ADDRESS);
		Mockito.when(contact.getCountry()).thenReturn(
				MiapeMocker.CONTACT_COUNTRY);
		Mockito.when(contact.getCP()).thenReturn(MiapeMocker.CONTACT_CP);
		Mockito.when(contact.getDepartment()).thenReturn(
				MiapeMocker.CONTACT_DEPARTMENT);
		Mockito.when(contact.getEmail()).thenReturn(
				MiapeMocker.CONTACT_EMAIL_MS);
		Mockito.when(contact.getFax()).thenReturn(MiapeMocker.CONTACT_FAX);
		// Mockito.when(contact.getInstitution()).thenReturn(MiapeMocker.CONTACT_INSTITUTION);
		Mockito.when(contact.getLastName()).thenReturn(
				MiapeMocker.CONTACT_LAST_N_AME);
		Mockito.when(contact.getLocality()).thenReturn(
				MiapeMocker.CONTACT_LOCALTY);
		Mockito.when(contact.getName()).thenReturn(MiapeMocker.CONTACT_NAME);
		Mockito.when(contact.getTelephone()).thenReturn(
				MiapeMocker.CONTACT_TELEPHONE);
		Mockito.when(contact.getPosition()).thenReturn(
				MiapeMocker.CONTACT_POSITION_MS);
		return contact;
	}

	public static GEContact mockGEContact() {
		GEContact contact = Mockito.mock(GEContact.class);
		Mockito.when(contact.getAddress()).thenReturn(
				MiapeMocker.CONTACT_ADDRESS);
		Mockito.when(contact.getCountry()).thenReturn(
				MiapeMocker.CONTACT_COUNTRY);
		Mockito.when(contact.getCP()).thenReturn(MiapeMocker.CONTACT_CP);
		Mockito.when(contact.getDepartment()).thenReturn(
				MiapeMocker.CONTACT_DEPARTMENT);
		Mockito.when(contact.getEmail()).thenReturn(
				MiapeMocker.CONTACT_EMAIL_GE);
		Mockito.when(contact.getFax()).thenReturn(MiapeMocker.CONTACT_FAX);
		Mockito.when(contact.getInstitution()).thenReturn(
				MiapeMocker.CONTACT_INSTITUTION);
		Mockito.when(contact.getLastName()).thenReturn(
				MiapeMocker.CONTACT_LAST_N_AME);
		Mockito.when(contact.getLocality()).thenReturn(
				MiapeMocker.CONTACT_LOCALTY);
		Mockito.when(contact.getName()).thenReturn(MiapeMocker.CONTACT_NAME);
		Mockito.when(contact.getTelephone()).thenReturn(
				MiapeMocker.CONTACT_TELEPHONE);
		Mockito.when(contact.getPosition()).thenReturn(
				MiapeMocker.CONTACT_POSITION_GE);

		return contact;
	}

	public static GIAdditionalInformation mockGIAdditionalInformation(int i) {
		GIAdditionalInformation hibAdditionalInformation = Mockito
				.mock(GIAdditionalInformation.class);
		Mockito.when(hibAdditionalInformation.getName()).thenReturn(
				MiapeGiMocker.ADDINFO_NAME + i);
		Mockito.when(hibAdditionalInformation.getValue()).thenReturn(
				MiapeGiMocker.ADDINFO_VALUE + i);
		return hibAdditionalInformation;
	}

	public static MSAdditionalInformation mockMSAdditionalInformation(int i) {
		MSAdditionalInformation hibAdditionalInformation = Mockito
				.mock(MSAdditionalInformation.class);
		Mockito.when(hibAdditionalInformation.getName()).thenReturn(
				MiapeMsMocker.ADDINFO_NAME + i);
		Mockito.when(hibAdditionalInformation.getValue()).thenReturn(
				MiapeMsMocker.ADDINFO_VALUE + i);
		return hibAdditionalInformation;
	}

	public static ResultingData mockResultingData(int i) {
		ResultingData resultingData = Mockito.mock(ResultingData.class);
		Mockito.when(resultingData.getName()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_NAME);
		Mockito.when(resultingData.getAdditionalUri()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_ADDURI);
		Mockito.when(resultingData.getDataFileType()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_RAWFILETYPE);
		Mockito.when(resultingData.getDataFileUri()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_RAWFILEURI);
		Mockito.when(resultingData.getSRMDescriptor()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_SRM_DESCRIPTOR);
		Mockito.when(resultingData.getSRMType()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_SRM_TYPE);
		Mockito.when(resultingData.getSRMUri()).thenReturn(
				MiapeMsMocker.RESULTING_DATA_SRM_URI);

		// Mockito.when(resultingData.getSpectrumDescriptions()).thenReturn(MiapeMsMocker.SPECTRUM_DESCRIPTIONS);

		return resultingData;

	}
}
