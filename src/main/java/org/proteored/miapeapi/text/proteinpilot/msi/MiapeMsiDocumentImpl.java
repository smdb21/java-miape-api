package org.proteored.miapeapi.text.proteinpilot.msi;

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
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.text.tsv.msi.TableTextFileSeparator;
import org.proteored.miapeapi.util.UniprotId2AccMapping;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.masses.AssignMass;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

public class MiapeMsiDocumentImpl implements MiapeMSIDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final File peptideFile;

	private final ControlVocabularyManager cvManager;

	private int referencedMS = -1;

	private String url;

	private String fileLocation;

	private MiapeMSIDocument miapeMSI;

	private final String idSetName;

	private final TableTextFileSeparator separator = TableTextFileSeparator.TAB;
	private static final ModReader modReader = ModReader.getInstance();

	//

	private final String projectName;
	private final User owner;

	public MiapeMsiDocumentImpl(File peptideFile, ControlVocabularyManager cvManager, String idSetName,
			String projectName) {
		this.peptideFile = peptideFile;
		if (peptideFile == null) {
			throw new IllegalArgumentException("Peptide file cannot be null");
		}

		owner = null;
		this.cvManager = cvManager;
		this.projectName = projectName;
		this.idSetName = idSetName;
	}

	public MiapeMsiDocumentImpl(File peptideFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String idSetName, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException {
		this.peptideFile = peptideFile;

		if (peptideFile == null) {
			throw new IllegalArgumentException("Peptide file cannot be null");
		}

		this.cvManager = cvManager;
		this.projectName = projectName;
		this.idSetName = idSetName;
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
			final MiapeMSIDocumentBuilder processFile = processFiles();
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
		return "MIAPEMSIfromProteinPilotFiles";
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

	protected MiapeMSIDocumentBuilder processFiles() {

		final Map<String, IdentifiedProtein> proteins = new THashMap<String, IdentifiedProtein>();
		final Map<String, IdentifiedPeptide> peptides = new THashMap<String, IdentifiedPeptide>();
		processPeptideFile(proteins, peptides);
		if (proteins.isEmpty() && peptides.isEmpty()) {
			throw new IllegalArgumentException("No proteins and peptides were able to be parsed");
		}

		checkRelationBetweenPeptidesAndProteins(peptides, proteins);

		log.info("End parsing. Now building MIAPE MSI.");
		log.info(peptides.size() + " PSMs and " + proteins.size() + " proteins from file "
				+ peptideFile.getAbsolutePath());

		final Project project = MiapeDocumentFactory.createProjectBuilder(projectName).build();

		final MiapeMSIDocumentBuilder builder = MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project,
				idSetName, owner);
		final List<IdentifiedPeptide> peptideList = new ArrayList<IdentifiedPeptide>();
		peptideList.addAll(peptides.values());
		builder.identifiedPeptides(peptideList);
		final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();
		final IdentifiedProteinSet proteinSet = MiapeMSIDocumentFactory.createIdentifiedProteinSetBuilder("Protein set")
				.identifiedProteins(proteins).build();
		proteinSets.add(proteinSet);
		builder.identifiedProteinSets(proteinSets);
		log.info("MIAPE MSI builder created.");
		return builder;
	}

	private void processPeptideFile(Map<String, IdentifiedProtein> proteins, Map<String, IdentifiedPeptide> peptides) {
		final Map<TableTextProteinPilotPeptideFileColumn, Integer> indexesByHeaders = new THashMap<TableTextProteinPilotPeptideFileColumn, Integer>();
		BufferedReader dis = null;
		try {

			dis = new BufferedReader(new FileReader(peptideFile));
			String line = "";
			log.info("Parsing proteinpilot peptide file " + peptideFile.getAbsolutePath());

			final String previousProteinACC = null;
			int row = 0;
			// String scoreName = null;
			while ((line = dis.readLine()) != null) {
				log.debug("Reading " + line);
				row++;
				// COMMENTS STARTING BY #
				if (line.trim().startsWith("#") || "".equals(line.trim())) {
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
						parsePeptideFileHeader(split, indexesByHeaders);

					} else {

						// PEPTIDE SEQUENCE
						final int seqIndex = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.SEQUENCE);
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
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.SPECTRUM)) {
							final Integer index = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.SPECTRUM);
							if (index < split.length) {
								psmID = split[index];
							}
						}

						// create or get the peptide (PSM)
						IdentifiedPeptideImplFromProteinPilot peptide = null;
						if (!peptides.containsKey(psmID)) {
							peptide = new IdentifiedPeptideImplFromProteinPilot(seq);
							peptides.put(psmID, peptide);
						} else {
							peptide = (IdentifiedPeptideImplFromProteinPilot) peptides.get(psmID);
							// check if the score is better or not. if it is not
							// better, continue to next line
							final Double conf1 = getScore(peptide.getScores(), "Conf");
							if (conf1 != null) {
								if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.CONF)) {
									final Integer index = indexesByHeaders
											.get(TableTextProteinPilotPeptideFileColumn.CONF);
									if (index < split.length) {
										try {
											final double conf2 = Double.valueOf(split[index]);
											if (conf2 <= conf1) {
												log.info("Skipping row for scan " + psmID
														+ " because an equaly or more confident peptide (" + conf1
														+ " vs " + conf2 + ") was found before");
												continue;
											}
										} catch (final NumberFormatException e) {
										}
									}
								}
							}
						}
						peptide.setSpectrumRef(psmID);
						// CHARGE
						Integer charge = null;
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.THEOR_Z)) {
							final Integer index = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.THEOR_Z);
							if (index < split.length) {
								try {

									charge = Integer.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing charge state from column " + (index + 1) + " in file '"
											+ peptideFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setCharge(String.valueOf(charge));
						// Retention time
						Double rt = null;
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.APEX_PEPTIDE)) {
							final Integer index = indexesByHeaders
									.get(TableTextProteinPilotPeptideFileColumn.APEX_PEPTIDE);
							if (index < split.length && !"".equals(split[index])) {
								try {
									rt = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn(
											"Error parsing "
													+ TableTextProteinPilotPeptideFileColumn.APEX_PEPTIDE
															.getHeaderName()
													+ " state from column " + (index + 1) + " in file '"
													+ peptideFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setRetentionTime(rt);
						// precursor MZ
						Double mz = null;
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.OBS_MZ)) {
							final Integer index = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.OBS_MZ);
							if (index < split.length) {
								try {
									mz = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing precursor M/Z from column " + (index + 1) + " in file '"
											+ peptideFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.PREC_MZ)) {
							final Integer index = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.PREC_MZ);
							if (index < split.length) {
								try {
									mz = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing precursor M/Z from column " + (index + 1) + " in file '"
											+ peptideFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setPrecursorMZ(mz);
						// precursor MZ
						Double theorMZ = null;
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.THEOR_MZ)) {
							final Integer index = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.THEOR_MZ);
							if (index < split.length) {
								try {
									theorMZ = Double.valueOf(split[index]);
								} catch (final NumberFormatException e) {
									log.warn("Error parsing precursor theoretical M/Z from column " + (index + 1)
											+ " in file '" + peptideFile.getAbsolutePath() + "'");
									log.warn(e.getMessage());
								}
							}
						}
						peptide.setTheorMZ(theorMZ);
						// ptms
						// format: Gln->pyro-Glu@N-term; Carbamidomethyl(Y)@4
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.MODIFICATIONS)) {
							final Integer index = indexesByHeaders
									.get(TableTextProteinPilotPeptideFileColumn.MODIFICATIONS);
							final String modifications = split[index];
							final List<String> individualModifications = new ArrayList<String>();
							if (modifications.contains(";")) {
								for (final String split2 : modifications.split(";")) {
									individualModifications.add(split2.trim());
								}
							} else {
								individualModifications.add(modifications.trim());
							}
							for (final String individualModification : individualModifications) {
								if (individualModification.contains("@")) {
									final String[] split3 = individualModification.split("@");
									String name = split3[0];
									if (name.contains("(")) {
										name = name.substring(0, name.indexOf("("));
									}
									final String positionString = split3[1];
									int position = -1;
									if (positionString.equalsIgnoreCase("N-term")) {
										position = 0;
									} else if (positionString.equalsIgnoreCase("C-term")) {
										position = seq.length();
									} else {
										try {
											position = Integer.valueOf(positionString);
										} catch (final NumberFormatException e) {
											log.warn("Error parsing PTM position from column " + (index + 1)
													+ " in file '" + peptideFile.getAbsolutePath() + "'");
											log.warn(e.getMessage());
										}
									}
									final List<PTM> ptmListByPatternName = modReader.getPTMListByPatternName(name);
									PTM ptmFromReader = null;
									if (!ptmListByPatternName.isEmpty()) {
										ptmFromReader = ptmListByPatternName.get(0);
									} else {
										final List<PTM> ptmListByEqualName = modReader.getPTMListByEqualName(name);
										if (!ptmListByEqualName.isEmpty()) {
											ptmFromReader = ptmListByEqualName.get(0);
										}
									}
									if (ptmFromReader == null) {
										log.info(individualModification + " not recognized in ontologies");
									}
									if (ptmFromReader != null) {
										final PeptideModificationBuilder ptmBuilder = MiapeMSIDocumentFactory
												.createPeptideModificationBuilder(name)
												.monoDelta(ptmFromReader.getMonoDeltaMass())
												.avgDelta(ptmFromReader.getAveDeltaMass()).position(position);
										// if (name.contains("->")) {
										// // is a substitution
										// final String[] split4 =
										// name.split("->");
										// ptmBuilder.replacementResidue(split4[1]).residues(split4[0]);
										// } else {
										if (position > 0) {
											ptmBuilder.residues(String.valueOf(seq.charAt(position - 1)));
										} else {
											ptmBuilder.residues("N-term");
										}
										// }
										peptide.addModification(ptmBuilder.build());
									} else {
										double monoDeltaMass = 0;
										if (name.equals("Lys-add")) {
											monoDeltaMass = AssignMass.getMass('K');
										} else if (name.equals("Arg-add")) {
											monoDeltaMass = AssignMass.getMass('K');
										}
										final PeptideModificationBuilder ptmBuilder = MiapeMSIDocumentFactory
												.createPeptideModificationBuilder(name).monoDelta(monoDeltaMass)
												.position(position);
										if (position > 0) {
											ptmBuilder.residues(String.valueOf(seq.charAt(position - 1)));
										} else {
											ptmBuilder.residues("N-term");
										}
										peptide.addModification(ptmBuilder.build());
									}
								}
							}

						}

						// PEPTIDE SCORES
						for (final TableTextProteinPilotPeptideFileColumn column : TableTextProteinPilotPeptideFileColumn
								.values()) {
							if (column.isScore()) {
								if (indexesByHeaders.containsKey(column)) {
									final Integer index = indexesByHeaders.get(column);

									final String scoreString = split[index].trim();
									if (!"".equals(scoreString)) {
										Double score = null;
										try {
											score = Double.valueOf(scoreString);
											final PeptideScore peptideScore = MiapeMSIDocumentFactory
													.createPeptideScoreBuilder(column.getHeaderName(), score.toString())
													.build();
											peptide.addScore(peptideScore);
										} catch (final NumberFormatException e) {
											log.warn("Error parsing score value for column " + (index + 1) + " row "
													+ row + " in file '" + peptideFile.getAbsolutePath() + "'");
											log.warn(e.getMessage());
										}
									}
								}
							}
						}
						final int accIndex = indexesByHeaders.get(TableTextProteinPilotPeptideFileColumn.ACC);
						String preliminarProteinAcc = split[accIndex].trim();
						if ("".equals(preliminarProteinAcc)) {
							preliminarProteinAcc = previousProteinACC;
						}
						if (preliminarProteinAcc.startsWith("\"") && preliminarProteinAcc.endsWith("\"")) {
							preliminarProteinAcc = preliminarProteinAcc.substring(1, preliminarProteinAcc.length() - 1);
						}

						String preliminaryDescription = null;
						if (indexesByHeaders.containsKey(TableTextProteinPilotPeptideFileColumn.DESCRIPTION)) {
							final int descriptionIndex = indexesByHeaders
									.get(TableTextProteinPilotPeptideFileColumn.DESCRIPTION);
							preliminaryDescription = split[descriptionIndex].trim();
						}
						Double individualProteinScore = null;
						if (indexesByHeaders
								.containsKey(TableTextProteinPilotPeptideFileColumn.INDIVIDUAL_PROTEIN_SCORE)) {
							final int proteinScoreIndex = indexesByHeaders
									.get(TableTextProteinPilotPeptideFileColumn.INDIVIDUAL_PROTEIN_SCORE);
							individualProteinScore = Double.valueOf(split[proteinScoreIndex].trim());
						}

						IdentifiedProteinImplFromProteinPilot protein = null;
						// if more than one accession is present, get the list
						final List<String> accessions = splitAccessions(preliminarProteinAcc, ";");
						final List<String> descriptions = splitDescriptions(preliminaryDescription, ";");
						for (int i = 0; i < accessions.size(); i++) {
							final String proteinAcc = accessions.get(i);
							final String description = descriptions.get(i);
							if (proteins.containsKey(proteinAcc)) {
								protein = (IdentifiedProteinImplFromProteinPilot) proteins.get(proteinAcc);
								if (peptide != null) {
									protein.addPeptide(peptide);
									peptide.addProtein(protein);
								}
							} else {
								protein = new IdentifiedProteinImplFromProteinPilot(proteinAcc, description);
								if (peptide != null) {
									protein.addPeptide(peptide);
									peptide.addProtein(protein);
								}
								proteins.put(proteinAcc, protein);
							}
							if (description != null) {
								protein.setDescription(description);
							}
							// scores
							if (individualProteinScore != null) {
								if (protein.getScores().isEmpty()) {
									final ProteinScore proteinScore = MiapeMSIDocumentFactory.createProteinScoreBuilder(
											"ProteinPilot protein score", individualProteinScore.toString()).build();
									protein.getScores().add(proteinScore);
								}
							}
						}

					}
				}

			}

		} catch (

		final Exception e) {
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
	}

	private Double getScore(Set<PeptideScore> scores, String scoreName) {
		if (scores != null) {
			for (final PeptideScore peptideScore : scores) {
				if (peptideScore.getName().equals(scoreName)) {
					return Double.valueOf(peptideScore.getValue());
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

	public List<String> splitDescriptions(String descriptions, String separator) {
		final List<String> ret = new ArrayList<String>();
		if (descriptions.contains(separator)) {
			final String[] split = descriptions.split(separator);
			for (final String string : split) {
				ret.add(string);
			}
		} else {
			ret.add(descriptions);
		}
		return ret;
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
	private List<String> splitAccessions(String proteinAcc, String separator) {
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
		ret.add(proteinAcc);
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

			final List<PTM> ptmCandidates = modReader.getPTMListByAvgDeltaMass(deltaMass);
			if (ptmCandidates != null && !ptmCandidates.isEmpty()) {
				return ptmCandidates.get(0).getName();
			}
			// TODO add more modifications!
			// read from a file?

			if (aa.equals("E") && compareWithError(deltaMass, -18.0106)) {
				final ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(
						new Accession(PeptideModificationName.UNIMOD27),
						PeptideModificationName.getInstance(cvManager));
				if (cvTerm != null)
					return cvTerm.getPreferredName();
				else
					return "Glu->pyro-Glu";
			}
			final DecimalFormat df = new DecimalFormat("+#.####");
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

	private void parsePeptideFileHeader(String[] splittedLine,
			Map<TableTextProteinPilotPeptideFileColumn, Integer> indexesByHeaders) {
		int index = 0;
		for (String element : splittedLine) {
			element = element.trim();
			boolean found = false;
			for (final TableTextProteinPilotPeptideFileColumn column : TableTextProteinPilotPeptideFileColumn
					.values()) {
				if (element.equalsIgnoreCase(column.getHeaderName())) {
					indexesByHeaders.put(column, index);
					found = true;
					break;
				}
			}
			// if not recognized as a column, treat it as a new score
			if (!found) {
				log.warn("Column '" + element + "' is not recognized");
			}
			index++;
		}
		for (final TableTextProteinPilotPeptideFileColumn column : TableTextProteinPilotPeptideFileColumn.values()) {
			if (column.isMandatory() && !indexesByHeaders.containsKey(column)) {
				final String message = column.getHeaderName() + " column is missing in input file '"
						+ peptideFile.getAbsolutePath() + "'";

				throw new IllegalArgumentException(message);
			}
		}

	}
}
