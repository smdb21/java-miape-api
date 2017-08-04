package org.proteored.miapeapi.xml.pepxml.msi.copy;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentBuilder;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import umich.ms.fileio.exceptions.FileParsingException;
import umich.ms.fileio.filetypes.pepxml.PepXmlParser;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.MsmsPipelineAnalysis;

public class MiapeMsiDocumentImpl implements MiapeMSIDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final ControlVocabularyManager cvManager;

	private final User owner;

	private final String projectName;

	private int referencedMS = -1;

	private String url;

	private String fileLocation;

	private MiapeMSIDocument miapeMSI;

	private final File pepXMLFile;

	public MiapeMsiDocumentImpl(File pepXMLFile, ControlVocabularyManager cvManager, String xtandemXMLFileName,
			String projectName) {
		this.pepXMLFile = pepXMLFile;
		owner = null;
		this.projectName = projectName;
		this.cvManager = cvManager;

	}

	public MiapeMsiDocumentImpl(File pepXMLFile, PersistenceManager databaseManager, ControlVocabularyManager cvManager,
			String user, String password, String projectName) throws MiapeDatabaseException, MiapeSecurityException {

		this.pepXMLFile = pepXMLFile;
		if (databaseManager != null) {
			owner = databaseManager.getUser(user, password);
		} else {
			owner = null;
		}
		this.projectName = projectName;
		this.cvManager = cvManager;
	}

	private MiapeMSIDocumentBuilder processPepXMLFile(File pepXMLFile) throws IOException, FileParsingException {
		return getMIAPEMSIDocumentBuilder(pepXMLFile, FilenameUtils.getBaseName(pepXMLFile.getAbsolutePath()),
				cvManager, owner, projectName);

	}

	public static MiapeMSIDocumentBuilder getMIAPEMSIDocumentBuilder(File pepXMLFile, String idSetName,
			ControlVocabularyManager cvManager, User owner, String projectName)
			throws IOException, FileParsingException {
		// clear map
		IdentifiedPeptideImplFromPepXML.map.clear();

		final MsmsPipelineAnalysis dtaSelectProteins = PepXmlParser.parse(Paths.get(pepXMLFile.getAbsolutePath()));
		// Map<String, IdentifiedProtein> proteins = new THashMap<String,
		// IdentifiedProtein>();
		// List<IdentifiedPeptide> peptides = new
		// ArrayList<IdentifiedPeptide>();
		// Set<String> psmIDs = new THashSet<String>();
		// for (String acc : dtaSelectProteins.keySet()) {
		// final DTASelectProtein dtaSelectProtein = dtaSelectProteins.get(acc);
		// IdentifiedProtein protein = new
		// IdentifiedProteinImplFromPepXMLProtein(dtaSelectProtein, cvManager);
		// final String accession = protein.getAccession();
		// if (proteins.containsKey(accession))
		// protein = proteins.get(accession);
		// proteins.put(accession, protein);
		// final List<IdentifiedPeptide> identifiedPeptides =
		// protein.getIdentifiedPeptides();
		// for (IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
		// if (!psmIDs.contains(identifiedPeptide.getSpectrumRef())) {
		// psmIDs.add(identifiedPeptide.getSpectrumRef());
		// peptides.add(identifiedPeptide);
		// }
		// }
		// }
		//
		// log.info("End parsing. Now building MIAPE MSI with " +
		// proteins.size() + " proteins and " + peptides.size()
		// + " peptides");
		// Project project =
		// MiapeDocumentFactory.createProjectBuilder(projectName).build();
		// MiapeMSIDocumentBuilder builder =
		// MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project,
		// idSetName,
		// owner);
		// builder.identifiedPeptides(peptides);
		// Set<IdentifiedProteinSet> proteinSets = new
		// THashSet<IdentifiedProteinSet>();
		// IdentifiedProteinSet proteinSet =
		// MiapeMSIDocumentFactory.createIdentifiedProteinSetBuilder("Protein
		// set")
		// .identifiedProteins(proteins).build();
		// proteinSets.add(proteinSet);
		// builder.identifiedProteinSets(proteinSets);
		//
		// log.info("MIAPE MSI builder created.");
		return null;
	}

	public void setAttachedFileURL(String fileURL) {
		final File file = new File(fileURL);
		// convert to local url
		if (file.exists()) {
			try {
				fileURL = file.toURI().toURL().toString();
			} catch (MalformedURLException e) {
			}
		}
		url = fileURL;
		fileLocation = fileURL;
	}

	@Override
	public String getAttachedFileLocation() {
		return url;
	}

	@Override
	public String getGeneratedFilesURI() {
		return fileLocation;
	}

	private MiapeMSIDocument getMiapeMSIDocument() {
		if (miapeMSI == null) {
			try {
				miapeMSI = processPepXMLFile(this.pepXMLFile).build();
			} catch (IOException | FileParsingException e) {
				e.printStackTrace();
			}
		}
		return miapeMSI;
	}

	@Override
	public int getMSDocumentReference() {
		return referencedMS;
	}

	@Override
	public int getId() {
		return getMiapeMSIDocument().getId();
	}

	@Override
	public String getVersion() {
		return getMiapeMSIDocument().getVersion();
	}

	@Override
	public Project getProject() {
		return getMiapeMSIDocument().getProject();
	}

	@Override
	public User getOwner() {
		return getMiapeMSIDocument().getOwner();
	}

	@Override
	public String getName() {
		return getMiapeMSIDocument().getName();
	}

	@Override
	public MiapeDate getDate() {
		return getMiapeMSIDocument().getDate();
	}

	@Override
	public Date getModificationDate() {
		return getMiapeMSIDocument().getModificationDate();
	}

	@Override
	public Boolean getTemplate() {
		return getMiapeMSIDocument().getTemplate();
	}

	@Override
	public ValidationReport getValidationReport() {
		return getMiapeMSIDocument().getValidationReport();
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		return getMiapeMSIDocument().store();
	}

	@Override
	public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
		getMiapeMSIDocument().delete(userName, password);
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml() {
		return MiapeMSIXmlFactory.getFactory().toXml(this, cvManager);
	}

	@Override
	public void setReferencedMSDocument(int id) {
		referencedMS = id;

	}

	@Override
	public String getGeneratedFilesDescription() {
		// TODO
		return "MIAPEMSIfromDTASelect";
	}

	@Override
	public Set<Software> getSoftwares() {
		return getMiapeMSIDocument().getSoftwares();
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return getMiapeMSIDocument().getInputDataSets();
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		return getMiapeMSIDocument().getInputParameters();
	}

	@Override
	public Set<Validation> getValidations() {
		return getMiapeMSIDocument().getValidations();
	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {
		return getMiapeMSIDocument().getIdentifiedProteinSets();
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return getMiapeMSIDocument().getIdentifiedPeptides();
	}

	@Override
	public MSContact getContact() {
		return getMiapeMSIDocument().getContact();
	}

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		return getMiapeMSIDocument().getAdditionalInformations();
	}

	@Override
	public void setId(int id) {
		getMiapeMSIDocument().setId(id);

	}
}