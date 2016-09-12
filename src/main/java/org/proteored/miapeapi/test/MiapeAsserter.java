package org.proteored.miapeapi.test;

import java.util.Set;

import org.junit.Assert;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.BufferComponent;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.Image;
import org.proteored.miapeapi.interfaces.Laboratory;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.Node;
import org.proteored.miapeapi.interfaces.Profile;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Scope;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.Substance;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionSoftware;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;

public class MiapeAsserter {

	public static void assertOldUser(User user) {
		Assert.assertEquals(MiapeMocker.USER_NAME, user.getName());
		Assert.assertEquals(MiapeMocker.USER_ADDRESS, user.getAddress());
		Assert.assertEquals(Integer.valueOf(0), user.getContactPerson());
		Assert.assertEquals(MiapeMocker.USER_EMAIL, user.getEmail());
		Assert.assertEquals(MiapeMocker.USER_FAX_NUMBER, user.getFaxNumber());

		Assert.assertEquals(MiapeMocker.USER_LAST_NAME, user.getLastName());
		Assert.assertEquals(Integer.valueOf(0), user.getManager());
		Assert.assertEquals(MiapeMocker.USER_MANAGER_ROLE, user.getManagerRole());
		Assert.assertEquals(MiapeMocker.USER_STATUS, user.getStatus());
		Assert.assertEquals(MiapeMocker.TELEPHONE_NUMBER, user.getTelephoneNumber());
		Assert.assertEquals(MiapeMocker.USERNAME, user.getUserName());
		Assert.assertEquals(MiapeMocker.USER_WEB, user.getWeb());

	}

	public static void assertContactGE(GEContact contact) {
		Assert.assertEquals(MiapeMocker.CONTACT_ADDRESS, contact.getAddress());
		Assert.assertEquals(MiapeMocker.CONTACT_COUNTRY, contact.getCountry());
		Assert.assertEquals(MiapeMocker.CONTACT_CP, contact.getCP());
		Assert.assertEquals(MiapeMocker.CONTACT_DEPARTMENT, contact.getDepartment());
		Assert.assertEquals(MiapeMocker.CONTACT_EMAIL_GE, contact.getEmail());
		Assert.assertEquals(MiapeMocker.CONTACT_FAX, contact.getFax());
		Assert.assertEquals(MiapeMocker.CONTACT_INSTITUTION, contact.getInstitution());
		Assert.assertEquals(MiapeMocker.CONTACT_LAST_N_AME, contact.getLastName());
		Assert.assertEquals(MiapeMocker.CONTACT_LOCALTY, contact.getLocality());
		Assert.assertEquals(MiapeMocker.CONTACT_NAME, contact.getName());
		Assert.assertEquals(MiapeMocker.CONTACT_TELEPHONE, contact.getTelephone());
		Assert.assertEquals(MiapeMocker.CONTACT_POSITION_GE, contact.getPosition());

	}

	public static void assertContactMS(MSContact contact) {
		Assert.assertEquals(MiapeMocker.CONTACT_ADDRESS, contact.getAddress());
		Assert.assertEquals(MiapeMocker.CONTACT_COUNTRY, contact.getCountry());
		Assert.assertEquals(MiapeMocker.CONTACT_CP, contact.getCP());
		Assert.assertEquals(MiapeMocker.CONTACT_DEPARTMENT, contact.getDepartment());
		Assert.assertEquals(MiapeMocker.CONTACT_EMAIL_MS, contact.getEmail());
		Assert.assertEquals(MiapeMocker.CONTACT_FAX, contact.getFax());
		Assert.assertEquals(MiapeMocker.CONTACT_INSTITUTION, contact.getInstitution());
		Assert.assertEquals(MiapeMocker.CONTACT_LAST_N_AME, contact.getLastName());
		Assert.assertEquals(MiapeMocker.CONTACT_LOCALTY, contact.getLocality());
		Assert.assertEquals(MiapeMocker.CONTACT_NAME, contact.getName());
		Assert.assertEquals(MiapeMocker.CONTACT_TELEPHONE, contact.getTelephone());
		Assert.assertEquals(MiapeMocker.CONTACT_POSITION_MS, contact.getPosition());

	}

	public static void assertScope(Scope scope) {
		Assert.assertEquals(MiapeMocker.SCOPE_ADDRESS, scope.getAddress());
		Assert.assertEquals(MiapeMocker.SCOPE_CODE, scope.getCode());
		Assert.assertEquals(MiapeMocker.SCOPE_EMAIL, scope.getEmail());
		Assert.assertEquals(MiapeMocker.SCOPE_NAME, scope.getName());
		Assert.assertEquals(MiapeMocker.SCOPE_TELEPHONE, scope.getTelephone());
		Assert.assertEquals(MiapeMocker.SCOPE_URL, scope.getUrl());

	}

	public static void assertNode(Node node) {
		Assert.assertEquals(MiapeMocker.NODE_ADDRESS, node.getAddress());
		Assert.assertEquals(MiapeMocker.NODE_CODE, node.getCode());
		Assert.assertEquals(MiapeMocker.NODE_EMAIL, node.getEmail());
		// TODO Problem with the initialization.
		// Assert.assertEquals(HibernateMocker.NODE_MANAGER.getName(),
		// node.getManager().getName());
		Assert.assertEquals(MiapeMocker.NODE_NAME, node.getName());
		Assert.assertEquals(MiapeMocker.NODE_NUMBER, node.getNumber());
		Assert.assertEquals(MiapeMocker.NODE_TELEPHONE_NUMBER, node.getTelephoneNumber());
		Assert.assertEquals(MiapeMocker.NODE_URL, node.getUrl());

	}

	public static void assertProfile(Profile profile) {
		Assert.assertEquals(MiapeMocker.PROFILE_NAME, profile.getName());
		Assert.assertEquals(MiapeMocker.PROFILE_DESCRIPTION, profile.getDescription());
		Assert.assertEquals(MiapeMocker.PROFILE_RANK, profile.getRank());

	}

	public static void assertProject(Project project) {
		Assert.assertEquals(MiapeMocker.PROJECT_NAME, project.getName());
		Assert.assertEquals(MiapeMocker.PROJECT_COMMENTS, project.getComments());
		// Assert.assertEquals(HibernateMocker.PROJECT_DATE, project.getDate());
	}

	public static void assertLaboratory(Laboratory laboratory) {
		Assert.assertEquals(MiapeMocker.LABORATORY_NAME, laboratory.getName());
		Assert.assertEquals(MiapeMocker.LABORATORY_ADDRESS, laboratory.getAddress());
		Assert.assertEquals(MiapeMocker.LABORATORY_ADMIN_EMAIL, laboratory.getAdminEmail());

		Assert.assertEquals(MiapeMocker.LABORATORY_ADMIN_SEND, laboratory.getAdminSendFlag());
		Assert.assertEquals(MiapeMocker.LABORATORY_ASSOCIATED, laboratory.getAssociated());
		Assert.assertEquals(MiapeMocker.LABORATORY_CODE, laboratory.getCode());
		Assert.assertEquals(MiapeMocker.LABORATORY_EMAIL, laboratory.getEmail());
		Assert.assertEquals(MiapeMocker.LABORATORY_ICON, laboratory.getIcon());
		Assert.assertEquals(MiapeMocker.LABORATORY_NIF, laboratory.getNif());
		Assert.assertEquals(MiapeMocker.LABORATORY_SERVICE_DESCRIPTION,
				laboratory.getServiceDescription());
		Assert.assertEquals(MiapeMocker.LABORATORY_STATUS, laboratory.getStatus());
		Assert.assertEquals(MiapeMocker.LABORATORY_TELEPHONE, laboratory.getTelephone());
		Assert.assertEquals(MiapeMocker.LABORATORY_URL, laboratory.getUrl());

	}

	public static void assertDocument(MiapeDocument document) {
		Assert.assertEquals(MiapeMocker.MIAPE_NAME, document.getName());
		Assert.assertEquals(MiapeMocker.VERSION1, document.getVersion());
		// TODO why difference of millisconds?
		// Assert.assertEquals(HibernateMocker.DATE.getTime(),
		// document.getDate().getTime());
		// Assert.assertEquals(HibernateMocker.MODIFICATION_DATE, document
		// .getModificationDate());
		Assert.assertEquals(MiapeMocker.PRIDE_URL, document.getAttachedFileLocation());

	}

	public static void assertImageAnalysisSoftwares(Set<ImageAnalysisSoftware> set, int i) {
		ImageAnalysisSoftware software = set.iterator().next();
		assertImageAnalysisSoftware(i, software);
	}

	private static void assertImageAnalysisSoftware(int i, ImageAnalysisSoftware software) {
		Assert.assertEquals(MiapeMocker.SOFTWARE_NAME + i, software.getName());
		Assert.assertEquals(MiapeMocker.SOFTWARE_CATALOG_NUMBER + i, software.getCatalogNumber());
		Assert.assertEquals(MiapeMocker.SOFTWARE_COMMENTS + i, software.getComments());
		Assert.assertEquals(MiapeMocker.SOFTWARE_CUSTOMIZATIONS + i, software.getCustomizations());
		Assert.assertEquals(MiapeMocker.SOFTWARE_DESCRIPTION + i, software.getDescription());
		Assert.assertEquals(MiapeMocker.SOFTWARE_MANUFACTURER + i, software.getManufacturer());
		Assert.assertEquals(MiapeMocker.SOFTWARE_MODEL + i, software.getModel());
		Assert.assertEquals(MiapeMocker.SOFTWARE_PARAMETERS + i, software.getParameters());
		Assert.assertEquals(MiapeMocker.SOFTWARE_URI + i, software.getURI());
		Assert.assertEquals(MiapeMocker.SOFTWARE_VERSION + i, software.getVersion());
	}

	public static void assertSoftwares(Set<Software> set, int i) {
		Software software = set.iterator().next();
		assertSoftware(i, software);
	}

	public static void assertImageAcquisitionSoftwares(Set<ImageAcquisitionSoftware> softwares,
			int i) {
		ImageAcquisitionSoftware software = softwares.iterator().next();
		assertSoftware(i, software);
	}

	private static void assertSoftware(int i, Software software) {
		Assert.assertEquals(MiapeMocker.SOFTWARE_NAME + i, software.getName());
		Assert.assertEquals(MiapeMocker.SOFTWARE_CATALOG_NUMBER + i, software.getCatalogNumber());
		Assert.assertEquals(MiapeMocker.SOFTWARE_COMMENTS + i, software.getComments());
		Assert.assertEquals(MiapeMocker.SOFTWARE_CUSTOMIZATIONS + i, software.getCustomizations());
		Assert.assertEquals(MiapeMocker.SOFTWARE_DESCRIPTION + i, software.getDescription());
		Assert.assertEquals(MiapeMocker.SOFTWARE_MANUFACTURER + i, software.getManufacturer());
		Assert.assertEquals(MiapeMocker.SOFTWARE_MODEL + i, software.getModel());
		Assert.assertEquals(MiapeMocker.SOFTWARE_PARAMETERS + i, software.getParameters());
		Assert.assertEquals(MiapeMocker.SOFTWARE_URI + i, software.getURI());
		Assert.assertEquals(MiapeMocker.SOFTWARE_VERSION + i, software.getVersion());
	}

	public static void assertAlgorithms(Set<Algorithm> algorithms, int count) {
		Algorithm algorithm = algorithms.iterator().next();
		assertAlgorithm(count, algorithm);
	}

	public static void assertValidationAlgorithms(Set<PostProcessingMethod> algorithms, int count) {
		PostProcessingMethod algorithm = algorithms.iterator().next();
		assertAlgorithm(count, algorithm);
	}

	public static void assertAlgorithm(int count, Algorithm algorithm) {
		Assert.assertEquals(MiapeMocker.ALGORITHM_C_ATALOG_NUMBER + count,
				algorithm.getCatalogNumber());
		Assert.assertEquals(MiapeMocker.ALGORITHM_COMMENTS + count, algorithm.getComments());
		Assert.assertEquals(MiapeMocker.ALGORITHM_DESCRIPTION + count, algorithm.getDescription());
		Assert.assertEquals(MiapeMocker.ALGORITHM_MANUFACTURER + count, algorithm.getManufacturer());
		Assert.assertEquals(MiapeMocker.ALGORITHM_MODEL + count, algorithm.getModel());
		Assert.assertEquals(MiapeMocker.ALGORITHM_NAME + count, algorithm.getName());
		Assert.assertEquals(MiapeMocker.ALGORITHM_PARAMETERS + count, algorithm.getParameters());
		Assert.assertEquals(MiapeMocker.ALGORITHM_URI + count, algorithm.getURI());
		Assert.assertEquals(MiapeMocker.ALGORITHM_VERSION + count, algorithm.getVersion());
	}

	public static void assertEquipments(Set<Equipment> equipments, int i) {
		Equipment equipment = equipments.iterator().next();
		assertEquipment(i, equipment);
	}

	public static void assertEquipment(int i, Equipment spectrometer) {
		Assert.assertEquals(MiapeMocker.EQUIPMENT_CATALOG_NUMBER + i,
				spectrometer.getCatalogNumber());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_NAME + i, spectrometer.getName());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_COMMENTS + i, spectrometer.getComments());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_DESCRIPTION + i, spectrometer.getDescription());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_MANUFACTURER + i, spectrometer.getManufacturer());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_MODEL + i, spectrometer.getModel());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_PARAMETERS + i, spectrometer.getParameters());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_URI + i, spectrometer.getUri());
		Assert.assertEquals(MiapeMocker.EQUIPMENT_VERSION + i, spectrometer.getVersion());
	}

	public static void assertSubstances(Set<Substance> substances, int i) {
		Substance substance = substances.iterator().next();
		Assert.assertEquals(MiapeMocker.SUBSTANCE_NAME + i, substance.getName());
		Assert.assertEquals(MiapeMocker.CONCENTRATION + i, substance.getConcentration());
		Assert.assertEquals(MiapeMocker.CONCENTRATION_UNIT + i, substance.getConcentrationUnit());
		Assert.assertEquals(MiapeMocker.DESCRIPTION + i, substance.getDescription());
		Assert.assertEquals(MiapeMocker.MASS + i, substance.getMass());
		Assert.assertEquals(MiapeMocker.MASS_UNIT + i, substance.getMassUnit());
		Assert.assertEquals(MiapeMocker.TYPE + i, substance.getType());
		Assert.assertEquals(MiapeMocker.VOLUME + i, substance.getVolume());
		Assert.assertEquals(MiapeMocker.VOLUME_UNIT + i, substance.getVolumeUnit());

	}

	public static void assertAgents(Set<Agent> substances, int i) {
		Agent substance = substances.iterator().next();
		Assert.assertEquals(MiapeMocker.SUBSTANCE_NAME + i, substance.getName());
		Assert.assertEquals(MiapeMocker.CONCENTRATION + i, substance.getConcentration());
		Assert.assertEquals(MiapeMocker.CONCENTRATION_UNIT + i, substance.getConcentrationUnit());
		Assert.assertEquals(MiapeMocker.DESCRIPTION + i, substance.getDescription());
		Assert.assertEquals(MiapeMocker.MASS + i, substance.getMass());
		Assert.assertEquals(MiapeMocker.MASS_UNIT + i, substance.getMassUnit());
		Assert.assertEquals(MiapeMocker.TYPE + i, substance.getType());
		Assert.assertEquals(MiapeMocker.VOLUME + i, substance.getVolume());
		Assert.assertEquals(MiapeMocker.VOLUME_UNIT + i, substance.getVolumeUnit());

	}

	public static void assertBufferComponent(Set<BufferComponent> substances, int i) {
		BufferComponent substance = substances.iterator().next();
		Assert.assertEquals(MiapeMocker.COMPONENT_NAME, substance.getName());
		Assert.assertEquals(MiapeMocker.CONCENTRATION + i, substance.getConcentration());
		Assert.assertEquals(MiapeMocker.CONCENTRATION_UNIT + i, substance.getConcentrationUnit());
		Assert.assertEquals(MiapeMocker.DESCRIPTION + i, substance.getDescription());
		Assert.assertEquals(MiapeMocker.MASS + i, substance.getMass());
		Assert.assertEquals(MiapeMocker.MASS_UNIT + i, substance.getMassUnit());
		Assert.assertEquals(MiapeMocker.TYPE + i, substance.getType());
		Assert.assertEquals(MiapeMocker.VOLUME + i, substance.getVolume());
		Assert.assertEquals(MiapeMocker.VOLUME_UNIT + i, substance.getVolumeUnit());

	}

	public static void assertBuffers(Set<Buffer> buffers, int i) {
		Buffer buffer = buffers.iterator().next();
		Assert.assertEquals(MiapeMocker.BUFFER_DESCRIPTION + i, buffer.getDescription());
		Assert.assertEquals(MiapeMocker.BUFFER_NAME + i, buffer.getName());
		Assert.assertEquals(MiapeMocker.BUFFER_TYPE + i, buffer.getType());
		assertBufferComponent(buffer.getComponents(), 88);
	}

	public static void assertImage(Image image, int i) {
		Assert.assertEquals(MiapeMocker.IMAGE_NAME + i, image.getName());
		Assert.assertEquals(MiapeMocker.BIT_DEPTH + i, image.getBitDepth());
		Assert.assertEquals(MiapeMocker.DIMENSION_UNIT + i, image.getDimensionUnit());
		Assert.assertEquals(MiapeMocker.DIMENSION_X + i, image.getDimensionX());
		Assert.assertEquals(MiapeMocker.DIMENSION_Y + i, image.getDimensionY());
		Assert.assertEquals(MiapeMocker.FORMAT + i, image.getFormat());
		Assert.assertEquals(MiapeMocker.LOCATION + i, image.getLocation());
		Assert.assertEquals(MiapeMocker.ORIENTATION + i, image.getOrientation());
		Assert.assertEquals(MiapeMocker.RESOLUTION + i, image.getResolution());
		Assert.assertEquals(MiapeMocker.RESOLUTION_UNIT + i, image.getResolutionUnit());
	}

	public static void assertDirectDetectionAgent(Set<DirectDetectionAgent> substances, int i) {
		DirectDetectionAgent substance = substances.iterator().next();
		Assert.assertEquals(MiapeMocker.SUBSTANCE_NAME + i, substance.getName());
		Assert.assertEquals(MiapeMocker.CONCENTRATION + i, substance.getConcentration());
		Assert.assertEquals(MiapeMocker.CONCENTRATION_UNIT + i, substance.getConcentrationUnit());
		Assert.assertEquals(MiapeMocker.DESCRIPTION + i, substance.getDescription());
		Assert.assertEquals(MiapeMocker.MASS + i, substance.getMass());
		Assert.assertEquals(MiapeMocker.MASS_UNIT + i, substance.getMassUnit());
		Assert.assertEquals(MiapeMocker.TYPE + i, substance.getType());
		Assert.assertEquals(MiapeMocker.VOLUME + i, substance.getVolume());
		Assert.assertEquals(MiapeMocker.VOLUME_UNIT + i, substance.getVolumeUnit());

	}

	public static void assertIndirectDetectionAgent(Set<IndirectDetectionAgent> substances, int i) {
		IndirectDetectionAgent substance = substances.iterator().next();
		Assert.assertEquals(MiapeMocker.SUBSTANCE_NAME + i, substance.getName());
		Assert.assertEquals(MiapeMocker.CONCENTRATION + i, substance.getConcentration());
		Assert.assertEquals(MiapeMocker.CONCENTRATION_UNIT + i, substance.getConcentrationUnit());
		Assert.assertEquals(MiapeMocker.DESCRIPTION + i, substance.getDescription());
		Assert.assertEquals(MiapeMocker.MASS + i, substance.getMass());
		Assert.assertEquals(MiapeMocker.MASS_UNIT + i, substance.getMassUnit());
		Assert.assertEquals(MiapeMocker.TYPE + i, substance.getType());
		Assert.assertEquals(MiapeMocker.VOLUME + i, substance.getVolume());
		Assert.assertEquals(MiapeMocker.VOLUME_UNIT + i, substance.getVolumeUnit());

	}
}
