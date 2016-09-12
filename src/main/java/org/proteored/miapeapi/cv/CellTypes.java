package org.proteored.miapeapi.cv;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CellTypes extends ControlVocabularySet {
	private final String cellTypesFile = "cellTypes.tab";
	private final static Logger log = Logger.getLogger(CellTypes.class);
	private static CellTypes instance;

	public static CellTypes getInstance(ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new CellTypes(cvManager);
		return instance;
	}

	private CellTypes(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] tmpParentAccessions = { "CL:0000000" // whole body

		};
		parentAccessions = tmpParentAccessions;
		setExcludeParents(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.proteored.miapeapi.cv.ControlVocabularySet#getPossibleValues()
	 */
	@Override
	public List<ControlVocabularyTerm> getPossibleValues() {
		return getAllTerms();
	}

	private List<ControlVocabularyTerm> getAllTerms() {

		final List<ControlVocabularyTerm> cachedPossibleValues = getCachedPossibleValues();
		if (cachedPossibleValues != null) {
			return cachedPossibleValues;
		}
		List<ControlVocabularyTerm> ret = new ArrayList<ControlVocabularyTerm>();

		try {
			ClassLoader cl = this.getClass().getClassLoader();

			InputStream fis = cl.getResourceAsStream(cellTypesFile);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			boolean firstLine = true;
			// Read File Line By Line
			log.info("Loading cell types from file: " + cellTypesFile);
			while ((strLine = br.readLine()) != null) {
				if (!firstLine) {
					final String[] tabSepValues = strLine.trim().split("\\t");
					String accession = tabSepValues[0];
					String cellTypeName = tabSepValues[1];
					ControlVocabularyTerm cvTerm = new ControlVocabularyTermImpl2(
							new Accession(accession), cellTypeName, "CL");
					ret.add(cvTerm);
				}
				firstLine = false;
			}
			log.info(ret.size() + " cell types were loaded");

			// Close the input stream
			br.close();
			in.close();
			fis.close();
			setCachedPossibleValues(ret);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void main(String args[]) {
		final List<ControlVocabularyTerm> possibleValues = CellTypes
				.getInstance(null).getPossibleValues();

		for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
			System.out.println(controlVocabularyTerm.getTermAccession() + "-"
					+ controlVocabularyTerm.getPreferredName() + "-"
					+ controlVocabularyTerm.getCVRef());
		}
		System.out.println(possibleValues.size() + " terms");
	}
}
