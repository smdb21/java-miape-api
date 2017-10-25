package org.proteored.miapeapi.xml.pepxml;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.SoftwareBuilder;
import org.proteored.miapeapi.factories.msi.AdditionalParameterBuilder;
import org.proteored.miapeapi.factories.msi.DatabaseBuilder;
import org.proteored.miapeapi.factories.msi.IdentifiedProteinBuilder;
import org.proteored.miapeapi.factories.msi.IdentifiedProteinSetBuilder;
import org.proteored.miapeapi.factories.msi.InputDataSetBuilder;
import org.proteored.miapeapi.factories.msi.InputParameterBuilder;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentBuilder;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentFactory;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
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
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import gnu.trove.map.hash.THashMap;
import umich.ms.fileio.exceptions.FileParsingException;
import umich.ms.fileio.filetypes.pepxml.PepXmlParser;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.AminoacidModification;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.AnalysisTimestamp;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.MsmsPipelineAnalysis;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.MsmsRunSummary;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.NameValueType;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SampleEnzyme;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchDatabase;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchHit;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchResult;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchSummary;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SequenceSearchConstraint;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.Specificity;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SpectrumQuery;

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

	private final PersistenceManager dbManager;
	public String searchEngine;

	public MiapeMsiDocumentImpl(File pepXMLFile, ControlVocabularyManager cvManager, String projectName)
			throws FileParsingException {
		this.pepXMLFile = pepXMLFile;
		owner = null;
		this.projectName = projectName;
		this.cvManager = cvManager;
		this.dbManager = null;
		log.info("Starting parsing of pepXML file: " + pepXMLFile.getAbsolutePath());
		MsmsPipelineAnalysis pipelineAnalysis = PepXmlParser.parse(Paths.get(pepXMLFile.getAbsolutePath()));
		log.info("PepXML loaded. Now processing it...");
		searchEngine = PepXMLUtil.getSearchEngineFromSummaryXml(pipelineAnalysis.getSummaryXml());

		// to process the file
		miapeMSI = processPepXMLFile(pipelineAnalysis).build();
		log.info("PepXML processed");
	}

	public MiapeMsiDocumentImpl(File pepXMLFile, PersistenceManager databaseManager, ControlVocabularyManager cvManager,
			String user, String password, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, FileParsingException {

		this.pepXMLFile = pepXMLFile;
		if (databaseManager != null) {
			this.dbManager = databaseManager;
			owner = databaseManager.getUser(user, password);
		} else {
			owner = null;
			dbManager = null;
		}
		this.projectName = projectName;
		this.cvManager = cvManager;
		final MsmsPipelineAnalysis pipelineAnalysis = PepXmlParser.parse(Paths.get(pepXMLFile.getAbsolutePath()));
		// to process the file
		miapeMSI = processPepXMLFile(pipelineAnalysis).build();
	}

	private MiapeMSIDocumentBuilder processPepXMLFile(MsmsPipelineAnalysis pipelineAnalysis)
			throws FileParsingException {
		return getMIAPEMSIDocumentBuilder(pipelineAnalysis, FilenameUtils.getBaseName(pepXMLFile.getAbsolutePath()),
				cvManager, dbManager, owner, projectName);

	}

	public MiapeMSIDocumentBuilder getMIAPEMSIDocumentBuilder(MsmsPipelineAnalysis pipelineAnalysis, String idSetName,
			ControlVocabularyManager cvManager, PersistenceManager dbManager, User owner, String projectName)
			throws FileParsingException {

		Project project = MiapeDocumentFactory.createProjectBuilder(projectName).build();
		MiapeMSIDocumentBuilder miapeBuilder = MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project, idSetName,
				owner, dbManager);
		List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
		Set<InputParameter> inputParamters = new HashSet<InputParameter>();
		miapeBuilder.inputParameters(inputParamters);
		Set<IdentifiedProteinSet> proteinSets = new HashSet<IdentifiedProteinSet>();
		miapeBuilder.identifiedProteinSets(proteinSets);
		Set<InputDataSet> inputDatasets = new HashSet<InputDataSet>();
		Set<Software> softwares = new HashSet<Software>();
		int i = 0;
		int softwareCount = 1;
		int inputParameterID = 1;
		int proteinID = 1;
		for (MsmsRunSummary msmsRunSummary : pipelineAnalysis.getMsmsRunSummary()) {
			i++;
			InputDataSetBuilder inputDataSetBuilder = MiapeMSIDocumentFactory
					.createInputDataSetBuilder("Input_dataset_" + i).id(i);
			InputData inputData = MiapeMSIDocumentFactory.createInputDataBuilder("Input_data_" + i).id(i)
					.msFileType(msmsRunSummary.getRawDataType()).sourceDataUrl(msmsRunSummary.getBaseName()).build();
			inputDataSetBuilder.inputData(inputData);
			IdentifiedProteinSetBuilder proteinSetBuilder = MiapeMSIDocumentFactory
					.createIdentifiedProteinSetBuilder("Protein_set_" + i);
			Set<InputDataSet> inputDatasetsforProteinSet = new HashSet<InputDataSet>();
			inputDatasetsforProteinSet.add(inputDataSetBuilder.build());
			// only one set of proteins per msmsRunSummary. It could contain
			// several searches (several searchSummaries)
			Map<String, IdentifiedProteinBuilder> proteins = new THashMap<String, IdentifiedProteinBuilder>();

			for (SearchSummary searchSummary : msmsRunSummary.getSearchSummary()) {
				final long searchId = searchSummary.getSearchId();

				final InputParameter inputParameter = createInputParameters(inputParameterID++, searchSummary,
						softwares, msmsRunSummary.getSampleEnzyme());
				inputParamters.add(inputParameter);

				for (SpectrumQuery spectrumquery : msmsRunSummary.getSpectrumQuery()) {

					for (SearchResult searchResult : spectrumquery.getSearchResult()) {
						for (SearchHit searchHit : searchResult.getSearchHit()) {

							IdentifiedPeptideImplFromPepXML peptide = new IdentifiedPeptideImplFromPepXML(spectrumquery,
									searchHit, inputData, searchEngine, cvManager);
							peptides.add(peptide);
							// let see the protein
							String proteinACC = searchHit.getProtein();
							IdentifiedProteinBuilder identifiedProtein = null;
							if (proteins.containsKey(proteinACC)) {
								identifiedProtein = proteins.get(proteinACC);
							} else {
								identifiedProtein = createProtein(proteinID++, proteinACC, searchHit);
								proteins.put(proteinACC, identifiedProtein);
							}
							// add peptide to protein
							identifiedProtein.identifiedPeptide(peptide);
						}
					}

				}

			}
			proteinSetBuilder.inputDataSets(inputDatasetsforProteinSet);

			// build the actual proteins
			for (String proteinAcc : proteins.keySet()) {
				IdentifiedProteinBuilder identifiedProteinBuilder = proteins.get(proteinAcc);
				IdentifiedProtein protein = identifiedProteinBuilder.build();
				// add to the protein set
				proteinSetBuilder.identifiedProtein(protein);
				// add proteins to their peptides
				for (IdentifiedPeptide peptide : protein.getIdentifiedPeptides()) {
					if (peptide instanceof IdentifiedPeptideImplFromPepXML) {
						((IdentifiedPeptideImplFromPepXML) peptide).addProtein(protein);
					}
				}
			}
			// build protein set
			proteinSets.add(proteinSetBuilder.build());

			inputDatasets.add(inputDataSetBuilder.build());
			// other sofware steps
			if (msmsRunSummary.getAnalysisTimestamp() != null) {
				for (AnalysisTimestamp analysisTimestamp : msmsRunSummary.getAnalysisTimestamp()) {
					SoftwareBuilder softwareBuilder = MiapeDocumentFactory
							.createSoftwareBuilder(analysisTimestamp.getAnalysis()).id(softwareCount++);
					if (analysisTimestamp.getAny() != null) {
						softwareBuilder.description(analysisTimestamp.getAny().toString());
					}
					if (analysisTimestamp.getTime() != null) {
						softwareBuilder.parameters("timestamp:" + analysisTimestamp.getTime().toString());
					}
					softwares.add(softwareBuilder.build());
				}
			}
		}

		miapeBuilder.identifiedPeptides(peptides);
		miapeBuilder.inputDataSets(inputDatasets);
		miapeBuilder.msiSoftwares(softwares);
		return miapeBuilder;
	}

	private IdentifiedProteinBuilder createProtein(int proteinID, String proteinACC, SearchHit searchHit) {
		return MiapeMSIDocumentFactory.createIdentifiedProteinBuilder(proteinACC)
				.description(searchHit.getProteinDescr()).id(proteinID);

	}

	private InputParameter createInputParameters(int inputParameterID, SearchSummary searchSummary,
			Set<Software> softwares, SampleEnzyme sampleEnzyme) {
		String search_engine_version = null;

		Set<Database> databases = new HashSet<Database>();
		SearchDatabase searchDatabase = searchSummary.getSearchDatabase();
		if (searchDatabase != null) {
			String databaseName = searchDatabase.getDatabaseName();
			if (databaseName == null && searchDatabase.getLocalPath() != null) {
				databaseName = FilenameUtils.getName(searchDatabase.getLocalPath());
			}
			if (databaseName == null && searchDatabase.getOrigDatabaseUrl() != null) {
				databaseName = FilenameUtils.getName(searchDatabase.getOrigDatabaseUrl());
			}
			if (databaseName == null && searchDatabase.getURL() != null) {
				databaseName = FilenameUtils.getName(searchDatabase.getURL());
			}
			DatabaseBuilder databaseBuilder = MiapeMSIDocumentFactory.createDatabaseBuilder(databaseName);
			if (searchDatabase.getDatabaseReleaseDate() != null) {
				databaseBuilder.date(searchDatabase.getDatabaseReleaseDate().toString());
			}
			if (searchDatabase.getDatabaseReleaseIdentifier() != null) {
				databaseBuilder.numVersion(searchDatabase.getDatabaseReleaseIdentifier());
			}
			if (searchDatabase.getURL() != null) {
				databaseBuilder.URI(searchDatabase.getURL());
			} else if (searchDatabase.getLocalPath() != null) {
				databaseBuilder.URI(new File(searchDatabase.getLocalPath()).toURI().toString());
			} else if (searchDatabase.getOrigDatabaseUrl() != null) {
				databaseBuilder.URI(searchDatabase.getOrigDatabaseUrl());
			}
			if (searchDatabase.getType() != null) {
				databaseBuilder.description(searchDatabase.getType());
			}
			databases.add(databaseBuilder.build());

		}
		String precursorMassTolerance = null;
		String precursorMassToleranceUnit = null;
		String aaModifications = null;
		List<AminoacidModification> aminoacidModifications = searchSummary.getAminoacidModification();
		if (aminoacidModifications != null) {
			StringBuilder sb = new StringBuilder();
			for (AminoacidModification aminoacidModification : aminoacidModifications) {
				if (aminoacidModification.getMassdiff() > 0) {
					if (!"".equals(sb.toString())) {
						sb.append(", ");
					}
					if (aminoacidModification.getAminoacid() != null) {
						sb.append(
								aminoacidModification.getAminoacid() + "[" + aminoacidModification.getMassdiff() + "]");
					} else if (aminoacidModification.getPeptideTerminus() != null) {
						sb.append(aminoacidModification.getPeptideTerminus() + "[" + aminoacidModification.getMassdiff()
								+ "]");
					}
					if ("N".equals(aminoacidModification.getVariable())) {
						sb.append("-fixed");
					} else {
						sb.append("-variable");
					}
				}
			}
			aaModifications = sb.toString();
		}
		Set<AdditionalParameter> additionalParameters = new HashSet<AdditionalParameter>();
		String missedcleavages = null;
		if (searchEngine == null) {
			searchEngine = searchSummary.getSearchEngine().value();
			if ("X! Tandem".equals(searchEngine)) {
				// this is to be able to be recognized by a CV term in the MIAPE
				// XML
				searchEngine = "X!Tandem";
			}
			search_engine_version = searchSummary.getSearchEngineVersion();

		}
		for (NameValueType nameValue : searchSummary.getParameter()) {
			final String parameterName = nameValue.getName();
			if (parameterName.equalsIgnoreCase("database_name")) {
				continue;
			} else if (parameterName.equalsIgnoreCase("allowed_missed_cleavage")) {
				missedcleavages = nameValue.getValueStr();
			} else if (parameterName.startsWith("add_")) {
				// because I already took the modifications
				continue;
			} else if (searchEngine != null && parameterName.toLowerCase().contains(searchEngine.toLowerCase())
					&& parameterName.toLowerCase().contains("version")) {
				if (search_engine_version == null) {
					search_engine_version = nameValue.getValueStr();
					if (searchEngine == null) {
						if (parameterName.toLowerCase().contains("comet")) {
							searchEngine = "comet";
						} else if (parameterName.toLowerCase().contains("sequest")) {
							searchEngine = "sequest";
						} else if (parameterName.toLowerCase().contains("mascot")) {
							searchEngine = "mascot";
						} else if (parameterName.toLowerCase().contains("tandem")) {
							searchEngine = "X!Tandem";
						}
					}
				}
			} else if (parameterName.equals("peptide_mass_tolerance")) {
				precursorMassTolerance = nameValue.getValueStr();

			} else if (parameterName.equals("peptide_mass_units")) {
				if ("0".equals(nameValue.getValueStr())) {
					precursorMassToleranceUnit = "amu";
				} else if ("1".equals(nameValue.getValueStr())) {
					precursorMassToleranceUnit = "mmu";
				} else if ("2".equals(nameValue.getValueStr())) {
					precursorMassToleranceUnit = "ppm";
				} else {
					precursorMassToleranceUnit = nameValue.getValueStr();
				}

			} else {
				additionalParameters
						.add(new AdditionalParameterBuilder(parameterName).value(nameValue.getValueStr()).build());
			}
		}
		if (searchSummary.getSequenceSearchConstraint() != null
				&& !searchSummary.getSequenceSearchConstraint().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (SequenceSearchConstraint sequenceSearchConstraint : searchSummary.getSequenceSearchConstraint()) {
				if (!"".equals(sb.toString())) {
					sb.append(", ");
				}
				sb.append(sequenceSearchConstraint.getSequence());
			}
			additionalParameters
					.add(new AdditionalParameterBuilder("Sequence search constraint").value(sb.toString()).build());
		}
		Software software = null;
		if (searchEngine != null) {
			software = MiapeDocumentFactory.createSoftwareBuilder(searchEngine).version(search_engine_version).build();
			// add it to the general set of software
			softwares.add(software);
		}
		// enzymatic_search_constraint
		String enzimeName = null;
		StringBuilder cleavageRules = new StringBuilder();
		if (searchSummary.getEnzymaticSearchConstraint() != null) {
			enzimeName = searchSummary.getEnzymaticSearchConstraint().getEnzyme();
			if (missedcleavages == null
					&& searchSummary.getEnzymaticSearchConstraint().getMaxNumInternalCleavages() != null) {
				missedcleavages = String
						.valueOf(searchSummary.getEnzymaticSearchConstraint().getMaxNumInternalCleavages());
			}
		}
		if (sampleEnzyme != null) {
			if (enzimeName == null) {
				enzimeName = sampleEnzyme.getName();
			}
			if (sampleEnzyme.getDescription() != null) {
				cleavageRules.append("Enzyme description: " + sampleEnzyme.getDescription());
			}
			if (sampleEnzyme.getFidelity() != null) {
				if (!"".equals(cleavageRules.toString())) {
					cleavageRules.append(", ");
				}
				cleavageRules.append("Fidelity: " + sampleEnzyme.getFidelity());
			}
			if (sampleEnzyme.getSpecificity() != null) {
				for (Specificity specificity : sampleEnzyme.getSpecificity()) {
					if (specificity.getCut() != null) {
						if (!"".equals(cleavageRules.toString())) {
							cleavageRules.append(", ");
						}
						cleavageRules.append("Cut: " + specificity.getCut());
					}
					if (specificity.getMinSpacing() > 0) {
						if (!"".equals(cleavageRules.toString())) {
							cleavageRules.append(", ");
						}
						cleavageRules.append("MinSpacing: " + specificity.getMinSpacing());
					}
					if (specificity.getNoCut() != null) {
						if (!"".equals(cleavageRules.toString())) {
							cleavageRules.append(", ");
						}
						cleavageRules.append("NoCut: " + specificity.getNoCut());
					}
					if (specificity.getSense() != null) {
						if (!"".equals(cleavageRules.toString())) {
							cleavageRules.append(", ");
						}
						cleavageRules.append("Sense: " + specificity.getSense());
					}
				}

			}
		}

		InputParameterBuilder inputParameterBuilder = MiapeMSIDocumentFactory
				.createInputParameterBuilder("input_parameters_" + searchSummary.getSearchId()).databases(databases)
				.additionalParameters(additionalParameters).misscleavages(missedcleavages).software(software)
				.precursorMassTolerance(precursorMassTolerance).precursorMassToleranceUnit(precursorMassToleranceUnit)
				.aaModif(aaModifications).cleavageName(enzimeName).id(inputParameterID);

		if (!"".equals(cleavageRules.toString())) {
			inputParameterBuilder.cleavageRules(cleavageRules.toString());
		}

		return inputParameterBuilder.build();
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
		return "MIAPEMSIfromPepXML";
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
