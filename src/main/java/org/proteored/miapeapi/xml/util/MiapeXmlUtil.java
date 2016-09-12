package org.proteored.miapeapi.xml.util;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;

public class MiapeXmlUtil {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");
	public static final String TERM_SEPARATOR = "\n";
	public static final String CALCULATED_MZ = "Calculated Mass to Charge";
	public static final String EXPERIMENTAL_MZ = "Experimental Mass to Charge";
	public static final String ERROR_MZ = "Error Mass to Charge";
	public static final double PROTON_MASS = 1.00727647;

	public static String getFileNameFromPath(String filePath) {
		if (filePath == null)
			return null;
		File file = new File(filePath);
		if (file != null)
			return file.getName();
		return filePath;

	}

	/**
	 * Gets the integer value of an ID from the XML module that is like
	 * "XXX_integer"
	 * 
	 * @param xmlId
	 * @return the integer value
	 */
	public static int getIdFromXMLId(String xmlId) {
		if (xmlId == null)
			return -1;
		StringBuilder sb = new StringBuilder(xmlId);
		if (sb.indexOf("_") > 0) {
			try {
				return Integer.parseInt(sb.substring(sb.indexOf("_") + 1));
			} catch (NumberFormatException e) {
				return -1;
			}
		} else {
			try {
				return Integer.parseInt(xmlId);
			} catch (NumberFormatException e) {
				return -1;
			}
		}

	}

	/**
	 * Gets the prefixes for the XML identifiers
	 * 
	 * @author Salvador
	 * 
	 */
	public enum IdentifierPrefixes {
		PROTEIN("PROT_"), SOFTWARE("SW_"), SPECTRUM("SPEC_"), INPUTDATA(
				"INDATA_"), INPUTDATASET("INDATASET_"), PARAMETERS("PARAM_"), DIRECTDETECTION(
				"DD_"), DIRECTDETECTIONAGENT("DDA_"), INDIRECTDETECTION("ID_"), IMAGE(
				"IM_"), GELMATRIX("GM_"), SAMPLE("SMPL_"), LANE("LN_"), SUBSTANCE(
				"SUBS_"), EQUIPMENT("EQ_"), EPROTOCOL("EP_"), BUFFER("BU_"), PEPTIDE(
				"PEP_");
		private final String prefix;

		private IdentifierPrefixes(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return prefix;
		}
	}

	// General counters (for several modules)
	public static class SoftwareCounter extends MiapeIdentifierCounter {
	}

	public static class EquipmentCounter extends MiapeIdentifierCounter {
	}

	// Counters for MSI
	public static class SpectrumCounter extends MiapeIdentifierCounter {
	}

	// Counters for MSI
	public static class ProteinCounter extends MiapeIdentifierCounter {
	}

	public static class PeptideCounter extends MiapeIdentifierCounter {
	}

	public static class InputDataCounter extends MiapeIdentifierCounter {
	}

	public static class InputDataSetCounter extends MiapeIdentifierCounter {
	}

	public static class ParameterCounter extends MiapeIdentifierCounter {
	}

	// Counters for GE
	public static class LaneCounter extends MiapeIdentifierCounter {
	}

	public static class gelmatrixcounter extends MiapeIdentifierCounter {
	}

	public static class SampleCounter extends MiapeIdentifierCounter {
	}

	public static class ImageCounter extends MiapeIdentifierCounter {
	}

	public static class IndirectDetectionCounter extends MiapeIdentifierCounter {
	}

	public static class DirectDetectionCounter extends MiapeIdentifierCounter {
	}

	public static class SubstanceCounter extends MiapeIdentifierCounter {
	}

	/**
	 * 
	 * @param proteinSequence
	 * @param supportingPeptides
	 * @return a value from 0 to 1
	 */
	public static Double calculateProteinCoverage(String proteinSequence,
			List<ExtendedIdentifiedPeptide> supportingPeptides) {
		if (proteinSequence != null) {
			String replacedProteinSeq = proteinSequence.replace("I", "*");
			replacedProteinSeq = replacedProteinSeq.replace("L", "*");
			int[] cov = new int[replacedProteinSeq.length()];
			Set<String> peptideSequencesProcessed = new HashSet<String>();
			for (ExtendedIdentifiedPeptide peptide : supportingPeptides) {
				String seq = peptide.getSequence();

				if (seq != null) {
					if (!peptideSequencesProcessed.contains(seq)) {
						peptideSequencesProcessed.add(seq);
						seq = seq.replace("I", "*");
						seq = seq.replace("L", "*");

						final int index = replacedProteinSeq.indexOf(seq);
						if (index > -1) {
							for (int i = 0; i < seq.length(); i++) {
								cov[index + i] = 1;
							}
						} else {
							log.warn("The peptide "
									+ seq
									+ " doesn't fit with the sequence of the protein "
									+ replacedProteinSeq);
						}
					}
				}
			}
			// count all the "1" in the cov array
			int count = 0;
			for (int num : cov) {
				if (num == 1)
					count++;
			}
			if (count != 0 && replacedProteinSeq != null
					&& replacedProteinSeq.length() > 0)
				return (double) count / replacedProteinSeq.length();

		}

		return null;
	}

	/**
	 * 
	 * @param proteinSequence
	 * @param supportingPeptides
	 * @return a value from 0 to 1
	 */
	public static Double calculateProteinCoverage2(String proteinSequence,
			List<IdentifiedPeptide> supportingPeptides) {
		if (proteinSequence != null) {
			int[] cov = new int[proteinSequence.length()];
			for (IdentifiedPeptide peptide : supportingPeptides) {
				String seq = peptide.getSequence();
				if (seq != null) {
					final int index = proteinSequence.indexOf(seq);
					if (index > -1) {
						for (int i = 0; i < seq.length(); i++) {
							cov[index + i] = 1;
						}
					} else {
						log.warn("The peptide "
								+ peptide.getSequence()
								+ " doesn't fit with the sequence of the protein "
								+ proteinSequence);
					}
				}
			}
			// count all the "1" in the cov array
			int count = 0;
			for (int num : cov) {
				if (num == 1)
					count++;
			}
			if (count != 0 && proteinSequence != null
					&& proteinSequence.length() > 0)
				return (double) count / proteinSequence.length();

		}

		return null;
	}

}
