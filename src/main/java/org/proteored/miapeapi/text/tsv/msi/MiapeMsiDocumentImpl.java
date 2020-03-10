package org.proteored.miapeapi.text.tsv.msi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
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

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PTMEx;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.set.hash.THashSet;

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

	//

	private final String projectName;
	private final User owner;

	public MiapeMsiDocumentImpl(File tsvFile, TableTextFileSeparator separator, ControlVocabularyManager cvManager,
			String idSetName, String projectName) {
		this.tsvFile = tsvFile;
		if (tsvFile == null) {
			throw new IllegalArgumentException("TSV file cannot be null");
		}
		owner = null;
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
			} catch (final MalformedURLException e) {
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
			final MiapeMSIDocumentBuilder processFile = processFile();
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
		return "MIAPEMSIfromTextFile";
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

			final Map<TableTextFileColumn, Integer> indexesByHeaders = new THashMap<TableTextFileColumn, Integer>();
			final Map<String, Integer> indexesByScoreNames = new THashMap<String, Integer>();

			dis = new BufferedReader(new FileReader(tsvFile));
			String line = "";
			log.info("Parsing file TSV " + tsvFile.getAbsolutePath());
			final Map<String, IdentifiedProtein> proteins = new THashMap<String, IdentifiedProtein>();
			final Map<String, IdentifiedPeptide> peptides = new THashMap<String, IdentifiedPeptide>();
			String previousProteinACC = null;
			int row = 0;
			// String scoreName = null;
			while ((line = dis.readLine()) != null) {
				log.debug("Reading " + line);
				row++;
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
							final String message = "ACC column for protein accessions is missing in input file '"
									+ tsvFile.getAbsolutePath() + "'";

							throw new IllegalArgumentException(message);
						}
						if (!indexesByHeaders.containsKey(TableTextFileColumn.SEQ)) {
							final String message = "SEQ column for peptide sequences is missing in input file '"
									+ tsvFile.getAbsolutePath() + "'";

							throw new IllegalArgumentException(message);
						}
					} else {
						final int accIndex = indexesByHeaders.get(TableTextFileColumn.ACC);
						String preliminarProteinAcc = split[accIndex].trim();
						if ("".equals(preliminarProteinAcc)) {
							preliminarProteinAcc = previousProteinACC;
						}
						if (preliminarProteinAcc.startsWith("\"") && preliminarProteinAcc.endsWith("\"")) {
							preliminarProteinAcc = preliminarProteinAcc.substring(1, preliminarProteinAcc.length() - 1);
						}
						previousProteinACC = preliminarProteinAcc;

						// PEPTIDE SEQUENCE
						final int seqIndex = indexesByHeaders.get(TableTextFileColumn.SEQ);
						String rawSeq = null;
						if (seqIndex < split.length) {
							rawSeq = split[seqIndex].trim();
						} else {
							throw new IllegalMiapeArgumentException(
									"peptide sequence is missing at column " + seqIndex + 1 + " in row " + row);
						}
						final String seq = FastaParser.cleanSequence(rawSeq);
						// seq = parseSequence(seq);

						// PSMID
						String psmID = String.valueOf(peptides.size() + 1);
						if (indexesByHeaders.containsKey(TableTextFileColumn.PSMID)) {
							final Integer index = indexesByHeaders.get(TableTextFileColumn.PSMID);
							if (index < split.length) {
								psmID = split[index];
							}
						}
						// create or get the peptide (PSM)
						IdentifiedPeptideImplFromTSV peptide = null;
						if (!peptides.containsKey(psmID)) {
							peptide = new IdentifiedPeptideImplFromTSV(seq);
							peptides.put(psmID, peptide);
							if (FastaParser.somethingExtrangeInSequence(rawSeq) || rawSeq.contains("ox")) {
								final TIntDoubleHashMap pTMsByPosition = FastaParser.getPTMsFromSequence(rawSeq);
								for (final int position : pTMsByPosition.keys()) {
									String aa = null;
									if (position == 0) {
										// N-terminal
										aa = "N-term";
									} else if (position == peptide.getSequence().length() + 1) {
										// C-terminal
										aa = "C-term";
									} else {
										aa = String.valueOf(peptide.getSequence().charAt(position - 1));
									}
									final double deltaMass = pTMsByPosition.get(position);
									final PeptideModificationBuilder ptmBuilder = MiapeMSIDocumentFactory
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
							final Integer index = indexesByHeaders.get(TableTextFileColumn.CHARGE);
							if (index < split.length) {
								try {

									charge = Integer.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing charge state from column " + (index + 1) + " in file '"
											+ tsvFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setCharge(String.valueOf(charge));

						// precursor MZ
						Double mz = null;
						if (indexesByHeaders.containsKey(TableTextFileColumn.MZ)) {
							final Integer index = indexesByHeaders.get(TableTextFileColumn.MZ);
							if (index < split.length) {
								try {
									mz = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing precursor M/Z from column " + (index + 1) + " in file '"
											+ tsvFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setPrecursorMZ(mz);

						// RT
						Double rt = null;
						if (indexesByHeaders.containsKey(TableTextFileColumn.RT)) {
							final Integer index = indexesByHeaders.get(TableTextFileColumn.RT);
							if (index < split.length) {
								try {
									rt = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing retention time rom column " + (index + 1) + " in file '"
											+ tsvFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setRetentionTime(rt);

						// PEPTIDE SCORES
						for (final String scoreName : indexesByScoreNames.keySet()) {

							final Integer index = indexesByScoreNames.get(scoreName);
							if (split.length > index) {
								final String scoreString = split[index].trim();
								Double score = null;
								try {
									score = Double.valueOf(scoreString);
									final PeptideScore peptideScore = MiapeMSIDocumentFactory
											.createPeptideScoreBuilder(scoreName, score.toString()).build();
									peptide.addScore(peptideScore);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing score value for column " + (index + 1) + " row " + row
											+ " in file '" + tsvFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}

						// if more than one accession is present, get the list
						final List<String> accessions = splitAccessions(preliminarProteinAcc);
						for (final String proteinAcc : accessions) {

							if (proteins.containsKey(proteinAcc)) {
								final IdentifiedProteinImplFromTSV protein = (IdentifiedProteinImplFromTSV) proteins
										.get(proteinAcc);
								if (peptide != null) {
									protein.addPeptide(peptide);
									peptide.addProtein(protein);
								}
							} else {
								final IdentifiedProteinImplFromTSV protein = new IdentifiedProteinImplFromTSV(
										proteinAcc, null);
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
				throw new IllegalArgumentException("No proteins and peptides were able to be parsed");
			}

			checkRelationBetweenPeptidesAndProteins(peptides, proteins);

			log.info("End parsing. Now building MIAPE MSI.");
			log.info(peptides.size() + " PSMs and " + proteins.size() + " proteins from file "
					+ tsvFile.getAbsolutePath());

			final Project project = MiapeDocumentFactory.createProjectBuilder(projectName).build();

			final MiapeMSIDocumentBuilder builder = MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project,
					idSetName, owner);
			final List<IdentifiedPeptide> peptideList = new ArrayList<IdentifiedPeptide>();
			peptideList.addAll(peptides.values());
			builder.identifiedPeptides(peptideList);
			final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();
			final IdentifiedProteinSet proteinSet = MiapeMSIDocumentFactory
					.createIdentifiedProteinSetBuilder("Protein set").identifiedProteins(proteins).build();
			proteinSets.add(proteinSet);
			builder.identifiedProteinSets(proteinSets);
			log.info("MIAPE MSI builder created.");
			return builder;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Throws exception if some protein has no peptides or if some peptide has no
	 * proteins
	 * 
	 * @param peptides
	 * @param proteins
	 * @return
	 */
	private void checkRelationBetweenPeptidesAndProteins(Map<String, IdentifiedPeptide> peptides,
			Map<String, IdentifiedProtein> proteins) {
		for (final String psmID : peptides.keySet()) {
			final IdentifiedPeptide identifiedPeptide = peptides.get(psmID);
			if (identifiedPeptide.getIdentifiedProteins() == null
					|| identifiedPeptide.getIdentifiedProteins().isEmpty()) {
				final String message = "Peptide " + identifiedPeptide.getSequence() + " with ID " + psmID
						+ " has no linked to any protein";
				log.warn(message);
				throw new IllegalArgumentException(message);

			}

		}
		for (final IdentifiedProtein identifiedProtein : proteins.values()) {
			if (identifiedProtein.getIdentifiedPeptides() == null
					|| identifiedProtein.getIdentifiedPeptides().isEmpty()) {
				final String message = "Protein " + identifiedProtein.getAccession() + " has no linked to any peptide";
				log.warn(message);
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Splits the protein accession in a list if they are separated by other
	 * separator than the separator of the class.<br>
	 * It also converts a OKAK_HUMAN uniprot ID to a uniprot ACCESSION like P123122
	 *
	 * @param proteinAcc
	 * @return
	 */
	private List<String> splitAccessions(String proteinAcc) {
		List<String> ret = new ArrayList<String>();
		if (proteinAcc == null)
			return ret;
		for (final TableTextFileSeparator separator2 : TableTextFileSeparator.values()) {
			if (!separator2.equals(separator)) {
				if (proteinAcc.contains(separator2.getSymbol())) {

					final String[] split = proteinAcc.split(separator2.getSymbol());
					for (final String string : split) {

						ret.add(FastaParser.getACC(string).getAccession());
					}
					ret = convertUniprotIDToAcc(ret);
					return ret;
				}
			}
		}
		ret.add(FastaParser.getACC(proteinAcc).getAccession());
		ret = convertUniprotIDToAcc(ret);
		return ret;
	}

	private List<String> convertUniprotIDToAcc(List<String> accs) {

		try {

			final List<String> ret = new ArrayList<String>();
			for (final String string : accs) {
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
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accs;
	}

	private String getModificationNameFromResidueAndMass(String aa, double deltaMass) {
		try {
			// try first with the PRIDE mapping
			final PTM ptm = new PTMEx(deltaMass, aa, -1);
			if (ptm.getName() != null) {
				return ptm.getName();
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
			final DecimalFormat df = new DecimalFormat("+#.####,-#.####");
			return df.format(deltaMass);

			// final ControlVocabularyTerm pepModifDetailsTerm =
			// PeptideModificationName.getPepModifDetailsTerm(cvManager);
			// if (pepModifDetailsTerm != null)
			// return pepModifDetailsTerm.getPreferredName();
		} catch (final Exception e) {

		}
		final DecimalFormat format = new DecimalFormat("#.###");
		return "[" + aa + format.format(deltaMass) + "]";
	}

	private boolean compareWithError(double num1, double num2) {
		final double tolerance = 0.001;
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
			for (final TableTextFileColumn tsvColumn : TableTextFileColumn.values()) {
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
