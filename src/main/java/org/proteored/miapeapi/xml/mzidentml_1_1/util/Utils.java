package org.proteored.miapeapi.xml.mzidentml_1_1.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

public class Utils {

	public static final String SITEREGEXP = "Site Regular Expresion";
	public static final String MISSCLEAVAGES = "Allowed misscleavages";
	public static final String SEMISPECIFIC = "Semispecific";
	public static final String NTERM_GAIN = "formula gained at NTerm";
	public static final String CTERM_GAIN = "formula gained at CTerm";
	public static final String MIN_DISTANCE = "minimal distance for another cleavage";
	public static final String RESIDUES = "residues";
	public static final String MASS_DELTA = "mass delta";
	public static final String FIXED = "Fixed";
	public static final String VARIABLE = "Variable";
	public static final String FILTER_TYPE = "Filter type";
	public static final String EXCLUDE = "Excluded sequences";
	public static final String INCLUDE = "Included sequences";
	public static final String EXTERNAL_FORMAT_DOCUMENTATION = "External format documentation";
	public static final String AVERAGE_MASS_DELTA = "Average Mass Delta";
	public static final String MONOISOTOPIC_MASS_DELTA = "Monoisotopic Mass Delta";
	public static final String LOCATION = "Location";

	public static final String SOURCE_FILE_NAME = "Source file name";
	public static final String FILE_FORMAT = "File format";
	public static final String AUTHORS = "Authors";
	public static final String EDITOR = "Editor";
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String ISSUE = "Issue";
	public static final String PAGES = "Pages";
	public static final String TITLE = "Title";
	public static final String PUBLISHER = "Publisher";
	public static final String YEAR = "Year";
	public static final String VOLUME = "Volume";
	public static final String PUBLICATION = "Publication";
	public static final String SUBSTITUTION_MODIFICATION = "Substitution modification";

	public static Map<String, AbstractParam> initParamMap(
			List<AbstractParam> list) {
		Map<String, AbstractParam> map = new HashMap<String, AbstractParam>();
		if (list != null) {
			for (AbstractParam param : list) {
				if (param instanceof CvParam) {
					CvParam cvParam = (CvParam) param;
					map.put(cvParam.getAccession(), cvParam);
				} else {
					UserParam userParam = (UserParam) param;
					map.put(param.getName(), userParam);
				}
			}
		}
		return map;
	}

	public static String writeParam(List<AbstractParam> paramGroup) {
		StringBuilder sb = new StringBuilder();
		for (AbstractParam param : paramGroup) {
			sb.append(MzidentmlControlVocabularyXmlFactory
					.readEntireParam(param));
		}
		return sb.toString();
	}

	/**
	 * Transforms a String in a identifier: without blank spaces
	 * 
	 * @return the identifier
	 */
	public static String getIdFromString(String value) {
		String temp = new String(value);
		temp = temp.replace(" ", "_");
		return temp.toString() + "_Ref";
	}

	public static String checkReturnedString(StringBuilder sb) {
		// check if the string ends with TERM_SEPARATOR
		String string = sb.toString();
		if (string.endsWith(MiapeXmlUtil.TERM_SEPARATOR)) {
			sb.substring(1, sb.length());
		}
		// check if string is ""
		if (!string.equals(""))
			return string;

		return null;
	}

}
