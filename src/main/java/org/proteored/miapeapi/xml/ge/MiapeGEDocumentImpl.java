package org.proteored.miapeapi.xml.ge;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;
import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.ge.Protocol;
import org.proteored.miapeapi.interfaces.ge.Sample;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.ge.MiapeGEValidator;
import org.proteored.miapeapi.xml.ge.autogenerated.GEDimensionType;
import org.proteored.miapeapi.xml.ge.autogenerated.GEDirectDetection;
import org.proteored.miapeapi.xml.ge.autogenerated.GEGelMatrix;
import org.proteored.miapeapi.xml.ge.autogenerated.GEImage;
import org.proteored.miapeapi.xml.ge.autogenerated.GEImageAcquisition;
import org.proteored.miapeapi.xml.ge.autogenerated.GEIndirectDetection;
import org.proteored.miapeapi.xml.ge.autogenerated.GEMIAPEGE;
import org.proteored.miapeapi.xml.ge.autogenerated.GEProtocol;
import org.proteored.miapeapi.xml.ge.autogenerated.GESample;
import org.proteored.miapeapi.xml.ge.autogenerated.MIAPEContactType;
import org.proteored.miapeapi.xml.ge.autogenerated.MIAPEProject;
import org.proteored.miapeapi.xml.ge.autogenerated.ParamType;
import org.proteored.miapeapi.xml.ge.util.GEControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.miapeproject.UserImpl;

public class MiapeGEDocumentImpl implements MiapeGEDocument {
	private final GEMIAPEGE miapeGeXML;
	private PersistenceManager dbManager;
	private final User user;
	private final XmlMiapeFactory<MiapeGEDocument> xmlFactory;
	private ControlVocabularyManager cvUtil;
	private final Map<String, GESample> sampleMap;
	private final Map<String, GEGelMatrix> gelMatrixMap;
	private int id;

	public MiapeGEDocumentImpl(GEMIAPEGE miapeGEXML, User owner,
			ControlVocabularyManager controlVocabularyUtil, String userName,
			String password) {
		miapeGeXML = miapeGEXML;
		if (owner != null)
			user = owner;
		else {
			if (miapeGEXML != null && miapeGEXML.getMIAPEProject() != null)
				user = new UserImpl(
						miapeGEXML.getMIAPEProject().getOwnerName(), userName,
						password);
			else
				user = null;
		}

		xmlFactory = MiapeGEXmlFactory.getFactory();
		cvUtil = controlVocabularyUtil;
		sampleMap = initMapLaneSample();
		gelMatrixMap = initGelMatrixMap();
		id = miapeGeXML.getId();
	}

	public MiapeGEDocumentImpl(GEMIAPEGE miapeGEXML,
			ControlVocabularyManager controlVocabularyUtil,
			PersistenceManager dbManager, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		this(miapeGEXML, dbManager.getUser(userName, password),
				controlVocabularyUtil, userName, password);
		this.dbManager = dbManager;
		cvUtil = controlVocabularyUtil;
	}

	private Map<String, GEGelMatrix> initGelMatrixMap() {
		Map<String, GEGelMatrix> map = new HashMap<String, GEGelMatrix>();

		List<GEProtocol> geProtocol = miapeGeXML.getGEProtocol();
		if (geProtocol != null) {
			for (GEProtocol protocolItem : geProtocol) {
				List<GEDimensionType> geFirstDimension = protocolItem
						.getGEFirstDimension();
				if (geFirstDimension != null) {
					for (GEDimensionType dimensionItem : geFirstDimension) {
						List<GEGelMatrix> geGelMatrix = dimensionItem
								.getGEGelMatrix();
						if (geGelMatrix != null) {
							for (GEGelMatrix gelMatrixItem : geGelMatrix) {
								map.put(gelMatrixItem.getId(), gelMatrixItem);
							}
						}
					}
				}

			}
		}

		return map;
	}

	private Map<String, GESample> initMapLaneSample() {
		Map<String, GESample> map = new HashMap<String, GESample>();
		List<GESample> geSample = miapeGeXML.getGESample();
		if (geSample != null) {
			for (GESample sampleItem : geSample) {
				map.put(sampleItem.getId(), sampleItem);
			}
		}

		return map;
	}

	@Override
	public Set<DirectDetection> getDirectDetections() {
		/* Get GEDirectDetection list */
		List<GEDirectDetection> directDetectionList = miapeGeXML
				.getGEDirectDetection();
		if (directDetectionList != null) {
			Set<DirectDetection> setOfDirectDetections = new HashSet<DirectDetection>();

			/*
			 * For each GEDirectDetection, create an implementation
			 * (DirectDetectionImpl)
			 */
			/* and add it to the Set of DirectDetection */

			for (GEDirectDetection directDetectionItem : directDetectionList) {
				setOfDirectDetections.add(new DirectDetectionImpl(
						directDetectionItem));
			}

			return setOfDirectDetections;
		}
		return null;
	}

	@Override
	public String getElectrophoresisType() {
		ParamType electrophoresisType = miapeGeXML.getElectrophoresisType();
		if (electrophoresisType != null)
			return GEControlVocabularyXmlFactory.getName(electrophoresisType);
		return null;
	}

	@Override
	public Set<ImageAcquisition> getImageAcquisitions() {
		Set<ImageAcquisition> setOfImageAcquisition = new HashSet<ImageAcquisition>();
		List<GEImageAcquisition> imageAcquisitionList = miapeGeXML
				.getGEImageAcquisition();
		if (imageAcquisitionList != null) {
			for (GEImageAcquisition imageAcquisitionItem : imageAcquisitionList)
				setOfImageAcquisition.add(new ImageAcquisitionImpl(
						imageAcquisitionItem, gelMatrixMap, sampleMap));
			return setOfImageAcquisition;
		}
		return null;
	}

	@Override
	public Set<ImageGelElectrophoresis> getImages() {
		List<GEImage> imageList = miapeGeXML.getGEImage();
		if (imageList != null) {
			Set<ImageGelElectrophoresis> setOfImages = new HashSet<ImageGelElectrophoresis>();
			for (GEImage geImage : imageList) {
				setOfImages
						.add(new ImageImpl(geImage, gelMatrixMap, sampleMap));
			}
			return setOfImages;
		}
		return null;
	}

	@Override
	public Set<IndirectDetection> getIndirectDetections() {

		List<GEIndirectDetection> indirectDetectionList = miapeGeXML
				.getGEIndirectDetection();
		if (indirectDetectionList != null) {
			Set<IndirectDetection> setOfIndirectDetection = new HashSet<IndirectDetection>();

			for (GEIndirectDetection geIndirectDetection : indirectDetectionList) {
				setOfIndirectDetection.add(new IndirectDetectionImpl(
						geIndirectDetection));
			}

			return setOfIndirectDetection;
		}
		return null;

	}

	@Override
	public Set<Protocol> getProtocols() {
		Set<Protocol> setOfProtocols = new HashSet<Protocol>();
		List<GEProtocol> protocolList = miapeGeXML.getGEProtocol();
		if (protocolList != null) {
			for (GEProtocol geProtocol : protocolList) {
				setOfProtocols.add(new ProtocolImpl(geProtocol, sampleMap));
			}
			return setOfProtocols;
		}
		return null;
	}

	@Override
	public Set<Sample> getSamples() {
		Set<Sample> setOfSamples = new HashSet<Sample>();
		List<GESample> sampleList = miapeGeXML.getGESample();
		if (sampleList != null) {
			for (GESample geSample : sampleList) {
				setOfSamples.add(new SampleImpl(geSample));
			}
			return setOfSamples;
		}
		return null;
	}

	@Override
	public GEContact getContact() {
		MIAPEContactType geContact = miapeGeXML.getGEContact();
		if (geContact != null)
			return new ContactImpl(geContact);
		return null;
	}

	@Override
	public MiapeDate getDate() {
		String date = miapeGeXML.getDate();
		if (date != null) {
			return new MiapeDate(date);
		}
		return null;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Date getModificationDate() {
		XMLGregorianCalendar modificationDate = miapeGeXML
				.getModificationDate();
		if (modificationDate != null) {
			return modificationDate.toGregorianCalendar().getTime();
		}
		return null;
	}

	@Override
	public String getName() {
		return miapeGeXML.getName();
	}

	@Override
	public User getOwner() {
		return user;
	}

	@Override
	public String getAttachedFileLocation() {
		return miapeGeXML.getAttachedFileLocation();
	}

	@Override
	public Project getProject() {
		if (id > 0 && dbManager != null) {
			try {
				return dbManager
						.getMiapeGEPersistenceManager()
						.getMiapeById(id, user.getUserName(),
								user.getPassword()).getProject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MIAPEProject project = miapeGeXML.getMIAPEProject();
		if (project != null)
			return new ProjectImpl(project, user, dbManager);
		return null;
	}

	@Override
	public Boolean getTemplate() {
		return miapeGeXML.isTemplate();
	}

	@Override
	public String getVersion() {
		return miapeGeXML.getVersion();
	}

	@Override
	public void delete(String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null
				&& dbManager.getMiapeGEPersistenceManager() != null) {
			dbManager.getMiapeGEPersistenceManager().deleteById(getId(),
					userName, password);
		}
		throw new MiapeDatabaseException(
				"The persistance method is not defined.");
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null
				&& dbManager.getMiapeGEPersistenceManager() != null) {
			id = dbManager.getMiapeGEPersistenceManager().store(this);
		}
		throw new MiapeDatabaseException(
				"The persistance method is not defined.");
	}

	@Override
	public MiapeXmlFile<MiapeGEDocument> toXml() {
		if (xmlFactory != null) {
			xmlFactory.toXml(this, cvUtil);
		}
		throw new IllegalMiapeArgumentException(
				"The xml Factory is not defined.");
	}

	@Override
	public Set<GEAdditionalInformation> getAdditionalInformations() {
		Set<GEAdditionalInformation> setOfAdditionalInformation = new HashSet<GEAdditionalInformation>();
		List<ParamType> additionalInformationList = miapeGeXML
				.getGEAdditionalInformation();
		if (additionalInformationList != null) {
			for (ParamType geAdditionalInformation : additionalInformationList) {
				setOfAdditionalInformation.add(new AdditionalInformationImpl(
						geAdditionalInformation));
			}
			return setOfAdditionalInformation;
		}
		return null;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeGEValidator.getInstance().getReport(this);
	}

}
