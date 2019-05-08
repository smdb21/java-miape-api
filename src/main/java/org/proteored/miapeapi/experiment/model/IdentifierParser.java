package org.proteored.miapeapi.experiment.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierParser {
	private static final String NEXTPROT_PREFIX = "nxp:NX_";
	private static final String NEXTPROT_PREFIX2 = "NX_";
	private static boolean remove_acc_version = false;

	public static boolean isRemove_acc_version() {
		return remove_acc_version;
	}

	public static void setRemove_acc_version(boolean remove_acc_version) {
		IdentifierParser.remove_acc_version = remove_acc_version;
	}

	/**
	 * Accessions like sp|P12345|PASD_HUMAN are parsed to P12345.<br>
	 * If remove acc version is set to true, accessions like P12345-2 are parsed to
	 * P12345.
	 * 
	 * @param accession
	 * @return
	 */
	public static String parseACC(String accession) {
		if (accession != null) {
			if (accession.toLowerCase().contains("reverse")) {
				return accession;
			}
			if (accession.toLowerCase().contains("decoy")) {
				return accession;
			}
			if (accession.contains("|")) {
				final String[] split = accession.split("\\|");
				if (split.length == 3) {
					accession = split[1];
				}
			}
			// P123123-2 to P123123
			if (remove_acc_version) {
				final String regexp = "(.*)-\\d(.*)";

				if (Pattern.matches(regexp, accession)) {
					final Pattern p = Pattern.compile(regexp);
					final Matcher m = p.matcher(accession);
					if (m.find()) {
						accession = m.group(1);
						if (m.groupCount() > 1)
							accession = accession + m.group(2);
					}

				}
			}
			if (accession.contains(NEXTPROT_PREFIX)) {
				return accession.replace(NEXTPROT_PREFIX, "");
			}
			if (accession.contains(NEXTPROT_PREFIX2)) {
				return accession.replace(NEXTPROT_PREFIX2, "");
			}
		}
		return accession;
	}
}
