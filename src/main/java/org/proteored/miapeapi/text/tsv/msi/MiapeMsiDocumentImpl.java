package org.proteored.miapeapi.text.tsv.msi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentBuilder;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentFactory;
import org.proteored.miapeapi.factories.msi.PeptideModificationBuilder;
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
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.util.UniprotId2AccMapping;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.utilities.fasta.FastaParser;
import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;

public class MiapeMsiDocumentImpl implements MiapeMSIDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final File tsvFile;

	private final ControlVocabularyManager cvManager;

	private int referencedMS = -1;

	private String url;

	private String fileLocation;

	private MiapeMSIDocument miapeMSI;

	private final String idSetName;

	private final TableTextFileSeparator separator;
	private static final ClassPathResource resource = new ClassPathResource("modification_mappings.xml");

	//

	private SlimModCollection preferredModifications;

	private final String projectName;
	private final User owner;

	public MiapeMsiDocumentImpl(File tsvFile, TableTextFileSeparator separator, ControlVocabularyManager cvManager,
			String idSetName, String projectName) {
		this.tsvFile = tsvFile;
		if (tsvFile == null) {
			throw new IllegalArgumentException("TSV file cannot be null");
		}
		this.owner = null;
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.idSetName = idSetName;
		this.separator = separator;
	}

	public MiapeMsiDocumentImpl(File tsvFile, TableTextFileSeparator separator, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String idSetName, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException {
		this.tsvFile = tsvFile;
		if (tsvFile == null) {
			throw new IllegalArgumentException("TSV file cannot be null");
		}
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.idSetName = idSetName;
		this.separator = separator;
		if (databaseManager != null) {
			owner = databaseManager.getUser(userName, password);
		} else {
			owner = null;
		}
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
			MiapeMSIDocumentBuilder processFile = processFile();
			if (processFile != null) {
				miapeMSI = processFile.build();
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

	protected MiapeMSIDocumentBuilder processFile() {
		BufferedReader dis = null;
		try {

			Map<TableTextFileColumn, Integer> indexesByHeaders = new HashMap<TableTextFileColumn, Integer>();
			Map<String, Integer> indexesByScoreNames = new HashMap<String, Integer>();

			dis = new BufferedReader(new FileReader(tsvFile));
			String line = "";
			log.info("Parsing file TSV " + tsvFile.getAbsolutePath());
			HashMap<String, IdentifiedProtein> proteins = new HashMap<String, IdentifiedProtein>();
			Map<String, IdentifiedPeptide> peptides = new HashMap<String, IdentifiedPeptide>();
			String previousProteinACC = null;
			// String scoreName = null;
			while ((line = dis.readLine()) != null) {
				// COMMENTS STARTING BY #
				if (line.trim().startsWith("#")) {
					// scoreName = getScoreName(line);
					continue;
				}
				String[] split;
				if (line.contains(separator.getSymbol())) {
					split = line.split(separator.getSymbol());
				} else {
					split = new String[1];
					split[0] = line;
				}
				if (split.length > 0) {
					if (indexesByHeaders.isEmpty()) {
						parseHeader(split, indexesByHeaders, indexesByScoreNames);
						if (!indexesByHeaders.containsKey(TableTextFileColumn.ACC)) {
							String message = "ACC column for protein accessions is missing in input file '"
									+ tsvFile.getAbsolutePath() + "'";

							throw new IllegalArgumentException(message);
						}
						if (!indexesByHeaders.containsKey(TableTextFileColumn.SEQ)) {
							String message = "SEQ column for peptide sequences is missing in input file '"
									+ tsvFile.getAbsolutePath() + "'";

							throw new IllegalArgumentException(message);
						}
					} else {
						int accIndex = indexesByHeaders.get(TableTextFileColumn.ACC);
						String preliminarProteinAcc = split[accIndex].trim();
						if ("".equals(preliminarProteinAcc)) {
							preliminarProteinAcc = previousProteinACC;
						}
						if (preliminarProteinAcc.startsWith("\"") && preliminarProteinAcc.endsWith("\"")) {
							preliminarProteinAcc = preliminarProteinAcc.substring(1, preliminarProteinAcc.length() - 1);
						}
						previousProteinACC = preliminarProteinAcc;

						// PEPTIDE SEQUENCE
						int seqIndex = indexesByHeaders.get(TableTextFileColumn.SEQ);
						String rawSeq = split[seqIndex].trim();
						String seq = FastaParser.cleanSequence(rawSeq);
						// seq = parseSequence(seq);

						// PSMID
						String psmID = String.valueOf(peptides.size() + 1);
						if (indexesByHeaders.containsKey(TableTextFileColumn.PSMID)) {
							psmID = split[indexesByHeaders.get(TableTextFileColumn.PSMID)];
						}
						// create or get the peptide (PSM)
						IdentifiedPeptideImplFromTSV peptide = null;
						if (!peptides.containsKey(psmID)) {
							peptide = new IdentifiedPeptideImplFromTSV(seq);
							peptides.put(psmID, peptide);
							if (FastaParser.somethingExtrangeInSequence(rawSeq)) {
								Map<Integer, Double> pTMsByPosition = FastaParser.getPTMsFromSequence(rawSeq);
								for (Integer position : pTMsByPosition.keySet()) {
									String aa = String.valueOf(peptide.getSequence().charAt(position - 1));
									double deltaMass = pTMsByPosition.get(position);
									PeptideModificationBuilder ptmBuilder = MiapeMSIDocumentFactory
											.createPeptideModificationBuilder(
													getModificationNameFromResidueAndMass(aa, deltaMass))
											.monoDelta(deltaMass).position(position).residues(aa);
									peptide.addModification(ptmBuilder.build());
								}
							}

						} else {
							peptide = (IdentifiedPeptideImplFromTSV) peptides.get(psmID);
						}

						// CHARGE
						Integer charge = null;
						if (indexesByHeaders.containsKey(TableTextFileColumn.CHARGE)) {
							try {
								charge = Integer.valueOf(split[indexesByHeaders.get(TableTextFileColumn.CHARGE)]);
							} catch (NumberFormatException e) {
								log.warn("Error parsing charge state from column "
										+ (indexesByHeaders.get(TableTextFileColumn.CHARGE) + 1) + " in file '"
										+ tsvFile.getAbsolutePath() + "'");
								log.warn(e.getMessage());
							}
						}
						peptide.setCharge(String.valueOf(charge));

						// precursor MZ
						Double mz = null;
						if (indexesByHeaders.containsKey(TableTextFileColumn.MZ)) {
							try {
								mz = Double.valueOf(split[indexesByHeaders.get(TableTextFileColumn.MZ)]);
							} catch (NumberFormatException e) {
								log.warn("Error parsing precursor M/Z from column "
										+ (indexesByHeaders.get(TableTextFileColumn.MZ) + 1) + " in file '"
										+ tsvFile.getAbsolutePath() + "'");
								log.warn(e.getMessage());
							}
						}
						peptide.setPrecursorMZ(mz);

						// RT
						Double rt = null;
						if (indexesByHeaders.containsKey(TableTextFileColumn.RT)) {
							try {
								rt = Double.valueOf(split[indexesByHeaders.get(TableTextFileColumn.RT)]);
							} catch (NumberFormatException e) {
								log.warn("Error parsing retention time rom column "
										+ (indexesByHeaders.get(TableTextFileColumn.RT) + 1) + " in file '"
										+ tsvFile.getAbsolutePath() + "'");
								log.warn(e.getMessage());
							}
						}
						peptide.setRetentionTime(rt);

						// PEPTIDE SCORES
						for (String scoreName : indexesByScoreNames.keySet()) {
							String scoreString = split[indexesByScoreNames.get(scoreName)].trim();
							Double score = null;
							try {
								score = Double.valueOf(scoreString);
								PeptideScore peptideScore = MiapeMSIDocumentFactory
										.createPeptideScoreBuilder(scoreName, score.toString()).build();
								peptide.addScore(peptideScore);
							} catch (NumberFormatException e) {
								log.warn("Error parsing score value for column "
										+ (indexesByScoreNames.get(scoreName) + 1) + " in file '"
										+ tsvFile.getAbsolutePath() + "'");
								log.warn(e.getMessage());
							}
						}

						// if more than one accession is present, get the list
						List<String> accessions = splitAccessions(preliminarProteinAcc);
						for (String proteinAcc : accessions) {

							if (proteins.containsKey(proteinAcc)) {
								final IdentifiedProteinImplFromTSV protein = (IdentifiedProteinImplFromTSV) proteins
										.get(proteinAcc);
								if (peptide != null) {
									protein.addPeptide(peptide);
									peptide.addProtein(protein);
								}
							} else {
								IdentifiedProteinImplFromTSV protein = new IdentifiedProteinImplFromTSV(proteinAcc,
										null);
								if (peptide != null) {
									protein.addPeptide(peptide);
									peptide.addProtein(protein);
								}
								proteins.put(proteinAcc, protein);
							}
						}
					}
				}

			}

			if (proteins.isEmpty() && peptides.isEmpty()) {
				return null;
			}

			checkRelationBetweenPeptidesAndProteins(peptides, proteins);

			log.info("End parsing. Now building MIAPE MSI.");
			log.info(peptides.size() + " PSMs and " + proteins.size() + " proteins from file "
					+ tsvFile.getAbsolutePath());

			Project project = MiapeDocumentFactory.createProjectBuilder(projectName).build();

			final MiapeMSIDocumentBuilder builder = MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project,
					idSetName, this.owner);
			List<IdentifiedPeptide> peptideList = new ArrayList<IdentifiedPeptide>();
			peptideList.addAll(peptides.values());
			builder.identifiedPeptides(peptideList);
			Set<IdentifiedProteinSet> proteinSets = new HashSet<IdentifiedProteinSet>();
			IdentifiedProteinSet proteinSet = MiapeMSIDocumentFactory.createIdentifiedProteinSetBuilder("Protein set")
					.identifiedProteins(proteins).build();
			proteinSets.add(proteinSet);
			builder.identifiedProteinSets(proteinSets);
			log.info("MIAPE MSI builder created.");
			return builder;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Throws exception if some protein has no peptides or if some peptide has
	 * no proteins
	 * 
	 * @param peptides
	 * @param proteins
	 * @return
	 */
	private void checkRelationBetweenPeptidesAndProteins(Map<String, IdentifiedPeptide> peptides,
			HashMap<String, IdentifiedProtein> proteins) {
		for (String psmID : peptides.keySet()) {
			IdentifiedPeptide identifiedPeptide = peptides.get(psmID);
			if (identifiedPeptide.getIdentifiedProteins() == null
					|| identifiedPeptide.getIdentifiedProteins().isEmpty()) {
				String message = "Peptide " + identifiedPeptide.getSequence() + " with ID " + psmID
						+ " has no linked to any protein";
				log.warn(message);
				throw new IllegalArgumentException(message);

			}

		}
		for (IdentifiedProtein identifiedProtein : proteins.values()) {
			if (identifiedProtein.getIdentifiedPeptides() == null
					|| identifiedProtein.getIdentifiedPeptides().isEmpty()) {
				String message = "Protein " + identifiedProtein.getAccession() + " has no linked to any peptide";
				log.warn(message);
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Splits the protein accession in a list if they are separated by other
	 * separator than the separator of the class.<br>
	 * It also converts a OKAK_HUMAN uniprot ID to a uniprot ACCESSION like
	 * P123122
	 *
	 * @param proteinAcc
	 * @return
	 */
	private List<String> splitAccessions(String proteinAcc) {
		List<String> ret = new ArrayList<String>();
		if (proteinAcc == null)
			return ret;
		for (TableTextFileSeparator separator2 : TableTextFileSeparator.values()) {
			if (!separator2.equals(separator)) {
				if (proteinAcc.contains(separator2.getSymbol())) {

					String[] split = proteinAcc.split(separator2.getSymbol());
					for (String string : split) {

						ret.add(FastaParser.getACC(string).getFirstelement());
					}
					ret = convertUniprotIDToAcc(ret);
					return ret;
				}
			}
		}
		ret.add(proteinAcc);
		ret = convertUniprotIDToAcc(ret);
		return ret;
	}

	private SlimModCollection getModificationMapping() {
		if (preferredModifications == null) {
			URL url;
			try {
				url = resource.getURL();
				preferredModifications = PrideModController.parseSlimModCollection(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return preferredModifications;
	}

	private List<String> convertUniprotIDToAcc(List<String> accs) {

		try {

			List<String> ret = new ArrayList<String>();
			for (String string : accs) {
				final UniprotId2AccMapping instance = UniprotId2AccMapping.getInstance();
				if (instance != null) {
					final String accFromID = instance.getAccFromID(string);
					if (accFromID != null) {
						log.info("Uniprot ID: " + string + " changed by ACC: " + accFromID);
						ret.add(accFromID);
					} else {
						ret.add(string);
					}
				}
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accs;
	}

	private String getModificationNameFromResidueAndMass(String aa, double deltaMass) {
		try {
			// try first with the PRIDE mapping
			SlimModCollection modificationMapping = getModificationMapping();

			SlimModCollection slimMods = modificationMapping.getbyDelta(deltaMass, 0.03);
			if (slimMods != null && !slimMods.isEmpty()) {
				return slimMods.get(0).getPsiModDesc();
			}
			// TODO add more modifications!
			// read from a file?
			if (aa.equals("C") && compareWithError(deltaMass, 57.022)) {

				final ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(
						new Accession(PeptideModificationName.UNIMOD4), PeptideModificationName.getInstance(cvManager));
				if (cvTerm != null)
					return cvTerm.getPreferredName();
				else
					return "Carbamidomethyl";
			}
			if (aa.equals("E") && compareWithError(deltaMass, -18.0106)) {
				final ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(
						new Accession(PeptideModificationName.UNIMOD27),
						PeptideModificationName.getInstance(cvManager));
				if (cvTerm != null)
					return cvTerm.getPreferredName();
				else
					return "Glu->pyro-Glu";
			}
			final ControlVocabularyTerm pepModifDetailsTerm = PeptideModificationName.getPepModifDetailsTerm(cvManager);
			if (pepModifDetailsTerm != null)
				return pepModifDetailsTerm.getPreferredName();
		} catch (Exception e) {

		}
		DecimalFormat format = new DecimalFormat("#.###");
		return "[" + aa + format.format(deltaMass) + "]";
	}

	private boolean compareWithError(double num1, double num2) {
		double tolerance = 0.001;
		if (num1 > num2)
			if (num1 - num2 < tolerance)
				return true;
		if (num2 > num1)
			if (num2 - num1 < tolerance)
				return true;
		if (num1 == num2)
			return true;
		return false;
	}

	private void parseHeader(String[] splittedLine, Map<TableTextFileColumn, Integer> indexesByHeaders,
			Map<String, Integer> indexesByScoreNames) {
		int index = 0;
		for (String element : splittedLine) {
			element = element.trim();
			boolean found = false;
			for (TableTextFileColumn tsvColumn : TableTextFileColumn.values()) {
				if (element.equalsIgnoreCase(tsvColumn.getHeaderName())) {
					indexesByHeaders.put(tsvColumn, index);
					found = true;
					break;
				}
			}
			// if not recognized as a column, treat it as a new score
			if (!found) {
				indexesByScoreNames.put(element, index);
			}
			index++;
		}

	}
}
