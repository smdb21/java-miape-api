package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.RetentionTime;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.Contact;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.msi.MiapeMSIValidator;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.InputDataSetImpl;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeIdentifierCounter;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;
import org.proteored.miapeapi.xml.util.parallel.MapSync;

import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.exceptions.ParIteratorException;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.pi.reductions.Reduction;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractContact;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisProtocolCollection;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSampleCollection;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftwareList;
import uk.ac.ebi.jmzidml.model.mzidml.AuditCollection;
import uk.ac.ebi.jmzidml.model.mzidml.BibliographicReference;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.InputSpectra;
import uk.ac.ebi.jmzidml.model.mzidml.InputSpectrumIdentifications;
import uk.ac.ebi.jmzidml.model.mzidml.MzIdentML;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetection;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.Provider;
import uk.ac.ebi.jmzidml.model.mzidml.Sample;
import uk.ac.ebi.jmzidml.model.mzidml.SearchDatabase;
import uk.ac.ebi.jmzidml.model.mzidml.SearchDatabaseRef;
import uk.ac.ebi.jmzidml.model.mzidml.SourceFile;
import uk.ac.ebi.jmzidml.model.mzidml.SpectraData;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentification;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItemRef;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

public class MiapeMSIDocumentImpl implements MiapeMSIDocument {
	private static final int MAX_NUMBER_PARALLEL_PROCESSES = Integer.MAX_VALUE;

	private int msDocumentID;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	protected XmlMiapeFactory<MiapeMSDocument> xmlFactory;
	private final MzIdentMLUnmarshaller mzIdentMLUnmarshaller;

	private PersistenceManager dbManager;
	private final User user;
	private final String projectName;
	private int id = -1;
	protected Contact contact;
	protected ControlVocabularyManager cvManager;
	private List<AbstractContact> mzIdentContactList;

	private final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();
	private final Map<String, InputParameter> inputParameters = new THashMap<String, InputParameter>();
	private final Map<String, Software> msiSoftwares = new THashMap<String, Software>();
	private final Set<InputDataSet> inputDataSets = new THashSet<InputDataSet>();
	private final Set<Validation> validations = new THashSet<Validation>();
	private final List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
	public String url;
	private String generatedFileURI;
	private final String mzIdentMLFileName;
	// Map<ProteinACC, Protein>
	private Map<String, ProteinDetectionHypothesis> proteinDetectionHypotesisWithPeptideEvidence = new THashMap<String, ProteinDetectionHypothesis>();
	private AuditCollection auditCollection;
	private AnalysisSoftwareList analysisSoftwareList;
	private AnalysisProtocolCollection analysisProtocolCollection;
	private AnalysisSampleCollection analysisSampleCollection;
	private Iterator<SourceFile> sourceFiles;
	private Iterator<SearchDatabase> searchDataBases;
	private int sourceFilesCount;

	private final Map<ProteinDetectionHypothesis, DBSequence> pdhDBSeqMap = new THashMap<ProteinDetectionHypothesis, DBSequence>();

	private final Map<String, SpectrumIdentificationList> spectrumIdentificationListMap = new THashMap<String, SpectrumIdentificationList>();

	private Iterator<SpectrumIdentificationList> silIterator;

	private final Map<String, Set<String>> spectrumIdentificationItemsByPeptideEvidence = new THashMap<String, Set<String>>();

	public MiapeMSIDocumentImpl(MzIdentMLUnmarshaller unmarshaller, ControlVocabularyManager cvManager,
			String mzIdentMLFileName, String projectName, boolean processInParallel) throws JAXBException {
		mzIdentMLUnmarshaller = unmarshaller;
		user = null;
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.mzIdentMLFileName = mzIdentMLFileName;
		if (false && processInParallel) {
			log.info("Starting processing of mzIdentML in parallel");
			processMzIdentMLInParallel();
		} else {
			log.info("Starting processing of mzIdentML using just one core");
			processMzIdentML();
		}

	}

	public MiapeMSIDocumentImpl(MzIdentMLUnmarshaller unmarshaller, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String user, String password, String mzIdentMLFileName,
			String projectName, boolean processInParallel)
			throws MiapeDatabaseException, MiapeSecurityException, JAXBException {
		mzIdentMLUnmarshaller = unmarshaller;
		this.user = databaseManager.getUser(user, password);
		this.cvManager = cvManager;
		this.projectName = projectName;
		dbManager = databaseManager;
		this.mzIdentMLFileName = mzIdentMLFileName;
		if (false && processInParallel) {
			log.info("Starting processing of mzIdentML in parallel");
			processMzIdentMLInParallel();
		} else {
			log.info("Starting processing of mzIdentML using just one core");
			processMzIdentML();
		}

	}

	/**
	 * Main method that reads the mzIdentML file and create the MIAPE MSI sections
	 * in paralell
	 *
	 * @param mzIdentMLUnmarshaller
	 */
	/**
	 * @throws JAXBException
	 *
	 */
	private void processMzIdentMLInParallel() throws JAXBException {
		// clear static identifier counters
		MiapeXmlUtil.clearIdentifierCounters();
		final Map<String, InputDataSet> inputDataSetMap = new THashMap<String, InputDataSet>();

		String spectrumIdentificationSoftwareID = "";
		log.info("unmarshalling sourceFiles");
		sourceFiles = mzIdentMLUnmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.SourceFile);
		if (sourceFiles.hasNext())
			sourceFilesCount = mzIdentMLUnmarshaller.getObjectCountForXpath(MzIdentMLElement.SourceFile.getXpath());
		log.info("unmarshalling searchDatbases");
		searchDataBases = mzIdentMLUnmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.SearchDatabase);
		log.info("unmarshalling analysisSoftwareList");
		analysisSoftwareList = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisSoftwareList);
		log.info("unmarshalling auditCollection");
		auditCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AuditCollection);
		if (auditCollection != null) {
			mzIdentContactList = auditCollection.getPersonOrOrganization();

		}

		log.info("unmarshalling analysisProtocolCollection");
		analysisProtocolCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisProtocolCollection);
		log.info("unmarshalling analysisSampleCollection");
		analysisSampleCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisSampleCollection);
		log.info("unmarshalling spectrumIdentification");

		final Iterator<SpectrumIdentification> spectrumIdentifications = mzIdentMLUnmarshaller
				.unmarshalCollectionFromXpath(MzIdentMLElement.SpectrumIdentification);

		initProteinHypothesisByPeptideEvidenceHashMapInParallel();
		log.info("Processing spectrumIdentifications");
		int i = 1;
		// for (SpectrumIdentification spectrumIdent : spectrumIdentifications)
		// {
		while (spectrumIdentifications.hasNext()) {
			final SpectrumIdentification spectrumIdent = spectrumIdentifications.next();
			final SpectrumIdentificationList spectrumIdentificationList = getSpectrumIdentificationList(
					spectrumIdent.getSpectrumIdentificationListRef());
			Map<String, IdentifiedProtein> proteinHash = new THashMap<String, IdentifiedProtein>();

			log.info("spectrum identification " + i++ + " " + spectrumIdent.getId());

			SpectrumIdentificationProtocol spectrumIdentProtocol = null;
			// spectrumIdentProtocol = spectrumIdent
			// .getSpectrumIdentificationProtocol();
			// if (spectrumIdentProtocol == null)
			try {
				spectrumIdentProtocol = mzIdentMLUnmarshaller.unmarshal(SpectrumIdentificationProtocol.class,
						spectrumIdent.getSpectrumIdentificationProtocolRef());
			} catch (final JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// getSpectrumIdentificationProtocol(spectrumIdent.getSpectrumIdentificationProtocolRef(),
			// analysisProtocolCollection.getSpectrumIdentificationProtocol());
			final List<SearchDatabase> databaseListXML = getSearchDatabases(spectrumIdent.getSearchDatabaseRef(),
					searchDataBases);

			final AnalysisSoftware softwareXML = spectrumIdentProtocol.getAnalysisSoftware();
			if (softwareXML != null)
				spectrumIdentificationSoftwareID = softwareXML.getId();

			final ProteinDetectionProtocol pdp = getProteinDetectionProtocol(
					spectrumIdent.getSpectrumIdentificationListRef());
			// getSoftware(
			// spectrumIdentProtocol.getAnalysisSoftwareRef(),
			// analysisSoftware);
			Software msiSoftware = null;
			if (softwareXML != null) {
				if (msiSoftwares.containsKey(softwareXML.getId())) {
					msiSoftware = msiSoftwares.get(softwareXML.getId());
				} else {
					final Integer softwareID = MiapeIdentifierCounter.increaseCounter();
					msiSoftware = new SoftwareImpl(softwareXML, softwareID, cvManager);
					msiSoftwares.put(softwareXML.getId(), msiSoftware);
				}
			}
			// SpectrumIdentificationList spectIdentListXML =
			// getSpectrumIdentificationList(spectrumIdent
			// .getSpectrumIdentificationListRef());
			final Map<String, String> elementAttributes = mzIdentMLUnmarshaller.getElementAttributes(
					spectrumIdent.getSpectrumIdentificationListRef(), SpectrumIdentificationList.class);
			Long numSeqSearched = (long) -1;
			if (elementAttributes.containsKey("numSequencesSearched"))
				numSeqSearched = Long.valueOf(elementAttributes.get("numSequencesSearched"));

			// Input parameter and databases
			InputParameter inputParameter = null;
			final String inputParameterKey = getInputParameterKey(spectrumIdentProtocol, pdp, databaseListXML,
					msiSoftware, numSeqSearched);
			if (inputParameters.containsKey(inputParameterKey)) {
				inputParameter = inputParameters.get(inputParameterKey);
			} else {
				final Integer inputParamID = MiapeIdentifierCounter.increaseCounter();
				inputParameter = new InputParameterImpl(spectrumIdentProtocol, pdp, databaseListXML, msiSoftware,
						inputParamID, numSeqSearched, cvManager);
				inputParameters.put(inputParameterKey, inputParameter);
			}

			// inputDataSet
			// Integer inputDataSetID = MiapeXmlUtil.InputDataSetCounter
			// .increaseCounter();
			// InputDataSet inputDataSet = new InputDataSetImpl(inputDataSetID);
			// inputDataSets.add(inputDataSet);
			// inputDataSet
			final List<InputSpectra> inputSpectraXML = spectrumIdent.getInputSpectra();
			if (inputSpectraXML != null) {
				for (final InputSpectra inputSpectra : inputSpectraXML) {
					final String spectraDataRef = inputSpectra.getSpectraDataRef();
					if (!inputDataSetMap.containsKey(spectraDataRef)) {
						final Integer inputDataSetID = MiapeIdentifierCounter.increaseCounter();
						final InputDataSet inputDataSet = new InputDataSetImpl(inputDataSetID, spectraDataRef);
						inputDataSetMap.put(spectraDataRef, inputDataSet);
					}
				}
			}

			// proteinSet
			final IdentifiedProteinSet proteinSet = new ProteinSetImpl(inputParameter, inputDataSetMap.values());
			proteinSets.add(proteinSet);

			// time
			final long t1 = System.currentTimeMillis();

			final List<SpectrumIdentificationResult> spectrumIdentificationResultList = spectrumIdentificationList
					.getSpectrumIdentificationResult();
			final Iterator<SpectrumIdentificationResult> spectrumIdentificationResultIterator = spectrumIdentificationResultList
					.iterator();

			final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);

			log.info("Using " + threadCount + " processors from processing " + spectrumIdentificationResultList.size()
					+ " SIR");
			final ParIterator<SpectrumIdentificationResult> iterator = ParIteratorFactory.createParIterator(
					spectrumIdentificationResultIterator, spectrumIdentificationResultList.size(), threadCount,
					Schedule.GUIDED);

			final Reducible<List<IdentifiedPeptide>> reduciblePeptides = new Reducible<List<IdentifiedPeptide>>();
			final Map<String, InputData> inputDataHash = new THashMap<String, InputData>();
			final MapSync<String, InputData> syncInputDataHash = new MapSync<String, InputData>(inputDataHash);
			final Reducible<Map<String, IdentifiedProtein>> reducibleProteinHash = new Reducible<Map<String, IdentifiedProtein>>();

			final List<SpectrumIdentificationResultParallelProcesser> runners = new ArrayList<SpectrumIdentificationResultParallelProcesser>();
			for (int numCore = 0; numCore < threadCount; numCore++) {
				// take current DB session
				final SpectrumIdentificationResultParallelProcesser runner = new SpectrumIdentificationResultParallelProcesser(
						iterator, numCore, cvManager, mzIdentMLUnmarshaller, syncInputDataHash, reduciblePeptides,
						new MapSync<String, ProteinDetectionHypothesis>(proteinDetectionHypotesisWithPeptideEvidence),
						reducibleProteinHash);
				runners.add(runner);
				runner.start();
			}

			// Main thread waits for worker threads to complete
			for (int k = 0; k < threadCount; k++) {
				try {
					runners.get(k).join();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Handle exceptions
			final ParIteratorException<SpectrumIdentificationResult>[] piExceptions = iterator.getAllExceptions();
			for (int k = 0; k < piExceptions.length; k++) {
				final ParIteratorException<SpectrumIdentificationResult> pie = piExceptions[k];
				// object for iteration in which exception was encountered
				final Object iteration = pie.getIteration();
				// thread executing that iteration
				final Thread thread = pie.getRegisteringThread();
				// actual exception thrown
				final Exception e = pie.getException();
				// print exact location of exception
				e.printStackTrace();
				throw new IllegalMiapeArgumentException("Error in SIR parallel iterator: " + e.getMessage());
			}
			// Reductors
			final Reduction<List<IdentifiedPeptide>> peptideListReduction = new Reduction<List<IdentifiedPeptide>>() {
				@Override
				public List<IdentifiedPeptide> reduce(List<IdentifiedPeptide> first, List<IdentifiedPeptide> second) {
					final List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
					peptides.addAll(first);
					peptides.addAll(second);
					return peptides;
				}
			};

			final Reduction<Map<String, IdentifiedProtein>> proteinHashReduction = new Reduction<Map<String, IdentifiedProtein>>() {
				@Override
				public Map<String, IdentifiedProtein> reduce(Map<String, IdentifiedProtein> first,
						Map<String, IdentifiedProtein> second) {
					final Map<String, IdentifiedProtein> map = new THashMap<String, IdentifiedProtein>();

					final List<Map<String, IdentifiedProtein>> listofmaps = new ArrayList<Map<String, IdentifiedProtein>>();
					listofmaps.add(first);
					listofmaps.add(second);
					for (final Map<String, IdentifiedProtein> mapToReduce : listofmaps) {

						for (final String proteinAcc : mapToReduce.keySet()) {
							if (!map.containsKey(proteinAcc)) {
								map.put(proteinAcc, mapToReduce.get(proteinAcc));
							} else {
								// Add peptides from protein2 to the protein1
								final IdentifiedProteinImpl protein = (IdentifiedProteinImpl) map.get(proteinAcc);
								final IdentifiedProtein protein2 = mapToReduce.get(proteinAcc);
								for (final IdentifiedPeptide peptide2 : protein2.getIdentifiedPeptides()) {
									protein.addIdentifiedPeptide(peptide2);

									// delete protein2 from peptide
									final IdentifiedPeptideImpl peptideImpl2 = (IdentifiedPeptideImpl) peptide2;
									final Iterator<IdentifiedProtein> iterator2 = peptideImpl2.getIdentifiedProteins()
											.iterator();
									while (iterator2.hasNext()) {
										if (iterator2.next().getId() == protein2.getId())
											iterator2.remove();
									}
									// add protein1 to peptide
									peptideImpl2.addProtein(protein);
								}
							}
						}
					}
					return map;
				}
			};
			log.info("Collapsing thread results");
			peptides.addAll(reduciblePeptides.reduce(peptideListReduction));
			proteinHash = reducibleProteinHash.reduce(proteinHashReduction);

			for (final String spectraDataRef : syncInputDataHash.keySet()) {
				if (inputDataSetMap.containsKey(spectraDataRef)) {
					final InputDataSet inputDataSet = inputDataSetMap.get(spectraDataRef);

					final Set<InputData> inputDatas = inputDataSet.getInputDatas();
					boolean include = true;
					for (final InputData inputData : inputDatas) {
						if (inputData.getName().equals(spectraDataRef))
							include = false;
					}
					if (include)
						inputDatas.add(syncInputDataHash.get(spectraDataRef));
				}
			}

			log.info(spectrumIdentificationResultList.size() + " SIR processed successfully in parallel in "
					+ (System.currentTimeMillis() - t1) / 1000 + " sg.");

			// Add all the proteins to the proteinSet
			for (final String protein_Acc : proteinHash.keySet()) {
				final IdentifiedProtein protein = proteinHash.get(protein_Acc);
				((ProteinSetImpl) proteinSet).addIdentifiedProtein(protein);
			}
			log.info("Parsed " + proteinHash.size() + " proteins and " + peptides.size() + " peptides.");
		}
		// add input data sets:
		for (final InputDataSet inputDataSet : inputDataSetMap.values()) {
			inputDataSets.add(inputDataSet);
		}

		// for the protein detection protocol and the peptide identification
		// detection protocol
		final Map<String, List<Object>> protocolsBySoftware = getProtocolsBySoftware(analysisProtocolCollection);
		if (protocolsBySoftware != null) {
			for (final List<Object> list : protocolsBySoftware.values()) {
				// Validations
				validations.add(new ValidationImpl(list, analysisSoftwareList.getAnalysisSoftware(),
						spectrumIdentificationSoftwareID, cvManager));
			}
		}

		// Add new validation softwares for the softwares not referenced by
		// protocols
		if (analysisSoftwareList != null) {
			final Set<String> referencesSoftwareRefs = getProtocolsBySoftwareIDs(analysisProtocolCollection);
			for (final AnalysisSoftware analysisSoftware : analysisSoftwareList.getAnalysisSoftware()) {
				final String analysisSoftwareID = analysisSoftware.getId();
				if (!referencesSoftwareRefs.contains(analysisSoftwareID)
						&& !spectrumIdentificationSoftwareID.equals(analysisSoftwareID)) {
					validations.add(new ValidationImpl(analysisSoftware, cvManager));
				}
			}
		}
		//
		if (existsSourceFile()) {
			// Get the location of the first file in the list. The rest of them,
			// put it in GeneratedFilesDescription
			final SourceFile file = sourceFiles.next();
			generatedFileURI = file.getLocation();
		}

	}

	private String getInputParameterKey(SpectrumIdentificationProtocol spectrumIdentProtocol,
			ProteinDetectionProtocol pdp, List<SearchDatabase> databaseListXML, Software msiSoftware,
			Long numSeqSearched) {
		final StringBuilder sb = new StringBuilder();
		if (spectrumIdentProtocol != null) {
			sb.append(spectrumIdentProtocol.getId());
		}
		if (pdp != null) {
			sb.append(pdp.getId());
		}
		if (databaseListXML != null) {
			sb.append(databaseListXML.size());
		}
		if (msiSoftware != null) {
			sb.append(msiSoftware.getId());
		}
		if (numSeqSearched != null) {
			sb.append(numSeqSearched);
		}
		final String inputParameterKey = sb.toString();
		return inputParameterKey;
	}

	private SpectrumIdentificationList getSpectrumIdentificationList(String spectrumIdentificationListId) {
		if (spectrumIdentificationListMap.containsKey(spectrumIdentificationListId)) {
			return spectrumIdentificationListMap.get(spectrumIdentificationListId);
		}
		if (silIterator == null) {
			log.info("Unmarshalling  the spectrumIdentificationList iterator");
			silIterator = mzIdentMLUnmarshaller
					.unmarshalCollectionFromXpath(MzIdentMLElement.SpectrumIdentificationList);
			log.info("SpectrumIdentificationList iterator unmarshalled");
		}
		while (silIterator.hasNext()) {
			log.debug("Unmarshalling  next SpectrumIdentificationList");
			final SpectrumIdentificationList sil = silIterator.next();
			log.debug("SpectrumIdentificationList " + sil.getId() + " unmarshalled");
			spectrumIdentificationListMap.put(sil.getId(), sil);
			if (sil.getId().equals(spectrumIdentificationListId)) {
				return sil;
			}
		}
		return null;
	}

	/**
	 * Main method that reads the mzIdentML file and create the MIAPE MSI sections
	 *
	 * @param mzIdentMLUnmarshaller
	 */
	/**
	 *
	 */
	private void processMzIdentML() {

		// clear static identifier counters
		MiapeXmlUtil.clearIdentifierCounters();
		String spectrumIdentificationSoftwareID = "";
		log.debug("unmarshalling sourceFiles");
		sourceFiles = mzIdentMLUnmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.SourceFile);
		if (sourceFiles.hasNext())
			sourceFilesCount = mzIdentMLUnmarshaller.getObjectCountForXpath(MzIdentMLElement.SourceFile.getXpath());
		log.debug("unmarshalling searchDatbases");
		searchDataBases = mzIdentMLUnmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.SearchDatabase);
		log.info("unmarshalling analysisSoftwareList");
		analysisSoftwareList = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisSoftwareList);
		log.debug("unmarshalling auditCollection");
		auditCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AuditCollection);
		if (auditCollection != null) {
			mzIdentContactList = auditCollection.getPersonOrOrganization();

		}

		log.debug("unmarshalling analysisProtocolCollection");
		analysisProtocolCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisProtocolCollection);
		log.debug("unmarshalling analysisSampleCollection");
		analysisSampleCollection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.AnalysisSampleCollection);
		log.debug("unmarshalling spectrumIdentification");

		// Set<String> spectrumIdentificationIDs = new THashSet<String>();
		// try {
		// spectrumIdentificationIDs = mzIdentMLUnmarshaller
		// .getIDsForElement(MzIdentMLElement.SpectrumIdentification);
		// } catch (ConfigurationException e1) {
		// e1.printStackTrace();
		// }

		final Iterator<SpectrumIdentification> spectrumIdentifications = mzIdentMLUnmarshaller
				.unmarshalCollectionFromXpath(MzIdentMLElement.SpectrumIdentification);
		// List<SpectrumIdentification> spectrumIdentification =
		// analysisCollection
		// .getSpectrumIdentification();

		initProteinHypothesisByPeptideEvidenceHashMap();
		log.info("Processing spectrumIdentifications");
		int i = 1;
		// for (SpectrumIdentification spectrumIdent : spectrumIdentifications)
		// {
		// create a protein set per proteinIdentificationlist

		while (spectrumIdentifications.hasNext()) {
			final Map<String, IdentifiedProtein> proteinHash = new THashMap<String, IdentifiedProtein>();

			final SpectrumIdentification spectrumIdent = spectrumIdentifications.next();
			log.info("spectrum identification " + i++ + " " + spectrumIdent.getId());

			final SpectrumIdentificationList spectrumIdentificationList = getSpectrumIdentificationList(
					spectrumIdent.getSpectrumIdentificationListRef());

			SpectrumIdentificationProtocol spectrumIdentProtocol = null;

			try {
				spectrumIdentProtocol = mzIdentMLUnmarshaller.unmarshal(SpectrumIdentificationProtocol.class,
						spectrumIdent.getSpectrumIdentificationProtocolRef());
			} catch (final JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			final List<SearchDatabase> databaseListXML = getSearchDatabases(spectrumIdent.getSearchDatabaseRef(),
					searchDataBases);

			// MSI Software

			final AnalysisSoftware softwareXML = spectrumIdentProtocol.getAnalysisSoftware();
			if (softwareXML != null)
				spectrumIdentificationSoftwareID = softwareXML.getId();

			final ProteinDetectionProtocol pdp = getProteinDetectionProtocol(
					spectrumIdent.getSpectrumIdentificationListRef());
			Software msiSoftware = null;
			if (softwareXML != null) {
				if (msiSoftwares.containsKey(softwareXML.getId())) {
					msiSoftware = msiSoftwares.get(softwareXML.getId());
				} else {
					final Integer softwareID = MiapeIdentifierCounter.increaseCounter();

					msiSoftware = new SoftwareImpl(softwareXML, softwareID, cvManager);
					msiSoftwares.put(softwareXML.getId(), msiSoftware);
				}
			}
			final Map<String, String> elementAttributes = mzIdentMLUnmarshaller.getElementAttributes(
					spectrumIdent.getSpectrumIdentificationListRef(), SpectrumIdentificationList.class);
			Long numSeqSearched = (long) -1;
			if (elementAttributes.containsKey("numSequencesSearched"))
				numSeqSearched = Long.valueOf(elementAttributes.get("numSequencesSearched"));

			final String inputParameterKey = getInputParameterKey(spectrumIdentProtocol, pdp, databaseListXML,
					msiSoftware, numSeqSearched);
			// Input parameter and databases
			InputParameter inputParameter = null;
			if (inputParameters.containsKey(inputParameterKey)) {
				inputParameter = inputParameters.get(inputParameterKey);
			} else {
				final Integer inputParamID = MiapeIdentifierCounter.increaseCounter();
				inputParameter = new InputParameterImpl(spectrumIdentProtocol, pdp, databaseListXML, msiSoftware,
						inputParamID, numSeqSearched, cvManager);
				inputParameters.put(inputParameterKey, inputParameter);
			}

			// inputDataSet
			final Integer inputDataSetID = MiapeIdentifierCounter.increaseCounter();
			final InputDataSet inputDataSet = new InputDataSetImpl(inputDataSetID);
			inputDataSets.add(inputDataSet);

			// proteinSet
			final IdentifiedProteinSet proteinSet = new ProteinSetImpl(inputParameter, inputDataSet);
			proteinSets.add(proteinSet);
			final Map<String, InputData> inputDataBySpectraDataRef = new THashMap<String, InputData>();
			// Map<spectraDataXML.ID, InputData

			// List<SpectrumIdentificationResult> spectrumIdentificationResult =
			// spectIdentListXML
			// .getSpectrumIdentificationResult();

			long peptideCount = 0;

			final Iterator<SpectrumIdentificationResult> spectrumIdentificationResultIterator = spectrumIdentificationList
					.getSpectrumIdentificationResult().iterator();
			log.info("processing SpectrumIdentificationResults in SIL " + spectrumIdentificationList.getId());
			while (spectrumIdentificationResultIterator.hasNext()) {
				final SpectrumIdentificationResult spectIdentResultXML = spectrumIdentificationResultIterator.next();
				final String RT = getRetentionTimeInSeconds(spectIdentResultXML);

				// TODO this is very important! Be careful.

				// final String spectrumID =
				// spectIdentResultXML.getSpectrumID();
				final String spectrumRef = parseSpectrumRef(spectIdentResultXML.getSpectrumID());

				// Input data
				// check if the spectraData is already captured. If not, add a
				// new input Data
				final String spectraDataRef = spectIdentResultXML.getSpectraDataRef();
				InputData inputData = null;
				try {
					if (!inputDataBySpectraDataRef.containsKey(spectraDataRef)) {
						final SpectraData spectraDataXML = mzIdentMLUnmarshaller.unmarshal(SpectraData.class,
								spectraDataRef);
						final Integer inputDataID = MiapeIdentifierCounter.increaseCounter();
						inputData = new InputDataImpl(spectraDataXML, inputDataID);
						inputDataBySpectraDataRef.put(spectraDataRef, inputData);
					} else {
						inputData = inputDataBySpectraDataRef.get(spectraDataRef);
					}
					((InputDataSetImpl) inputDataSet).addInputData(inputData);

				} catch (final JAXBException e1) {
					e1.printStackTrace();
				}
				// log.info(spectIdentResultXML.getSpectrumIdentificationItem().size()
				// + " SpectrumIdentificationItems");
				final Set<PeptideScore> scoresFromFirstPeptide = new THashSet<PeptideScore>();
				final List<SpectrumIdentificationItem> spectrumIdentificationItems = spectIdentResultXML
						.getSpectrumIdentificationItem();
				for (final SpectrumIdentificationItem spectIdentItemXML : spectrumIdentificationItems) {

					// some peptides contribute to proteins even if they
					// have
					// not passed the threshold
					// if (spectIdentItemXML.isPassThreshold()) {

					// CREATE Peptide
					final Integer peptideID = MiapeIdentifierCounter.increaseCounter();
					Peptide peptideXML = null;

					if (spectIdentItemXML.getPeptideRef() != null) {
						if (peptideXML == null) {
							try {
								peptideXML = mzIdentMLUnmarshaller.unmarshal(Peptide.class,
										spectIdentItemXML.getPeptideRef());
							} catch (final JAXBException e) {
								log.debug(e.getMessage());
							} catch (final IllegalArgumentException e) {
								log.debug(e.getMessage());
							}
						}
					}
					if (peptideXML == null) {
						peptideXML = getPeptideFromPeptideEvidences(spectIdentItemXML.getPeptideEvidenceRef());
					}
					if (peptideXML == null) {
						throw new IllegalMiapeArgumentException(
								"Peptide " + spectIdentItemXML.getPeptideRef() + " cannot be found in mzIdentML file");
					} else {

						boolean includePeptide = false;
						final Set<PeptideScore> scores = IdentifiedPeptideImpl
								.getScoresFromThisPeptide(spectIdentItemXML, peptideXML, cvManager);
						// if (scores == null || scores.isEmpty()) {
						// log.info("Skipping SII:" + spectIdentItemXML.getId()
						// + " because no scores have found");
						// continue;
						// }
						if (spectIdentItemXML.getRank() == 1) {
							includePeptide = true;
							scoresFromFirstPeptide.clear();
							scoresFromFirstPeptide.addAll(scores);
						} else {
							// if the rank 2 has the same scores, also
							// include it
							if (comparePeptideScores(scoresFromFirstPeptide, scores) == 0) {
								includePeptide = true;
								log.debug("Peptide with rank " + spectIdentItemXML.getRank()
										+ " is going to be included");
							}
						}
						if (!includePeptide) {
							break;
						} else {
							final IdentifiedPeptide peptide = new IdentifiedPeptideImpl(spectIdentItemXML, peptideXML,
									inputData, spectrumRef, peptideID, cvManager,
									new MapSync<String, ProteinDetectionHypothesis>(
											proteinDetectionHypotesisWithPeptideEvidence),
									proteinHash, RT);
							// if (peptide.getScores() == null ||
							// peptide.getScores().isEmpty())
							// throw new IllegalMiapeArgumentException(
							// "The peptide from SII:" +
							// spectIdentItemXML.getId() + " has no scores!");
							// Add the peptide to the peptide list
							peptides.add(peptide);
							peptideCount++;

						}

					}
				}
			}
			// Add all the proteins to the proteinSet
			for (final String protein_Acc : proteinHash.keySet()) {
				final IdentifiedProtein protein = proteinHash.get(protein_Acc);
				((ProteinSetImpl) proteinSet).addIdentifiedProtein(protein);
			}
			log.info("Parsed " + proteinHash.size() + " proteins and " + peptideCount + " peptides.");
		}

		// for the protein detection protocol and the peptide identification
		// detection protocol
		final Map<String, List<Object>> protocolsBySoftware = getProtocolsBySoftware(analysisProtocolCollection);
		if (protocolsBySoftware != null) {
			for (final List<Object> list : protocolsBySoftware.values()) {
				// Validations
				validations.add(new ValidationImpl(list, analysisSoftwareList.getAnalysisSoftware(),
						spectrumIdentificationSoftwareID, cvManager));
			}
		}

		// Add new validation softwares for the softwares not referenced by
		// protocols
		if (analysisSoftwareList != null) {
			final Set<String> referencesSoftwareRefs = getProtocolsBySoftwareIDs(analysisProtocolCollection);
			for (final AnalysisSoftware analysisSoftware : analysisSoftwareList.getAnalysisSoftware()) {
				final String analysisSoftwareID = analysisSoftware.getId();
				if (!referencesSoftwareRefs.contains(analysisSoftwareID)
						&& !spectrumIdentificationSoftwareID.equals(analysisSoftwareID)) {
					validations.add(new ValidationImpl(analysisSoftware, cvManager));
				}
			}
		}
		//
		if (existsSourceFile()) {
			// Get the location of the first file in the list. The rest of them,
			// put it in GeneratedFilesDescription
			final SourceFile file = sourceFiles.next();
			generatedFileURI = file.getLocation();
		}

	}

	private int comparePeptideScores(Set<PeptideScore> scoresFromFirstPeptide, Set<PeptideScore> scores) {
		if (scoresFromFirstPeptide != null && scores != null) {
			if (scores.size() == scoresFromFirstPeptide.size()) {
				for (final PeptideScore peptideScore : scores) {
					if (!foundPeptideScore(peptideScore, scoresFromFirstPeptide))
						return -1;
				}
				return 0;
			}
		}
		return -1;
	}

	private boolean foundPeptideScore(PeptideScore peptideScore, Set<PeptideScore> scoresFromFirstPeptide) {
		for (final PeptideScore peptideScore2 : scoresFromFirstPeptide) {
			if (peptideScore2.getName().equals(peptideScore.getName()))
				if (peptideScore2.getValue().equals(peptideScore.getValue()))
					return true;
		}
		return false;
	}

	private String getRetentionTimeInSeconds(SpectrumIdentificationResult spectIdentResultXML) {
		if (spectIdentResultXML != null) {
			if (spectIdentResultXML.getParamGroup() != null) {
				for (final AbstractParam paramType : spectIdentResultXML.getParamGroup()) {
					if (paramType instanceof CvParam) {
						final CvParam cvparam = (CvParam) paramType;
						final ControlVocabularyTerm cvTerm = RetentionTime.getInstance(cvManager)
								.getCVTermByAccession(new Accession(cvparam.getAccession()));
						if (cvTerm != null) {
							if (cvparam.getValue() != null) {
								try {
									double num = Double.valueOf(cvparam.getValue());
									// check unit
									final String unitAccession = cvparam.getUnitAccession();
									if (unitAccession != null) {
										if ("UO:0000010".equals(unitAccession)) {
											log.debug("Retention time in seconds: " + num);
										} else if ("UO:0000031".equals(unitAccession)) {
											log.debug("Retention time in minutes: " + num);
											num = num * 60;
											log.debug("Retention time converted to seconds: " + num);
										}
									}
									return String.valueOf(num);
								} catch (final NumberFormatException e) {

								}
							}
						}
					}
				}
			}
		}
		final List<SpectrumIdentificationItem> spectrumIdentificationItems = spectIdentResultXML
				.getSpectrumIdentificationItem();
		if (spectrumIdentificationItems != null) {
			for (final SpectrumIdentificationItem spectrumIdentificationItem : spectrumIdentificationItems) {
				if (spectrumIdentificationItem.getParamGroup() != null) {
					for (final AbstractParam paramType : spectrumIdentificationItem.getParamGroup()) {
						if (paramType instanceof CvParam) {
							final CvParam cvparam = (CvParam) paramType;
							final ControlVocabularyTerm cvTerm = RetentionTime.getInstance(cvManager)
									.getCVTermByAccession(new Accession(cvparam.getAccession()));
							if (cvTerm != null) {
								if (cvparam.getValue() != null) {
									try {
										double num = Double.valueOf(cvparam.getValue());
										// check unit
										final String unitAccession = cvparam.getUnitAccession();
										if (unitAccession != null) {
											if ("UO:0000010".equals(unitAccession)) {
												log.debug("Retention time in seconds: " + num);
											} else if ("UO:0000031".equals(unitAccession)) {
												log.debug("Retention time in minutes: " + num);
												num = num * 60;
												log.debug("Retention time converted to seconds: " + num);
											}
										}
										return String.valueOf(num);
									} catch (final NumberFormatException e) {

									}
								}
							}
						}
					}

				}
			}
		}
		return null;
	}

	private ProteinDetectionProtocol getProteinDetectionProtocol(String spectrumIdentificationListRef) {

		final ProteinDetection proteinDetection = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.ProteinDetection);
		if (proteinDetection != null) {
			final List<InputSpectrumIdentifications> inputSpectrumIdentifications = proteinDetection
					.getInputSpectrumIdentifications();
			for (final InputSpectrumIdentifications inputSpectrumIdentification : inputSpectrumIdentifications) {
				if (inputSpectrumIdentification.getSpectrumIdentificationListRef()
						.equals(spectrumIdentificationListRef))
					try {
						return mzIdentMLUnmarshaller.unmarshal(ProteinDetectionProtocol.class,
								proteinDetection.getProteinDetectionProtocolRef());
					} catch (final JAXBException e) {

						e.printStackTrace();
					}
			}
		}
		return null;
	}

	private Map<String, List<Object>> getProtocolsBySoftware(AnalysisProtocolCollection analysisProtocolCollection2) {
		final Map<String, List<Object>> ret = new THashMap<String, List<Object>>();
		if (analysisProtocolCollection2 != null) {
			final List<SpectrumIdentificationProtocol> spectrumIdentificationProtocol = analysisProtocolCollection2
					.getSpectrumIdentificationProtocol();
			if (spectrumIdentificationProtocol != null) {
				for (final SpectrumIdentificationProtocol sip : spectrumIdentificationProtocol) {
					final String analysisSoftwareRef = sip.getAnalysisSoftwareRef();
					if (ret.containsKey(analysisSoftwareRef))
						ret.get(analysisSoftwareRef).add(sip);
					else {
						final List<Object> list = new ArrayList<Object>();
						list.add(sip);
						ret.put(analysisSoftwareRef, list);
					}
				}
			}
			final ProteinDetectionProtocol proteinDetectionProtocol = analysisProtocolCollection2
					.getProteinDetectionProtocol();
			if (proteinDetectionProtocol != null) {
				final String analysisSoftwareRef = proteinDetectionProtocol.getAnalysisSoftwareRef();
				if (ret.containsKey(analysisSoftwareRef))
					ret.get(analysisSoftwareRef).add(proteinDetectionProtocol);
				else {
					final List<Object> list = new ArrayList<Object>();
					list.add(proteinDetectionProtocol);
					ret.put(analysisSoftwareRef, list);
				}
			}
			return ret;
		}
		return null;
	}

	private Set<String> getProtocolsBySoftwareIDs(AnalysisProtocolCollection analysisProtocolCollection2) {
		final Set<String> ret = new THashSet<String>();
		if (analysisProtocolCollection2 != null) {
			final List<SpectrumIdentificationProtocol> spectrumIdentificationProtocol = analysisProtocolCollection2
					.getSpectrumIdentificationProtocol();
			if (spectrumIdentificationProtocol != null) {
				for (final SpectrumIdentificationProtocol sip : spectrumIdentificationProtocol) {
					final String analysisSoftwareRef = sip.getAnalysisSoftwareRef();

					ret.add(analysisSoftwareRef);
				}
			}
			final ProteinDetectionProtocol proteinDetectionProtocol = analysisProtocolCollection2
					.getProteinDetectionProtocol();
			if (proteinDetectionProtocol != null) {
				final String analysisSoftwareRef = proteinDetectionProtocol.getAnalysisSoftwareRef();
				ret.add(analysisSoftwareRef);
			}
			return ret;
		}
		return null;
	}

	/**
	 * Take the first peptideEvidence referenced by the peptideEvidenceRefs coming
	 * from the SpectrumIdentificationItem
	 *
	 * @param peptideEvidenceRefs
	 * @return
	 */
	private Peptide getPeptideFromPeptideEvidences(List<PeptideEvidenceRef> peptideEvidenceRefs) {
		if (peptideEvidenceRefs != null && !peptideEvidenceRefs.isEmpty()) {
			for (final PeptideEvidenceRef peptideEvidenceRef : peptideEvidenceRefs) {
				final PeptideEvidence peptideEvidence = peptideEvidenceRef.getPeptideEvidence();
				if (peptideEvidence != null) {
					if (peptideEvidence.getPeptide() != null)
						return peptideEvidence.getPeptide();

					final String peptideRef = peptideEvidence.getPeptideRef();
					try {
						return mzIdentMLUnmarshaller.unmarshal(Peptide.class, peptideRef);
					} catch (final JAXBException e) {
						log.info("JAXBException for unmarshal Peptide with ID:" + peptideRef);
					} catch (final NumberFormatException e) {
						log.info("Number format exception for unmarshal Peptide with ID:" + peptideRef);
					} catch (final IllegalArgumentException e) {
						log.info("IllegalArgumentException for unmarshal Peptide with ID:" + peptideRef);
					}
				}
			}

		}
		return null;
	}

	/**
	 * Returns the spectrumRef of the spectrumIdentificationResult
	 *
	 * @param spectrumID                        : If the mzIdentML comes from a
	 *                                          MASCOT mgf search, the spectrumID
	 *                                          should be "index=x" where x is the
	 *                                          order of the spectra in the MGF
	 *                                          (starting by 0).<br>
	 *                                          If the mzIdentML comes from a MASCOT
	 *                                          mzML search, the spectrumID should
	 *                                          start by "mzMLid=...". In this case,
	 *                                          return the number of SIR as a
	 *                                          spectrumRef, since the mzML spectra
	 *                                          will be readed in parent mass order,
	 *                                          and the mzIdentML presents the SIR
	 *                                          in parent mass order.
	 *
	 *                                          the scan is the order of the spectra
	 *                                          in the mzML (starting by 1)
	 * @param spectrumIdentificationResultIndex
	 * @return
	 */
	private String parseSpectrumRef(String spectrumID) {
		if (spectrumID == null)
			return null;
		Integer specRefInt = 0;
		final String mzMLSpectrumIDRegexp = "^mzMLid=(.*)$";

		if (Pattern.matches(mzMLSpectrumIDRegexp, spectrumID)) {
			// in case of being a spectrumID of a mzML file, return the
			// string that appears after the "mzMLid="
			final Pattern p = Pattern.compile(mzMLSpectrumIDRegexp);
			final Matcher m = p.matcher(spectrumID);
			if (m.find())
				return m.group(1);

		}
		final String mgfSpectrumIDRegexp = "^index=(\\d+)$";
		final String mzMLSpectrumIDRegexp2 = ".*scan=(\\d+)$";
		final String mgfSpectrumIDRegexpQuery = "^query=(\\d+)$";
		final String mgfSpectrumIDRegexpQuery2 = "^(\\d+)$";
		final String mgfPMEQCIDRegexp = "^.*\\.\\d+\\.\\d+\\.\\d+$"; // spectrumID='OrbiVL1A01.13334.13334.2'

		// if is like "index=1235"
		if (Pattern.matches(mgfSpectrumIDRegexp, spectrumID)) {
			final Pattern p = Pattern.compile(mgfSpectrumIDRegexp);
			final Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1)) + 1; // sum 1
																// (starts
																// by 0)
			}
			// if it is like
			// "mzMLid=controllerType=0 controllerNumber=1 scan=3423"
		} else if (Pattern.matches(mzMLSpectrumIDRegexp2, spectrumID)) {
			final Pattern p = Pattern.compile(mzMLSpectrumIDRegexp2);
			final Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1)); // don't sum 1
															// (starts
															// by 1)
			}
		} else if (Pattern.matches(mgfSpectrumIDRegexpQuery, spectrumID)) {
			final Pattern p = Pattern.compile(mgfSpectrumIDRegexpQuery);
			final Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1));
			}
		} else if (Pattern.matches(mgfSpectrumIDRegexpQuery2, spectrumID)) {
			final Pattern p = Pattern.compile(mgfSpectrumIDRegexpQuery2);
			final Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1));
			}
		} else if (Pattern.matches(mgfPMEQCIDRegexp, spectrumID)) { // spectrumID='OrbiVL1A01.13334.13334.2'
			final Pattern p = Pattern.compile(mgfPMEQCIDRegexp);
			final Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				return spectrumID;
			}
		}

		if (specRefInt != 0)
			return specRefInt.toString();
		return null;
	}

	/**
	 * Here the relationship between the proteins and the peptides is captured
	 * iterating the ProteinDetectionHypothesises and getting the PeptideHypotheisis
	 * in there.
	 */
	protected void initProteinHypothesisByPeptideEvidenceHashMap() {
		log.info("Keeping relationships between ProteinDetectionHypothesis and peptideEvidences");
		log.info("Unmarshalling ProteinDetectionHypotesis");
		final long t1 = System.currentTimeMillis();
		final Iterator<ProteinDetectionHypothesis> proteinDetectionHypothesises = mzIdentMLUnmarshaller
				.unmarshalCollectionFromXpath(MzIdentMLElement.ProteinDetectionHypothesis);
		if (proteinDetectionHypothesises != null) {
			while (proteinDetectionHypothesises.hasNext()) {
				final ProteinDetectionHypothesis proteinHypothesisXML = proteinDetectionHypothesises.next();
				if (!pdhDBSeqMap.containsKey(proteinHypothesisXML)) {
					final DBSequence dbSequence = proteinHypothesisXML.getDBSequence();
					pdhDBSeqMap.put(proteinHypothesisXML, dbSequence);
				}
				for (final PeptideHypothesis peptideHypothesisXML : proteinHypothesisXML.getPeptideHypothesis()) {
					if (!proteinDetectionHypotesisWithPeptideEvidence
							.containsKey(peptideHypothesisXML.getPeptideEvidenceRef())) {
						proteinDetectionHypotesisWithPeptideEvidence.put(peptideHypothesisXML.getPeptideEvidenceRef(),
								proteinHypothesisXML);
					}
					final List<SpectrumIdentificationItemRef> spectrumIdentificationItemRefs = peptideHypothesisXML
							.getSpectrumIdentificationItemRef();
					for (final SpectrumIdentificationItemRef spectrumIdentificationItemRef : spectrumIdentificationItemRefs) {
						final String siiID = spectrumIdentificationItemRef.getSpectrumIdentificationItemRef();
						if (!spectrumIdentificationItemsByPeptideEvidence
								.containsKey(peptideHypothesisXML.getPeptideEvidenceRef())) {
							spectrumIdentificationItemsByPeptideEvidence
									.put(peptideHypothesisXML.getPeptideEvidenceRef(), new THashSet<String>());
						}
						spectrumIdentificationItemsByPeptideEvidence.get(peptideHypothesisXML.getPeptideEvidenceRef())
								.add(siiID);
					}
				}
			}

		}
		log.info("Finished keeping relationships between ProteinDetectionHypothesis and peptideEvidences in "
				+ (System.currentTimeMillis() - t1) + " milliseconds");

	}

	/**
	 * Here the relationship between the proteins and the peptides is captured
	 * iterating the ProteinDetectionHypothesises and getting the PeptideHypotheisis
	 * in there.
	 */
	protected void initProteinHypothesisByPeptideEvidenceHashMapInParallel() {
		final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		final int proteinDetectionHypothesisCount = mzIdentMLUnmarshaller
				.getObjectCountForXpath(MzIdentMLElement.ProteinDetectionHypothesis.getXpath());
		log.info("Keeping relationships between " + proteinDetectionHypothesisCount
				+ " ProteinDetectionHypothesis and peptideEvidences using " + threadCount + " cores");
		log.info("Unmarshalling ProteinDetectionHypotesis");
		final long t1 = System.currentTimeMillis();
		final Iterator<ProteinDetectionHypothesis> proteinDetectionHypothesises = mzIdentMLUnmarshaller
				.unmarshalCollectionFromXpath(MzIdentMLElement.ProteinDetectionHypothesis);

		final ParIterator<ProteinDetectionHypothesis> iterator = ParIteratorFactory.createParIterator(
				proteinDetectionHypothesises, proteinDetectionHypothesisCount, threadCount, Schedule.GUIDED);

		final Reducible<Map<String, ProteinDetectionHypothesis>> reducibleProteinHash = new Reducible<Map<String, ProteinDetectionHypothesis>>();

		final List<ProteinDetectionHypothesisParallelProcesser> runners = new ArrayList<ProteinDetectionHypothesisParallelProcesser>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			final ProteinDetectionHypothesisParallelProcesser runner = new ProteinDetectionHypothesisParallelProcesser(
					iterator, numCore, reducibleProteinHash);
			runners.add(runner);
			runner.start();
		}

		// Main thread waits for worker threads to complete
		for (int k = 0; k < threadCount; k++) {
			try {
				runners.get(k).join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (iterator.getAllExceptions().length > 0) {
			throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
		}
		// Reductors
		final Reduction<Map<String, ProteinDetectionHypothesis>> pdhReduction = new Reduction<Map<String, ProteinDetectionHypothesis>>() {
			@Override
			public Map<String, ProteinDetectionHypothesis> reduce(Map<String, ProteinDetectionHypothesis> first,
					Map<String, ProteinDetectionHypothesis> second) {
				final Map<String, ProteinDetectionHypothesis> ret = new THashMap<String, ProteinDetectionHypothesis>();
				for (final String key : first.keySet()) {
					if (!ret.containsKey(key))
						ret.put(key, first.get(key));
				}
				for (final String key : second.keySet()) {
					if (!ret.containsKey(key))
						ret.put(key, second.get(key));
				}
				return ret;
			}
		};
		proteinDetectionHypotesisWithPeptideEvidence = reducibleProteinHash.reduce(pdhReduction);

		log.info("Finished keeping relationships between ProteinDetectionHypothesis and "
				+ proteinDetectionHypotesisWithPeptideEvidence.size() + " peptideEvidences in "
				+ (System.currentTimeMillis() - t1) + " milliseconds");

	}

	// protected static PSIPIAnalysisSearchDBSequenceType getDBSequence(String
	// dbSequenceRef,
	// List<PSIPIAnalysisSearchDBSequenceType> dbSequences) {
	// for (PSIPIAnalysisSearchDBSequenceType dbSequenceXML : dbSequences) {
	// if (dbSequenceXML.getId().equals(dbSequenceRef)) {
	// return dbSequenceXML;
	// }
	// }
	// return null;
	// }

	//
	// private PSIPISpectraSpectraDataType getSpectraData(String spectraDataRef,
	// List<PSIPISpectraSpectraDataType> spectraDatas) {
	// for (PSIPISpectraSpectraDataType spectraData : spectraDatas) {
	// if (spectraData.getId().equals(spectraDataRef)) {
	// return spectraData;
	// }
	// }
	// return null;
	// }

	// private SpectrumIdentificationList getSpectrumIdentificationList(
	// String spectrumIdentificationListRef) {
	// try {
	// final SpectrumIdentificationList spectrumIdentificationList =
	// mzIdentMLUnmarshaller
	// .unmarshal(SpectrumIdentificationList.class,
	// spectrumIdentificationListRef);
	// return spectrumIdentificationList;
	// } catch (JAXBException e) {
	// log.warn("spectrumIdentificationList ID="
	// + spectrumIdentificationListRef + " not found");
	// }
	// return null;
	// }

	//
	// private AnalysisSoftware getSoftware(String analysisSoftwareRef,
	// List<AnalysisSoftware> analysisSoftwareList) {
	// if (analysisSoftwareList != null)
	// for (AnalysisSoftware softwareXML : analysisSoftwareList) {
	// if (softwareXML.getId().equals(analysisSoftwareRef)) {
	// return softwareXML;
	// }
	// }
	// return null;
	// }

	/**
	 * Get a List of PSIPIAnalysisSearchSearchDatabaseType from an input list
	 * looking for the references in searchDatabaseRefList
	 *
	 * @param searchDatabaseRefs list of references
	 * @param searchDataBases    input list
	 * @return
	 */
	private List<SearchDatabase> getSearchDatabases(List<SearchDatabaseRef> searchDatabaseRefs,
			Iterator<SearchDatabase> searchDataBases) {
		final List<SearchDatabase> ret = new ArrayList<SearchDatabase>();

		for (final SearchDatabaseRef searchDatabaseRef : searchDatabaseRefs) {
			while (searchDataBases.hasNext()) {
				final SearchDatabase database = searchDataBases.next();
				if (searchDatabaseRef.getSearchDatabaseRef().equals(database.getId())) {
					ret.add(database);
				}
			}
		}
		return ret;
	}

	// private SpectrumIdentificationProtocol
	// getSpectrumIdentificationProtocol(String id,
	// List<SpectrumIdentificationProtocol> spectrumIdentProtocol) {
	// for (SpectrumIdentificationProtocol specIdentProtocol :
	// spectrumIdentProtocol) {
	// if (specIdentProtocol.getId().equals(id))
	// return specIdentProtocol;
	// }
	// return null;
	// }

	public void setDatabaseManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	public String getGeneratedFilesDescription() {
		final StringBuilder sb = new StringBuilder();
		if (existsSourceFile() == false) {
			return null;
		}
		int counter = 1;

		while (sourceFiles.hasNext()) {
			final SourceFile file = sourceFiles.next();

			if (file.getName() != null) {
				sb.append(Utils.SOURCE_FILE_NAME + "=" + file.getName());
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			if (file.getLocation() != null) {
				sb.append(Utils.LOCATION + "=" + file.getLocation());
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			if (file.getExternalFormatDocumentation() != null) {
				sb.append(Utils.EXTERNAL_FORMAT_DOCUMENTATION + "=" + file.getExternalFormatDocumentation());
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			if (file.getFileFormat() != null) {
				final uk.ac.ebi.jmzidml.model.mzidml.FileFormat fileFormat = file.getFileFormat();
				fileFormat.getCvParam();
				sb.append(Utils.FILE_FORMAT + "=");
				sb.append(org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory
						.readEntireParam(fileFormat.getCvParam()));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			final List<AbstractParam> paramGroup = file.getParamGroup();
			for (final AbstractParam param : paramGroup) {
				org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory
						.readEntireParam(param);
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}

			if (counter < sourceFilesCount) {
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			counter++;
		}
		if (sb.length() > 1000)
			return sb.substring(0, 1000);
		return sb.toString();
	}

	@Override
	public String getGeneratedFilesURI() {
		return generatedFileURI;

	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {

		return proteinSets;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		return inputParameters.values().stream().collect(Collectors.toSet());
	}

	@Override
	public Set<Software> getSoftwares() {
		return msiSoftwares.values().stream().collect(Collectors.toSet());
	}

	@Override
	public int getMSDocumentReference() {

		return msDocumentID;
	}

	@Override
	public Set<Validation> getValidations() {

		return validations;
	}

	@Override
	public MSContact getContact() {
		final Provider provider = mzIdentMLUnmarshaller.unmarshal(MzIdentMLElement.Provider);

		if (provider != null) {
			if (mzIdentMLUnmarshaller != null) {
				return new ContactImpl(provider, auditCollection, mzIdentContactList, user);
			}
		}
		return null;
	}

	@Override
	public MiapeDate getDate() {
		final Map<String, String> creationDateMap = mzIdentMLUnmarshaller
				.getElementAttributes(mzIdentMLUnmarshaller.getMzIdentMLId(), MzIdentML.class);
		if (creationDateMap != null && !creationDateMap.isEmpty())
			return new MiapeDate(creationDateMap.get("creationDate"));
		return new MiapeDate(new Date(System.currentTimeMillis()));
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Date getModificationDate() {
		return new Date(System.currentTimeMillis());
	}

	@Override
	public String getName() {
		if (mzIdentMLFileName != null)
			return mzIdentMLFileName;
		return "MIAPE MSI from mzIdentML file";
	}

	@Override
	public User getOwner() {
		return user;
	}

	@Override
	public Project getProject() {
		if (id > 0 && dbManager != null) {
			try {
				return dbManager.getMiapeMSIPersistenceManager()
						.getMiapeById(id, user.getUserName(), user.getPassword()).getProject();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return new ProjectImpl(projectName, user, dbManager);
	}

	@Override
	public Boolean getTemplate() {
		return Boolean.FALSE;
	}

	@Override
	public String getVersion() {
		return mzIdentMLUnmarshaller.getMzIdentMLVersion();
	}

	@Override
	public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager == null)
			throw new MiapeDatabaseException("The persistance method is not defined.");
		if (id > 0) {
			dbManager.getMiapeMSIPersistenceManager().deleteById(id, userName, password);
		} else
			throw new MiapeDatabaseException("The MIAPE is not stored yet!");
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager == null)
			throw new MiapeDatabaseException("The persistance method is not defined.");
		id = dbManager.getMiapeMSIPersistenceManager().store(this);

		return id;
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml() {
		return MiapeMSIXmlFactory.getFactory().toXml(this, cvManager);
	}

	private boolean existsSourceFile() {

		if (sourceFiles == null || !sourceFiles.hasNext())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MSIDocumentFromMzIndentML [" + " getContact()=" + getContact() + ", getDate()=" + getDate()
				+ ", getGeneratedFilesDescription()=" + getGeneratedFilesDescription() + ", getGeneratedFilesUrl()="
				+ getGeneratedFilesURI() + ", getId()=" + getId() + ", getIdentifiedProteinSets()="
				+ getIdentifiedProteinSets() + ", getInputDataSets()=" + getInputDataSets() + ", getInputParameters()="
				+ getInputParameters() + ", getModificationDate()=" + getModificationDate() + ", getMsiSoftwares()="
				+ getSoftwares() + ", getName()=" + getName() + ", getOwner()=" + getOwner() + ", getPrideUrl()="
				+ getAttachedFileLocation() + ", getProject()=" + getProject() + ", getReferencedMSDocument()="
				+ getMSDocumentReference() + ", getTemplate()=" + getTemplate() + ", getValidations()="
				+ getValidations() + ", getVersion()=" + getVersion() + "]";
	}

	// private void initPepEvidenceMap(
	// Map<String, PSIPIPolypeptidePeptideType> mapPeptides,
	// Map<String, PSIPIAnalysisSearchSpectrumIdentificationItemType>
	// mapEvidenceSpectrum,
	// Map<PSIPIPolypeptidePeptideType,
	// PSIPIAnalysisSearchSpectrumIdentificationItemType>
	// mapPeptideSpectrum,
	// Map<String, PSIPIAnalysisSearchDBSequenceType> mapEvidenceProtein,
	// Map<String, PSIPIAnalysisSearchDBSequenceType> mapProteins) {
	// for (PSIPIAnalysisSearchSpectrumIdentificationListType
	// spectrumIdentificationList :
	// mzIdentMLUnmarshaller
	// .getDataCollection().getAnalysisData().getSpectrumIdentificationList()) {
	// for (PSIPIAnalysisSearchSpectrumIdentificationResultType spectrumResult :
	// spectrumIdentificationList
	// .getSpectrumIdentificationResult()) {
	// List<PSIPIAnalysisSearchSpectrumIdentificationItemType> spectra =
	// spectrumResult
	// .getSpectrumIdentificationItem();
	// for (PSIPIAnalysisSearchSpectrumIdentificationItemType spectrum :
	// spectra) {
	//
	// PSIPIPolypeptidePeptideType peptide =
	// mapPeptides.get(spectrum.getPeptideRef());
	// mapPeptideSpectrum.put(peptide, spectrum);
	// List<PSIPIAnalysisProcessPeptideEvidenceType> peptideEvidences = spectrum
	// .getPeptideEvidence();
	// for (PSIPIAnalysisProcessPeptideEvidenceType evidence : peptideEvidences)
	// {
	// mapEvidenceSpectrum.put(evidence.getId(), spectrum);
	// if (mapProteins.containsKey(evidence.getDBSequenceRef())) {
	// PSIPIAnalysisSearchDBSequenceType protein = mapProteins.get(evidence
	// .getDBSequenceRef());
	// mapEvidenceProtein.put(evidence.getId(), protein);
	// }
	// }
	// }
	// }
	// }
	//
	// }

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		final Set<MSIAdditionalInformation> addInfos = new THashSet<MSIAdditionalInformation>();

		// Capture information about the samples
		if (analysisSampleCollection != null) {
			for (final Sample sampleXML : analysisSampleCollection.getSample()) {

				addInfos.add(new AdditionalInformationImpl(sampleXML));
			}
		}

		// Capture information from the biobliographic references
		final Iterator<BibliographicReference> bibliographicReferences = mzIdentMLUnmarshaller
				.unmarshalCollectionFromXpath(MzIdentMLElement.BibliographicReference);
		if (bibliographicReferences != null) {
			while (bibliographicReferences.hasNext()) {
				final BibliographicReference bibliographicReference = bibliographicReferences.next();
				addInfos.add(new AdditionalInformationImpl(bibliographicReference));

			}
		}

		if (addInfos.size() > 0)
			return addInfos;
		return null;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeMSIValidator.getInstance().getReport(this);
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return inputDataSets;
	}

	@Override
	public String getAttachedFileLocation() {
		return url;
	}

	public void setAttachedFileURL(String fileURL) {
		final File file = new File(fileURL);
		// convert to local url
		if (file.exists()) {
			try {
				fileURL = file.toURI().toURL().toString();
			} catch (final MalformedURLException e) {
			}
		}
		url = fileURL;
		// also add it to the generated file
		setGeneratedFileURI(fileURL);

	}

	public void setGeneratedFileURI(String generatedFileURI) {
		this.generatedFileURI = generatedFileURI;

	}

	@Override
	public void setReferencedMSDocument(int msDocumentID) {
		this.msDocumentID = msDocumentID;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return peptides;
	}

	@Override
	public void setId(int id) {
		this.id = id;

	}

}
