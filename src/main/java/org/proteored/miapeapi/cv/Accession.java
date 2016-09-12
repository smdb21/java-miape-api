package org.proteored.miapeapi.cv;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Accession {

	private String accession;
	private String cvRef;
	private final static String REGEXP = "(.*):.*";
	private final static Pattern myPattern = Pattern.compile(REGEXP);

	public Accession(String accession) {

		this.accession = accession;
		cvRef = getCVRefFromAccession(accession);
		String cvRefSynonym = getValidCVRef(cvRef);
		if (cvRefSynonym != null) {
			this.accession = cvRefSynonym + ":" + getAccessionWithoutCVRef();
			cvRef = cvRefSynonym;
		}
	}

	@Override
	public boolean equals(Object obj) {
		String cadena = null;
		if (obj instanceof Accession) {
			cadena = ((Accession) obj).toString();
		} else if (obj instanceof String) {
			cadena = new Accession((String) obj).toString();
		}

		final String accessionNumber = getAccessionWithoutCVRef();
		String validCVRef = getValidCVRef(cvRef);
		if (validCVRef == null)
			validCVRef = cvRef;
		if (cadena.equalsIgnoreCase(validCVRef + ":" + accessionNumber))
			return true;

		return false;
	}

	public String getCvRef() {
		return cvRef;
	}

	@Override
	public String toString() {
		return accession;
	}

	public String toLowerCase() {
		return accession.toLowerCase();
	}

	public String toUpperCase() {
		return accession.toUpperCase();
	}

	/**
	 * From "MS:100000" it returns "MS"
	 * 
	 * @param accession
	 * @return
	 */
	private String getCVRefFromAccession(String accession) {
		// TODO test it
		// get all before the ":"

		Matcher m = myPattern.matcher(accession);
		while (m.find()) {
			if (!"".equals(m.group(1)))
				return m.group(1).toUpperCase();
		}

		return null;
	}

	/**
	 * Get synonymous of a CVRef
	 * 
	 * @param cvRef
	 * @return the synonymus
	 */
	private String getValidCVRef(String cvRef) {
		HashMap<String, String> synonyms = createSynonymDicctionary();
		if (synonyms.containsKey(cvRef)) {
			return synonyms.get(cvRef);
		}
		return null;
	}

	/**
	 * This function will be updated to handle new synonyms in the ontologies ID
	 * 
	 * @return
	 */
	private HashMap<String, String> createSynonymDicctionary() {
		HashMap<String, String> synonym = new HashMap<String, String>();

		// Sinonimos de "MS"
		// List<String> lista = new ArrayList<String>();
		// lista.add("PSI");
		// synonym.put(PSIMassSepctrometryOntology.getCVLabel(), lista);

		// sinonimos de "PSI"
		synonym.put("PSI", PSIMassSpectrometryOntology.getCVLabel());

		return synonym;
	}

	/**
	 * From "MS:100000" it returns "100000"
	 * 
	 * @param accession
	 * @return
	 */
	private String getAccessionWithoutCVRef() {
		// TODO test it
		// get all before the ":"
		final String regexp = ".*:(.*)";
		if (Pattern.matches(regexp, accession.toString())) {
			Pattern myPattern = Pattern.compile(regexp);
			Matcher m = myPattern.matcher(accession.toString());
			while (m.find()) {
				if (!"".equals(m.group(1)))
					return m.group(1).toUpperCase();
			}
		}
		return null;
	}
}
