package org.proteored.miapeapi.xml.xtandem.msi;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.IntermediateFileFormat;
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
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.ProjectImpl;
import org.proteored.miapeapi.xml.util.MiapeIdentifierCounter;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import de.proteinms.xtandemparser.parser.XTandemParser;
import de.proteinms.xtandemparser.xtandem.Domain;
import de.proteinms.xtandemparser.xtandem.InputParams;
import de.proteinms.xtandemparser.xtandem.ModificationMap;
import de.proteinms.xtandemparser.xtandem.Peptide;
import de.proteinms.xtandemparser.xtandem.PeptideMap;
import de.proteinms.xtandemparser.xtandem.PerformParams;
import de.proteinms.xtandemparser.xtandem.Protein;
import de.proteinms.xtandemparser.xtandem.ProteinMap;
import de.proteinms.xtandemparser.xtandem.Spectrum;
import de.proteinms.xtandemparser.xtandem.XTandemFile;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class MiapeMsiDocumentImpl implements MiapeMSIDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final PersistenceManager dbManager;
	private final ControlVocabularyManager cvManager;
	private final Set<InputParameter> inputParameters = new THashSet<InputParameter>();
	private final Set<InputDataSet> inputDataSets = new THashSet<InputDataSet>();
	private final Set<Validation> validations = new THashSet<Validation>();
	private final Set<Software> softwares = new THashSet<Software>();
	private final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();
	private String fileLocation;
	private final String xtandemXMLFileName;
	private final User owner;

	private String projectName;

	private int id;

	private MiapeDate miapeDate = null;

	private String url = null;

	private int referencedMS;
	private Map<String, List<IdentifiedPeptide>> peptidesByProteinIDHash;
	private Map<String, IdentifiedProtein> proteinHash;

	public MiapeMsiDocumentImpl(XTandemFile xfile, ControlVocabularyManager cvManager, String xtandemXMLFileName,
			String projectName) {
		fileLocation = xfile.getFileName();
		owner = null;
		dbManager = null;
		this.projectName = projectName;
		this.cvManager = cvManager;
		this.xtandemXMLFileName = xtandemXMLFileName;
		processXTandemFile(xfile);
	}

	public MiapeMsiDocumentImpl(XTandemFile xfile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String user, String password, String xtandemXMLFileName,
			String projectName) throws MiapeDatabaseException, MiapeSecurityException {

		fileLocation = xfile.getFileName();
		owner = databaseManager.getUser(user, password);
		this.projectName = projectName;
		this.projectName = projectName;
		dbManager = databaseManager;
		this.cvManager = cvManager;
		this.xtandemXMLFileName = xtandemXMLFileName;
		processXTandemFile(xfile);
	}

	private void processXTandemFile(XTandemFile xfile) {
		// clear static identifier counters
		MiapeXmlUtil.clearIdentifierCounters();
		XTandemParser parser = null;
		PerformParams performParameters = null;
		InputParams xmlInputParameters = null;
		try {
			parser = xfile.getXTandemParser();
			performParameters = xfile.getPerformParameters();
			xmlInputParameters = xfile.getInputParameters();
		} catch (final NullPointerException ex) {

		}

		Software software = null;
		if (performParameters != null) {
			// Softwares
			final int softwareID = MiapeIdentifierCounter.increaseCounter();
			software = new XTandemSoftwareImpl(performParameters.getProcVersion(), softwareID, cvManager);
			softwares.add(software);
		}

		InputParameter inputParameter = null;
		if (parser != null) {
			// input parameters
			final Integer inputParamID = MiapeIdentifierCounter.increaseCounter();
			inputParameter = new InputParameterImpl(xfile, parser, inputParamID, software, cvManager);
			inputParameters.add(inputParameter);
		}

		if (xmlInputParameters != null) {
			// inputDataSet
			if (xmlInputParameters != null) {
				final String spectrumPath = xmlInputParameters.getSpectrumPath();
				if (spectrumPath != null && !"".equals(spectrumPath)) {
					final Integer inputDataSetID = MiapeIdentifierCounter.increaseCounter();
					final InputDataSet inputDataSet = new InputDataSetImpl(spectrumPath, inputDataSetID);
					inputDataSets.add(inputDataSet);
				}
			}
		}

		// Validations
		long falsePositives = -1;
		if (performParameters != null) {
			falsePositives = performParameters.getEstFalsePositives();
		}
		double maxValidExpectValue = -1;
		if (xmlInputParameters != null)
			maxValidExpectValue = xmlInputParameters.getMaxValidExpectValue();
		if (falsePositives != -1 && maxValidExpectValue != -1) {
			final Validation validation = new ValidationImpl(falsePositives, maxValidExpectValue, cvManager);
			validations.add(validation);
		}

		// Miape date
		if (performParameters != null) {
			final String procStartTime = performParameters.getProcStartTime();
			if (procStartTime != null && !"".equals(procStartTime)) {
				try {
					final String[] tmp = procStartTime.split(":");
					if (tmp.length > 3)
						miapeDate = new MiapeDate(tmp[0] + "-" + tmp[1] + "-" + tmp[2]);
				} catch (final PatternSyntaxException ex) {
					// do nothing
				}
			}
			if (miapeDate == null)
				miapeDate = new MiapeDate(new Date(System.currentTimeMillis()));
		}
		// create proteinAndPeptideHashMaps
		createPeptideAndProteinHashMaps(xfile);

		// protein data sets
		proteinSets
				.add(new IdentifiedProteinSetImpl(inputDataSets, inputParameter, fileLocation, proteinHash.values()));

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getProject() {
		return new ProjectImpl(projectName, owner, dbManager);
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public String getName() {
		if (xtandemXMLFileName != null)
			return xtandemXMLFileName;
		return "MIAPE MSI from X!Tandem result file";
	}

	@Override
	public MiapeDate getDate() {
		return miapeDate;
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
		fileLocation = fileURL;
	}

	@Override
	public ValidationReport getValidationReport() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public int getMSDocumentReference() {
		return referencedMS;
	}

	@Override
	public void setReferencedMSDocument(int id) {
		referencedMS = id;
	}

	@Override
	public String getGeneratedFilesURI() {
		return fileLocation;
	}

	@Override
	public String getGeneratedFilesDescription() {
		final ControlVocabularyTerm xtandemFileTerm = IntermediateFileFormat.getXtandemFileTerm(cvManager);
		if (xtandemFileTerm != null)
			return xtandemFileTerm.getPreferredName();
		return null;
	}

	@Override
	public Set<Software> getSoftwares() {
		if (!softwares.isEmpty())
			return softwares;
		return null;
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		if (!inputDataSets.isEmpty())
			return inputDataSets;
		return null;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		if (!inputParameters.isEmpty())
			return inputParameters;
		return null;
	}

	@Override
	public Set<Validation> getValidations() {
		if (!validations.isEmpty())
			return validations;
		return null;
	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {
		if (!proteinSets.isEmpty())
			return proteinSets;
		return null;
	}

	@Override
	public MSContact getContact() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		final Collection<List<IdentifiedPeptide>> values = peptidesByProteinIDHash.values();
		final List<IdentifiedPeptide> ret = new ArrayList<IdentifiedPeptide>();
		for (final List<IdentifiedPeptide> peptideList : values) {
			for (final IdentifiedPeptide identifiedPeptide : peptideList) {
				if (!ret.contains(identifiedPeptide))
					ret.add(identifiedPeptide);
				else
					log.debug("This peptide is already in the list!");
			}
		}
		return ret;

	}

	private void createPeptideAndProteinHashMaps(XTandemFile xfile) {
		peptidesByProteinIDHash = new THashMap<String, List<IdentifiedPeptide>>();
		final PeptideMap peptideMap = xfile.getPeptideMap();

		final ModificationMap modificationsMap = xfile.getModificationMap();
		final Map<String, String> rawModMap = xfile.getXTandemParser().getRawModMap();
		final InputData inputData = getInputData();
		final ArrayList<Spectrum> spectraList = xfile.getSpectraList();
		log.info("Iterating over " + spectraList.size() + " spectra...");

		for (final Spectrum spectrum : spectraList) {

			final Map<String, IdentifiedPeptide> peptidesByGroup = new THashMap<String, IdentifiedPeptide>();
			final int spectrumNumber = spectrum.getSpectrumNumber();
			final String spectrumRef = xfile.getSupportData(spectrumNumber).getFragIonSpectrumDescription();
			// ArrayList<Peptide> xTandemPeptides =
			// peptideMap.getPeptideByIndex(
			// spectrumNumber, index);
			// ArrayList<Peptide> xTandemPeptides = peptideMap
			// .getAllPeptides(spectrumNumber);
			final Set<String> numGroups = new THashSet<String>();
			for (int numPeptide = 1; numPeptide <= peptideMap.getNumberOfPeptides(spectrumNumber); numPeptide++) {
				// for (Peptide xTandemPeptide : xTandemPeptides) {
				final Peptide xTandemPeptide = peptideMap.getPeptideByIndex(spectrumNumber, numPeptide);
				final List<Domain> domains = xTandemPeptide.getDomains();

				for (final Domain domain : domains) {

					final String proteinRef = domain.getProteinKey();
					// NO CREAR UN NUEVO PEPTIDO SI LOS DOS PRIMEROS NUMEROS DEL
					// ID DEL DOMAIN YA SE HAN VISTO PARA ESTE ESPECTRO!!
					final String domainID = domain.getDomainID();
					final String[] split = domainID.split("\\.");
					final String numGroup = split[0];

					IdentifiedPeptide identifiedPeptide = null;
					if (!numGroups.contains(numGroup)) {
						numGroups.add(numGroup);

						identifiedPeptide = new IdentifiedPeptideImpl(domain, xTandemPeptide, modificationsMap,
								rawModMap, xfile.getInputParameters(), MiapeIdentifierCounter.increaseCounter(),
								inputData, cvManager, spectrum, 1, spectrumRef);

						peptidesByGroup.put(numGroup, identifiedPeptide);
					} else {
						identifiedPeptide = peptidesByGroup.get(numGroup);
					}
					if (peptidesByProteinIDHash.containsKey(proteinRef)) {
						peptidesByProteinIDHash.get(proteinRef).add(identifiedPeptide);
					} else {
						final List<IdentifiedPeptide> peptideList = new ArrayList<IdentifiedPeptide>();
						peptideList.add(identifiedPeptide);
						peptidesByProteinIDHash.put(proteinRef, peptideList);
					}
				}
			}
		}
		final ProteinMap proteinMap = xfile.getProteinMap();
		log.info("Iterating over proteins...");

		// protein hash
		proteinHash = new THashMap<String, IdentifiedProtein>();
		final Iterator<String> proteinIDIterator = proteinMap.getProteinIDIterator();
		while (proteinIDIterator.hasNext()) {
			final String proteinID = proteinIDIterator.next();
			final Protein protein = proteinMap.getProtein(proteinID);

			if (!proteinHash.containsKey(protein.getID())) {
				final IdentifiedProtein identifiedProtein = new IdentifiedProteinImpl(protein,
						MiapeIdentifierCounter.increaseCounter(), modificationsMap, cvManager);
				proteinHash.put(protein.getID(), identifiedProtein);
			}
		}

		// associate proteins and peptides
		log.info("Associating proteins and peptides...");
		final Set<String> proteinRefs = peptidesByProteinIDHash.keySet();
		for (final String proteinRef : proteinRefs) {
			if (proteinHash.containsKey(proteinRef)) {
				final IdentifiedProteinImpl identifiedProtein = (IdentifiedProteinImpl) proteinHash.get(proteinRef);
				final List<IdentifiedPeptide> identifiedPeptides = peptidesByProteinIDHash.get(proteinRef);
				for (final IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
					((IdentifiedPeptideImpl) identifiedPeptide).addProtein(identifiedProtein);
				}

				identifiedProtein.setPeptides(identifiedPeptides);
			}
		}
	}

	private InputData getInputData() {
		if (inputDataSets != null) {
			final InputDataSet next = inputDataSets.iterator().next();
			if (next != null) {
				final Set<InputData> inputDatas = next.getInputDatas();
				if (inputDatas != null)
					return inputDatas.iterator().next();
			}
		}
		return null;
	}

	@Override
	public void setId(int id) {
		this.id = id;

	}
}
