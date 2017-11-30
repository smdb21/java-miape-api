package org.proteored.miapeapi.xml.msi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDataInconsistencyException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.msi.MiapeMSIValidator;
import org.proteored.miapeapi.xml.miapeproject.UserImpl;
import org.proteored.miapeapi.xml.msi.autogenerated.MIAPEContactType;
import org.proteored.miapeapi.xml.msi.autogenerated.MIAPEProject;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedPeptide;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedPeptideSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProtein;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProteinSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputData;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputDataSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputParameters;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIMIAPEMSI;
import org.proteored.miapeapi.xml.msi.autogenerated.MSISoftwareType;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIValidation;
import org.proteored.miapeapi.xml.msi.autogenerated.ParamType;
import org.proteored.miapeapi.xml.msi.autogenerated.Ref;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.pi.reductions.Reduction;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class MiapeMSIDocumentImpl implements MiapeMSIDocument {
	private static final int MAX_NUMBER_PARALLEL_PROCESSES = 6;
	private int msDocumentID;
	private final MSIMIAPEMSI xmlMSIMiape;
	private PersistenceManager dbManager;
	private final User user;
	private final XmlMiapeFactory<MiapeMSIDocument> xmlFactory;
	private ControlVocabularyManager cvUtil;
	private final Map<String, MSIInputDataSet> mapInputDataSet;
	private final Map<String, MSIInputData> mapInputData;
	private final Map<String, MSIInputParameters> mapInputParameter;

	private final Map<String, MSISoftwareType> mapSoftware;

	private Map<String, IdentifiedPeptide> peptideList = new THashMap<String, IdentifiedPeptide>();
	private Map<String, IdentifiedProtein> proteinList = new THashMap<String, IdentifiedProtein>();

	private int id = -1;
	private boolean throwInconsistencies = true;
	// private final List<IdentifiedProtein> identifiedProteins;
	// private final List<IdentifiedPeptide> peptidesWithoutProtein = new
	// ArrayList<IdentifiedPeptide>();
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public MiapeMSIDocumentImpl(MSIMIAPEMSI xmlMsiMiape, User owner, ControlVocabularyManager controlVocabularyUtil,
			String userName, String password, boolean processInParallel) {
		// clear static identifier counters
		MiapeXmlUtil.clearIdentifierCounters();

		xmlMSIMiape = xmlMsiMiape;
		if (owner != null) {
			user = owner;
		} else {
			if (xmlMsiMiape != null && xmlMsiMiape.getMIAPEProject() != null) {
				if ("ProteoRed Multicentric Experiment 6".equals(xmlMsiMiape.getMIAPEProject().getName())) {
					throwInconsistencies = false;
				}
				user = new UserImpl(xmlMsiMiape.getMIAPEProject().getOwnerName(), userName, password);
			} else {
				user = null;
			}
		}

		xmlFactory = MiapeMSIXmlFactory.getFactory();
		cvUtil = controlVocabularyUtil;

		// this.identifiedProteins = createIdentifiedProteins();
		mapInputDataSet = initMapInputDataSet();
		mapInputData = initMapInputData();
		mapInputParameter = initMapInputParameter();
		// this.mapProteinPeptides = initMapProteinPeptides();
		// this.mapPeptideProteins = initMapPeptideProteins();
		mapSoftware = initMapSoftware();

		if (processInParallel) {
			// NEW JAN2013: this call has to be after the map initializations
			// that
			// are located before this line
			initializeProteinsAndPeptideListsInParallel();
		} else {
			initializeProteinsAndPeptideLists();
		}

	}

	public MiapeMSIDocumentImpl(MSIMIAPEMSI msmiapems, ControlVocabularyManager controlVocabularyUtil,
			PersistenceManager dbManager, String userName, String password, boolean processInParallel)
			throws MiapeDatabaseException, MiapeSecurityException {
		this(msmiapems, dbManager.getUser(userName, password), controlVocabularyUtil, userName, password,
				processInParallel);
		this.dbManager = dbManager;
		cvUtil = controlVocabularyUtil;
	}

	private void initializeProteinsAndPeptideLists() {
		log.info("Reading proteins and peptides...");
		if (xmlMSIMiape != null) {
			MSIIdentifiedPeptideSet msiIdentifiedPeptideSet = xmlMSIMiape.getMSIIdentifiedPeptideSet();
			if (msiIdentifiedPeptideSet != null) {
				List<MSIIdentifiedPeptide> msiIdentifiedPeptides = msiIdentifiedPeptideSet.getMSIIdentifiedPeptide();
				log.info(msiIdentifiedPeptides.size() + " peptides in dataset");
				if (msiIdentifiedPeptides != null) {
					for (MSIIdentifiedPeptide msiIdentifiedPeptide : msiIdentifiedPeptides) {
						// Just include peptides that have been linked to any
						// protein
						// if (msiIdentifiedPeptide.getProteinRefs() != null) {
						peptideList.put(msiIdentifiedPeptide.getId(),
								new IdentifiedPeptideImpl(msiIdentifiedPeptide, mapInputData));
						// }
					}
				}
			}
			List<MSIIdentifiedProteinSet> msiIdentifiedProteinSets = xmlMSIMiape.getMSIIdentifiedProteinSet();
			if (msiIdentifiedProteinSets != null) {
				log.info(msiIdentifiedProteinSets.size() + " proteins in dataset");
				for (MSIIdentifiedProteinSet msiIdentifiedProteinSet : msiIdentifiedProteinSets) {
					List<MSIIdentifiedProtein> msiIdentifiedProteins = msiIdentifiedProteinSet
							.getMSIIdentifiedProtein();
					if (msiIdentifiedProteins != null) {
						for (MSIIdentifiedProtein msiIdentifiedProtein : msiIdentifiedProteins) {
							// Just include proteins that have been linked to
							// any peptide
							if (msiIdentifiedProtein.getPeptideRefs() != null) {
								proteinList.put(msiIdentifiedProtein.getId(),
										new IdentifiedProteinImpl(msiIdentifiedProtein));
							}
						}
					}
				}
			}
		}
		// MAKE THE LINKS BETWEEN PROTEIN AND PEPTIDES:
		if (!peptideList.isEmpty()) {
			for (IdentifiedPeptide peptide : peptideList.values()) {

				IdentifiedPeptideImpl peptideImpl = (IdentifiedPeptideImpl) peptide;
				List<Ref> proteinRefs = peptideImpl.getProteinRefs();
				if (proteinRefs != null && !proteinRefs.isEmpty()) {
					for (Ref ref : proteinRefs) {
						if (proteinList.containsKey(ref.getId())) {
							peptideImpl.addProteinRelationship(proteinList.get(ref.getId()));
						} else {
							String message = "Referenced protein + '" + ref.getId() + "' not present in dataset "
									+ this.getName();
							log.debug(message);
							if (throwInconsistencies) {
								throw new MiapeDataInconsistencyException(message);
							}
						}
					}
				} else {
					String message = "Peptide " + peptide.getId() + " with no proteins";
					log.debug(message);
					if (throwInconsistencies) {
						throw new MiapeDataInconsistencyException(message);
					}
				}
			}
		}
		if (!proteinList.isEmpty()) {
			for (IdentifiedProtein protein : proteinList.values()) {
				IdentifiedProteinImpl proteinImpl = (IdentifiedProteinImpl) protein;
				List<Ref> peptideRefs = proteinImpl.getPeptideRefs();
				if (peptideRefs != null && !peptideRefs.isEmpty()) {
					for (Ref ref : peptideRefs) {
						if (peptideList.containsKey(ref.getId())) {
							proteinImpl.addPeptideRelationship(peptideList.get(ref.getId()));
						} else {
							String message = "Referenced peptide '" + ref.getId() + "' not present in dataset "
									+ this.getName();
							log.debug(message);
							if (throwInconsistencies) {
								throw new MiapeDataInconsistencyException(message);
							}
						}
					}
				} else {
					String message = "Protein " + protein.getId() + " with no peptides";
					log.debug(message);
					if (throwInconsistencies) {
						throw new MiapeDataInconsistencyException(message);
					}
				}
			}
		}

		TIntHashSet peptideIds = new TIntHashSet();
		for (IdentifiedProtein prot : proteinList.values()) {
			if (prot.getIdentifiedPeptides().isEmpty()) {
				log.debug("Protein with no peptides: ID=" + prot.getId() + " in " + xmlMSIMiape.getName());
			}
			for (IdentifiedPeptide pep : prot.getIdentifiedPeptides()) {
				peptideIds.add(pep.getId());
			}
		}
		log.info("peptides=" + peptideList.size());
		TIntHashSet proteinIds = new TIntHashSet();
		for (IdentifiedPeptide pept : peptideList.values()) {

			if (pept.getIdentifiedProteins().isEmpty()) {
				log.debug("Peptide with no proteins: ID=" + pept.getId() + " in " + xmlMSIMiape.getName());
			}
			for (IdentifiedProtein prot : pept.getIdentifiedProteins()) {
				proteinIds.add(prot.getId());
			}
		}
		log.info("proteins=" + proteinList.size());
	}

	private void initializeProteinsAndPeptideListsInParallel() {
		int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		log.info("Reading proteins and peptides in parallel...");
		if (xmlMSIMiape != null) {
			MSIIdentifiedPeptideSet msiIdentifiedPeptideSet = xmlMSIMiape.getMSIIdentifiedPeptideSet();
			if (msiIdentifiedPeptideSet != null) {
				List<MSIIdentifiedPeptide> msiIdentifiedPeptides = msiIdentifiedPeptideSet.getMSIIdentifiedPeptide();
				if (msiIdentifiedPeptides != null) {

					ParIterator<MSIIdentifiedPeptide> iterator = ParIteratorFactory
							.createParIterator(msiIdentifiedPeptides, threadCount, Schedule.GUIDED);

					Reducible<Map<String, IdentifiedPeptide>> reduciblePeptideMap = new Reducible<Map<String, IdentifiedPeptide>>();
					List<IdentifiedPeptideParallelProcesor> runners = new ArrayList<IdentifiedPeptideParallelProcesor>();
					for (int numCore = 0; numCore < threadCount; numCore++) {
						// take current DB session
						IdentifiedPeptideParallelProcesor runner = new IdentifiedPeptideParallelProcesor(iterator,
								reduciblePeptideMap, mapInputData);
						runners.add(runner);
						runner.start();
					}
					// Main thread waits for worker threads to complete
					for (int k = 0; k < threadCount; k++) {
						try {
							runners.get(k).join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (iterator.getAllExceptions().length > 0) {
						throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
					}
					Reduction<Map<String, IdentifiedPeptide>> peptideReduction = new Reduction<Map<String, IdentifiedPeptide>>() {
						@Override
						public Map<String, IdentifiedPeptide> reduce(Map<String, IdentifiedPeptide> first,
								Map<String, IdentifiedPeptide> second) {
							Map<String, IdentifiedPeptide> ret = new THashMap<String, IdentifiedPeptide>();
							for (String peptideID : first.keySet()) {
								if (!ret.containsKey(peptideID)) {
									ret.put(peptideID, first.get(peptideID));
								}
							}
							for (String peptideID : second.keySet()) {
								if (!ret.containsKey(peptideID)) {
									ret.put(peptideID, second.get(peptideID));
								}
							}
							return ret;
						}

					};
					peptideList = reduciblePeptideMap.reduce(peptideReduction);
					log.info("Parsed " + peptideList.size() + " peptides in paralell");
				}
			}

			// Proteins
			List<MSIIdentifiedProteinSet> msiIdentifiedProteinSets = xmlMSIMiape.getMSIIdentifiedProteinSet();
			if (msiIdentifiedProteinSets != null) {
				for (MSIIdentifiedProteinSet msiIdentifiedProteinSet : msiIdentifiedProteinSets) {
					List<MSIIdentifiedProtein> msiIdentifiedProteins = msiIdentifiedProteinSet
							.getMSIIdentifiedProtein();
					if (msiIdentifiedProteins != null) {
						ParIterator<MSIIdentifiedProtein> iterator = ParIteratorFactory
								.createParIterator(msiIdentifiedProteins, threadCount, Schedule.GUIDED);
						Reducible<Map<String, IdentifiedProtein>> reducibleProteinMap = new Reducible<Map<String, IdentifiedProtein>>();
						List<IdentifiedProteinParallelProcesor> runners = new ArrayList<IdentifiedProteinParallelProcesor>();
						for (int numCore = 0; numCore < threadCount; numCore++) {
							// take current DB session
							IdentifiedProteinParallelProcesor runner = new IdentifiedProteinParallelProcesor(iterator,
									reducibleProteinMap);
							runners.add(runner);
							runner.start();
						}

						// Main thread waits for worker threads to complete
						for (int k = 0; k < threadCount; k++) {
							try {
								runners.get(k).join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						Reduction<Map<String, IdentifiedProtein>> ProteinReduction = new Reduction<Map<String, IdentifiedProtein>>() {
							@Override
							public Map<String, IdentifiedProtein> reduce(Map<String, IdentifiedProtein> first,
									Map<String, IdentifiedProtein> second) {
								Map<String, IdentifiedProtein> ret = new THashMap<String, IdentifiedProtein>();
								for (String ProteinID : first.keySet()) {
									if (!ret.containsKey(ProteinID)) {
										ret.put(ProteinID, first.get(ProteinID));
									}
								}
								for (String ProteinID : second.keySet()) {
									if (!ret.containsKey(ProteinID)) {
										ret.put(ProteinID, second.get(ProteinID));
									}
								}
								return ret;
							}

						};
						proteinList = reducibleProteinMap.reduce(ProteinReduction);
						log.info("Parsed " + proteinList.size() + " proteins in paralell");
					}
				}
			}
		}
		// MAKE THE LINKS BETWEEN PROTEIN AND PEPTIDES:
		if (!peptideList.isEmpty()) {
			for (IdentifiedPeptide peptide : peptideList.values()) {

				IdentifiedPeptideImpl peptideImpl = (IdentifiedPeptideImpl) peptide;
				List<Ref> proteinRefs = peptideImpl.getProteinRefs();
				if (proteinRefs != null) {
					for (Ref ref : proteinRefs) {
						if (proteinList.containsKey(ref.getId())) {
							peptideImpl.addProteinRelationship(proteinList.get(ref.getId()));
						} else {
							log.info("Protein + '" + ref.getId() + "' referenced by peptid '" + peptide.getId()
									+ "' is not present in MIAPE " + xmlMSIMiape.getName());
						}
					}
				}
			}
		}
		if (!proteinList.isEmpty()) {
			for (IdentifiedProtein protein : proteinList.values()) {
				IdentifiedProteinImpl proteinImpl = (IdentifiedProteinImpl) protein;
				List<Ref> peptideRefs = proteinImpl.getPeptideRefs();
				if (peptideRefs != null) {
					for (Ref ref : peptideRefs) {
						if (peptideList.containsKey(ref.getId()))
							proteinImpl.addPeptideRelationship(peptideList.get(ref.getId()));
						else
							log.info("Peptide '" + ref.getId() + "' not present in MIAPE " + xmlMSIMiape.getName());
					}
				}
			}
		}

	}

	private Map<String, MSISoftwareType> initMapSoftware() {
		Map<String, MSISoftwareType> map = new THashMap<String, MSISoftwareType>();
		List<MSISoftwareType> msiSoftware = xmlMSIMiape.getMSISoftware();
		if (msiSoftware != null) {
			for (MSISoftwareType softwareXML : msiSoftware) {
				map.put(softwareXML.getId(), softwareXML);
			}
		}
		return map;
	}

	private Map<String, MSIInputParameters> initMapInputParameter() {
		Map<String, MSIInputParameters> map = new THashMap<String, MSIInputParameters>();
		List<MSIInputParameters> msiInputParameters = xmlMSIMiape.getMSIInputParameters();
		if (msiInputParameters != null) {
			for (MSIInputParameters inputParameterXML : msiInputParameters) {
				map.put(inputParameterXML.getId(), inputParameterXML);
			}
		}
		return map;
	}

	private Map<String, MSIInputData> initMapInputData() {
		Map<String, MSIInputData> map = new THashMap<String, MSIInputData>();
		List<MSIInputDataSet> msiInputDataSet = xmlMSIMiape.getMSIInputDataSet();
		if (msiInputDataSet != null) {
			for (MSIInputDataSet inputDataSetXML : msiInputDataSet) {
				List<MSIInputData> msiInputData = inputDataSetXML.getMSIInputData();
				if (msiInputData != null) {
					for (MSIInputData inputDataXML : msiInputData) {
						map.put(inputDataXML.getId(), inputDataXML);
					}
				}
			}
		}
		return map;
	}

	private Map<String, MSIInputDataSet> initMapInputDataSet() {
		Map<String, MSIInputDataSet> map = new THashMap<String, MSIInputDataSet>();
		List<MSIInputDataSet> msiInputDataSet = xmlMSIMiape.getMSIInputDataSet();
		if (msiInputDataSet != null) {
			for (MSIInputDataSet xmlInputDataSet : msiInputDataSet) {
				map.put(xmlInputDataSet.getId(), xmlInputDataSet);
			}
		}
		return map;
	}

	public void setDatabaseManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	public String getGeneratedFilesDescription() {
		return xmlMSIMiape.getGeneratedFilesDescription();
	}

	@Override
	public String getGeneratedFilesURI() {
		return xmlMSIMiape.getGeneratedFilesURL();
	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {
		List<MSIIdentifiedProteinSet> msiIdentifiedProteinSet = xmlMSIMiape.getMSIIdentifiedProteinSet();
		if (msiIdentifiedProteinSet == null)
			return null;
		Set<IdentifiedProteinSet> proteinSet = new THashSet<IdentifiedProteinSet>();
		for (MSIIdentifiedProteinSet xmlProteinSet : msiIdentifiedProteinSet) {
			proteinSet.add(new IdentifiedProteinSetImpl(xmlProteinSet, mapInputData, mapInputDataSet, mapInputParameter,
					mapSoftware, proteinList));
		}
		return proteinSet;
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		List<MSIInputDataSet> msiInputDataSet = xmlMSIMiape.getMSIInputDataSet();
		if (msiInputDataSet == null)
			return null;
		Set<InputDataSet> inputDataSets = new THashSet<InputDataSet>();
		for (MSIInputDataSet xmlInputDataSet : msiInputDataSet) {
			inputDataSets.add(new InputDataSetImpl(xmlInputDataSet));
		}
		return inputDataSets;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		List<MSIInputParameters> msiInputParameters = xmlMSIMiape.getMSIInputParameters();
		if (msiInputParameters == null)
			return null;
		Set<InputParameter> parameters = new THashSet<InputParameter>();
		for (MSIInputParameters xmlInputParameter : msiInputParameters) {
			parameters.add(new InputParameterImpl(xmlInputParameter, mapSoftware));
		}
		return parameters;
	}

	@Override
	public Set<Software> getSoftwares() {
		List<MSISoftwareType> msiSoftware = xmlMSIMiape.getMSISoftware();
		if (msiSoftware == null)
			return null;
		Set<Software> softwares = new THashSet<Software>();
		for (MSISoftwareType software : msiSoftware) {
			softwares.add(new SoftwareMSIImpl(software));
		}
		return softwares;
	}

	@Override
	public int getMSDocumentReference() {
		if (msDocumentID > 0)
			return msDocumentID;
		if (xmlMSIMiape != null) {
			Integer miapemsRef = xmlMSIMiape.getMIAPEMSRef();
			if (miapemsRef != null)
				return miapemsRef;
		}
		return -1;
	}

	@Override
	public Set<Validation> getValidations() {
		Set<Validation> validations = new THashSet<Validation>();
		List<MSIValidation> msiValidation = xmlMSIMiape.getMSIValidation();
		if (msiValidation == null)
			return null;
		for (MSIValidation xmlValidation : msiValidation) {
			validations.add(new ValidationImpl(xmlValidation));
		}
		return validations;
	}

	@Override
	public MSContact getContact() {
		MIAPEContactType msiContact = xmlMSIMiape.getMSIContact();
		if (msiContact != null)
			return new ContactImpl(msiContact);
		return null;
	}

	@Override
	public MiapeDate getDate() {
		String date = xmlMSIMiape.getDate();
		if (date != null)
			return new MiapeDate(date);
		return null;
	}

	@Override
	public int getId() {
		if (id > 0) {
			return id;
		}
		return xmlMSIMiape.getId();

	}

	@Override
	public Date getModificationDate() {
		XMLGregorianCalendar modificationDate = xmlMSIMiape.getModificationDate();
		if (modificationDate != null && modificationDate.toGregorianCalendar() != null) {
			return modificationDate.toGregorianCalendar().getTime();
		}
		return null;
	}

	@Override
	public String getName() {
		return xmlMSIMiape.getName();
	}

	@Override
	public User getOwner() {
		return user;
	}

	@Override
	public String getAttachedFileLocation() {
		return xmlMSIMiape.getAttachedFileLocation();
	}

	@Override
	public Project getProject() {
		MIAPEProject project = xmlMSIMiape.getMIAPEProject();
		if (project != null)
			return new ProjectImpl(project, user, dbManager);
		return null;
	}

	@Override
	public Boolean getTemplate() {
		return xmlMSIMiape.isTemplate();
	}

	@Override
	public String getVersion() {
		return xmlMSIMiape.getVersion();
	}

	@Override
	public void delete(String user, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null && dbManager.getMiapeMSPersistenceManager() != null) {
			dbManager.getMiapeMSIPersistenceManager().deleteById(getId(), user, password);
		} else {
			throw new MiapeDatabaseException("The persistance method is not defined.");
		}
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null && dbManager.getMiapeMSIPersistenceManager() != null) {
			id = dbManager.getMiapeMSIPersistenceManager().store(this);
			return id;
		}
		throw new MiapeDatabaseException("The persistance method is not defined.");
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml() {
		if (xmlFactory != null) {
			return xmlFactory.toXml(this, cvUtil);
		}
		throw new IllegalMiapeArgumentException("The xml Factory is not defined.");

	}

	/*
	 * private List<IdentifiedProtein> createIdentifiedProteins() {
	 * List<IdentifiedProtein> result = new ArrayList<IdentifiedProtein>(); for
	 * (MSIIdentifiedProteinSet proteinSets :
	 * xmlMSIMiape.getMSIIdentifiedProteinSet()) { for (MSIIdentifiedProtein
	 * protein : proteinSets.getMSIIdentifiedProtein()) { result.add(new
	 * IdentifiedProteinImpl(protein, mapProteinPeptide)); } } return result; }
	 */

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		Set<MSIAdditionalInformation> setOfAdditionalInformation = new THashSet<MSIAdditionalInformation>();
		List<ParamType> additionalInformationList = xmlMSIMiape.getMSIAdditionalInformation();
		if (additionalInformationList != null) {
			for (ParamType msAdditionalInformation : additionalInformationList) {
				setOfAdditionalInformation.add(new AdditionalInformationImpl(msAdditionalInformation));
			}
			return setOfAdditionalInformation;
		}
		return null;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeMSIValidator.getInstance().getReport(this);
	}

	@Override
	public void setReferencedMSDocument(int msID) {
		msDocumentID = msID;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		List<IdentifiedPeptide> ret = new ArrayList<IdentifiedPeptide>();
		if (peptideList != null) {
			for (IdentifiedPeptide identifiedPeptide : peptideList.values()) {

				ret.add(identifiedPeptide);
			}
		}
		// if (this.xmlMSIMiape.getMSIIdentifiedPeptideSet() != null) {
		// for (MSIIdentifiedPeptide xmlPeptide : this.xmlMSIMiape
		// .getMSIIdentifiedPeptideSet().getMSIIdentifiedPeptide()) {
		// ret.add(new IdentifiedPeptideImpl(xmlPeptide, mapInputData,
		// mapPeptideProteins, mapProteinPeptides));
		// }
		// }
		return ret;
	}

	@Override
	public void setId(int id) {
		this.id = id;

	}
}
