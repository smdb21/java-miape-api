package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.RetentionTime;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;
import org.proteored.miapeapi.xml.util.parallel.MapSync;

import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.SpectraData;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

public class SpectrumIdentificationResultParallelProcesser extends Thread {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final ParIterator<SpectrumIdentificationResult> sirParallelIterator;
	private int threadNumber = -1;
	private final MzIdentMLUnmarshaller mzIdentMLUnmarshaller;
	private final ControlVocabularyManager cvManager;
	private final Reducible<List<IdentifiedPeptide>> peptideListLocal;
	private final MapSync<String, ProteinDetectionHypothesis> pdhWithPeptideEvidence;
	private final Reducible<Map<String, IdentifiedProtein>> proteinHashLocal;
	private final MapSync<String, InputData> syncInputDataHash;

	public SpectrumIdentificationResultParallelProcesser(
			ParIterator<SpectrumIdentificationResult> spectrumIdentificationResultParallelIterator, int processID,
			ControlVocabularyManager cvManager, MzIdentMLUnmarshaller mzIdentMLUnmarshaller,
			MapSync<String, InputData> syncInputDataHash, Reducible<List<IdentifiedPeptide>> peptides,
			MapSync<String, ProteinDetectionHypothesis> proteinDetectionHypotesisWithPeptideEvidence,

			Reducible<Map<String, IdentifiedProtein>> proteinHash) {
		sirParallelIterator = spectrumIdentificationResultParallelIterator;
		threadNumber = processID;
		this.mzIdentMLUnmarshaller = mzIdentMLUnmarshaller;
		this.cvManager = cvManager;
		peptideListLocal = peptides;
		pdhWithPeptideEvidence = proteinDetectionHypotesisWithPeptideEvidence;
		proteinHashLocal = proteinHash;
		this.syncInputDataHash = syncInputDataHash;
	}

	@Override
	public void run() {
		int count = 0;
		List<IdentifiedPeptide> peptideList = new ArrayList<IdentifiedPeptide>();
		peptideListLocal.set(peptideList);

		Map<String, IdentifiedProtein> proteinHash = new THashMap<String, IdentifiedProtein>();
		proteinHashLocal.set(proteinHash);

		log.debug("Starting SIR processing from thread " + threadNumber);

		SpectrumIdentificationResult spectIdentResultXML = null;

		if (sirParallelIterator != null) {
			while (sirParallelIterator.hasNext()) {
				try {
					spectIdentResultXML = sirParallelIterator.next();
					// for (SpectrumIdentificationResult spectIdentResultXML
					// :
					// spectrumIdentificationResult) {

					log.debug("Processed " + count++ + " in core " + threadNumber);

					String RT = getRetentionTimeInSeconds(spectIdentResultXML);

					// TODO this is very important! Be careful.
					// final String spectrumID =
					// spectIdentResultXML.getSpectrumID();
					String spectrumRef = parseSpectrumRef(spectIdentResultXML.getSpectrumID());

					// Input data
					// check if the spectraData is already captured. If not,
					// add
					// a
					// new input Data
					String spectraDataRef = spectIdentResultXML.getSpectraDataRef();
					InputData inputData = null;
					try {
						if (!syncInputDataHash.containsKey(spectraDataRef)) {
							SpectraData spectraDataXML = mzIdentMLUnmarshaller.unmarshal(SpectraData.class,
									spectraDataRef);
							Integer inputDataID = MiapeXmlUtil.InputDataCounter.increaseCounter();
							inputData = new InputDataImpl(spectraDataXML, inputDataID);
							syncInputDataHash.put(spectraDataXML.getId(), inputData);
						}
						inputData = syncInputDataHash.get(spectraDataRef);
					} catch (JAXBException e1) {
						e1.printStackTrace();
					}
					// log.info(spectIdentResultXML.getSpectrumIdentificationItem().size()
					// + " SpectrumIdentificationItems");
					Set<PeptideScore> scoresFromFirstPeptide = new THashSet<PeptideScore>();
					List<SpectrumIdentificationItem> spectrumIdentificationItems = spectIdentResultXML
							.getSpectrumIdentificationItem();
					for (SpectrumIdentificationItem spectIdentItemXML : spectrumIdentificationItems) {

						// some peptides contribute to proteins even if they
						// have
						// not passed the threshold
						// if (spectIdentItemXML.isPassThreshold()) {

						// CREATE Peptide
						Integer peptideID = MiapeXmlUtil.PeptideCounter.increaseCounter();
						Peptide peptideXML = null;

						if (spectIdentItemXML.getPeptideRef() != null) {
							if (peptideXML == null) {
								try {
									peptideXML = mzIdentMLUnmarshaller.unmarshal(Peptide.class,
											spectIdentItemXML.getPeptideRef());
								} catch (JAXBException e) {
									log.debug(e.getMessage());
								} catch (IllegalArgumentException e) {
									log.debug(e.getMessage());
								}
							}
						}
						if (peptideXML == null) {
							peptideXML = getPeptideFromPeptideEvidences(spectIdentItemXML.getPeptideEvidenceRef());
						}
						if (peptideXML == null) {
							throw new IllegalMiapeArgumentException("Peptide " + spectIdentItemXML.getPeptideRef()
									+ " cannot be found in mzIdentML file");
						} else {

							boolean includePeptide = false;
							final Set<PeptideScore> scores = IdentifiedPeptideImpl
									.getScoresFromThisPeptide(spectIdentItemXML, peptideXML, cvManager);
							// if (scores == null || scores.isEmpty()) {
							// log.info("Skipping SII:" +
							// spectIdentItemXML.getId() + " because no scores
							// have found");
							// continue;
							// }
							if (spectIdentItemXML.getRank() == 1) {
								includePeptide = true;
								scoresFromFirstPeptide.clear();
								scoresFromFirstPeptide.addAll(scores);
							} else {

								// if the rank n has the same scores, also
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
								IdentifiedPeptide peptide = new IdentifiedPeptideImpl(spectIdentItemXML, peptideXML,
										inputData, spectrumRef, peptideID, cvManager, pdhWithPeptideEvidence,
										proteinHash, RT);
								// if (peptide.getScores() == null ||
								// peptide.getScores().isEmpty())
								// throw new IllegalMiapeArgumentException(
								// "The peptide from SII:" +
								// spectIdentItemXML.getId() + " has no
								// scores!");
								// Add the peptide to the peptide list
								peptideList.add(peptide);

							}
						}
					}
				} catch (Exception e) {
					sirParallelIterator.register(e);
				}
			}

			log.info("Thread " + threadNumber + " -> " + peptideList.size() + " peptides and " + proteinHash.size()
					+ " proteins");
		}

	}

	private String getRetentionTimeInSeconds(SpectrumIdentificationResult spectIdentResultXML) {
		if (spectIdentResultXML != null) {
			if (spectIdentResultXML.getParamGroup() != null) {
				for (AbstractParam paramType : spectIdentResultXML.getParamGroup()) {
					if (paramType instanceof CvParam) {
						CvParam cvparam = (CvParam) paramType;
						ControlVocabularyTerm cvTerm = RetentionTime.getInstance(cvManager)
								.getCVTermByAccession(new Accession(cvparam.getAccession()));
						if (cvTerm != null) {
							if (cvparam.getValue() != null) {
								try {
									double num = Double.valueOf(cvparam.getValue());
									// check unit
									String unitAccession = cvparam.getUnitAccession();
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
								} catch (NumberFormatException e) {

								}
							}
						}
					}
				}
			}
		}
		List<SpectrumIdentificationItem> spectrumIdentificationItems = spectIdentResultXML
				.getSpectrumIdentificationItem();
		if (spectrumIdentificationItems != null) {
			for (SpectrumIdentificationItem spectrumIdentificationItem : spectrumIdentificationItems) {
				if (spectrumIdentificationItem.getParamGroup() != null) {
					for (AbstractParam paramType : spectrumIdentificationItem.getParamGroup()) {
						if (paramType instanceof CvParam) {
							CvParam cvparam = (CvParam) paramType;
							ControlVocabularyTerm cvTerm = RetentionTime.getInstance(cvManager)
									.getCVTermByAccession(new Accession(cvparam.getAccession()));
							if (cvTerm != null) {
								if (cvparam.getValue() != null) {
									try {
										double num = Double.valueOf(cvparam.getValue());
										// check unit
										String unitAccession = cvparam.getUnitAccession();
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
									} catch (NumberFormatException e) {

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

	/**
	 * Returns the spectrumRef of the spectrumIdentificationResult
	 * 
	 * @param spectrumID
	 *            : If the mzIdentML comes from a MASCOT mgf search, the
	 *            spectrumID should be "index=x" where x is the order of the
	 *            spectra in the MGF (starting by 0).<br>
	 *            If the mzIdentML comes from a MASCOT mzML search, the
	 *            spectrumID should start by "mzMLid=...". In this case, return
	 *            the number of SIR as a spectrumRef, since the mzML spectra
	 *            will be readed in parent mass order, and the mzIdentML
	 *            presents the SIR in parent mass order.
	 * 
	 *            the scan is the order of the spectra in the mzML (starting by
	 *            1)
	 * @param spectrumIdentificationResultIndex
	 * @return
	 */
	private String parseSpectrumRef(String spectrumID) {
		if (spectrumID == null)
			return null;
		Integer specRefInt = 0;
		String mzMLSpectrumIDRegexp = "^mzMLid=(.*)$";

		if (Pattern.matches(mzMLSpectrumIDRegexp, spectrumID)) {
			// in case of being a spectrumID of a mzML file, return the
			// string that appears after the "mzMLid="
			Pattern p = Pattern.compile(mzMLSpectrumIDRegexp);
			Matcher m = p.matcher(spectrumID);
			if (m.find())
				return m.group(1);

		}
		String mgfSpectrumIDRegexp = "^index=(\\d+)$";
		String mzMLSpectrumIDRegexp2 = ".*scan=(\\d+)$";
		String mgfSpectrumIDRegexpQuery = "^query=(\\d+)$";
		String mgfSpectrumIDRegexpQuery2 = "^(\\d+)$";
		String mgfPMEQCIDRegexp = "^.*\\.\\d+\\.\\d+\\.\\d+$"; // spectrumID='OrbiVL1A01.13334.13334.2'

		// if is like "index=1235"
		if (Pattern.matches(mgfSpectrumIDRegexp, spectrumID)) {
			Pattern p = Pattern.compile(mgfSpectrumIDRegexp);
			Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1)) + 1; // sum 1
																// (starts
																// by 0)
			}
			// if it is like
			// "mzMLid=controllerType=0 controllerNumber=1 scan=3423"
		} else if (Pattern.matches(mzMLSpectrumIDRegexp2, spectrumID)) {
			Pattern p = Pattern.compile(mzMLSpectrumIDRegexp2);
			Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1)); // don't sum 1
															// (starts
															// by 1)
			}
		} else if (Pattern.matches(mgfSpectrumIDRegexpQuery, spectrumID)) {
			Pattern p = Pattern.compile(mgfSpectrumIDRegexpQuery);
			Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1));
			}
		} else if (Pattern.matches(mgfSpectrumIDRegexpQuery2, spectrumID)) {
			Pattern p = Pattern.compile(mgfSpectrumIDRegexpQuery2);
			Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				specRefInt = Integer.valueOf(m.group(1));
			}
		} else if (Pattern.matches(mgfPMEQCIDRegexp, spectrumID)) { // spectrumID='OrbiVL1A01.13334.13334.2'
			Pattern p = Pattern.compile(mgfPMEQCIDRegexp);
			Matcher m = p.matcher(spectrumID);
			if (m.find()) {
				return spectrumID;
			}
		}

		if (specRefInt != 0)
			return specRefInt.toString();
		return null;
	}

	/**
	 * Take the first peptideEvidence referenced by the peptideEvidenceRefs
	 * coming from the SpectrumIdentificationItem
	 * 
	 * @param peptideEvidenceRefs
	 * @return
	 */
	private Peptide getPeptideFromPeptideEvidences(List<PeptideEvidenceRef> peptideEvidenceRefs) {
		if (peptideEvidenceRefs != null && !peptideEvidenceRefs.isEmpty()) {
			for (PeptideEvidenceRef peptideEvidenceRef : peptideEvidenceRefs) {
				final PeptideEvidence peptideEvidence = peptideEvidenceRef.getPeptideEvidence();
				if (peptideEvidence != null) {
					if (peptideEvidence.getPeptide() != null)
						return peptideEvidence.getPeptide();

					final String peptideRef = peptideEvidence.getPeptideRef();
					try {
						return mzIdentMLUnmarshaller.unmarshal(Peptide.class, peptideRef);
					} catch (JAXBException e) {
						log.info("JAXBException for unmarshal Peptide with ID:" + peptideRef);
					} catch (NumberFormatException e) {
						log.info("Number format exception for unmarshal Peptide with ID:" + peptideRef);
					} catch (IllegalArgumentException e) {
						log.info("IllegalArgumentException for unmarshal Peptide with ID:" + peptideRef);
					}
				}
			}

		}
		return null;
	}

	private int comparePeptideScores(Set<PeptideScore> scoresFromFirstPeptide, Set<PeptideScore> scores) {
		if (scoresFromFirstPeptide != null && scores != null) {
			if (scores.size() == scoresFromFirstPeptide.size()) {
				for (PeptideScore peptideScore : scores) {
					if (!foundPeptideScore(peptideScore, scoresFromFirstPeptide))
						return -1;
				}
				return 0;
			}
		}
		return -1;
	}

	private boolean foundPeptideScore(PeptideScore peptideScore, Set<PeptideScore> scoresFromFirstPeptide) {
		for (PeptideScore peptideScore2 : scoresFromFirstPeptide) {
			if (peptideScore2.getName().equals(peptideScore.getName()))
				if (peptideScore2.getValue().equals(peptideScore.getValue()))
					return true;
		}
		return false;
	}
}
