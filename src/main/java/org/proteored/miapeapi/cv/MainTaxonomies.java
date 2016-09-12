package org.proteored.miapeapi.cv;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MainTaxonomies extends ControlVocabularySet {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final String taxonomyFileName = "taxonomy-reviewed.tab";
	private static MainTaxonomies instance;

	protected MainTaxonomies(ControlVocabularyManager cvManager) {
		super(cvManager);

	}

	@Override
	public List<ControlVocabularyTerm> getPossibleValues() {
		return getAllTerms();
	}

	public static MainTaxonomies getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MainTaxonomies(null);
		return instance;

	}

	private List<ControlVocabularyTerm> getAllTerms() {

		final List<ControlVocabularyTerm> cachedPossibleValues = this
				.getCachedPossibleValues();
		if (cachedPossibleValues != null) {
			return cachedPossibleValues;
		}
		List<ControlVocabularyTerm> ret = new ArrayList<ControlVocabularyTerm>();

		File iFile;
		try {
			ClassLoader cl = this.getClass().getClassLoader();

			InputStream fis = cl.getResourceAsStream(taxonomyFileName);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			boolean firstLine = true;
			// Read File Line By Line
			log.info("Loading taxonomies from file: " + this.taxonomyFileName);
			while ((strLine = br.readLine()) != null) {
				if (!firstLine) {
					final String[] tabSepValues = strLine.trim().split("\\t");
					String taxon = tabSepValues[0];
					String commonName = tabSepValues[2];
					ControlVocabularyTerm cvTerm = new ControlVocabularyTermImpl2(
							new Accession(taxon), commonName, "NEWT");
					ret.add(cvTerm);
				}
				firstLine = false;
			}
			log.info(ret.size() + " taxonomies were loaded");

			// Close the input stream
			br.close();
			in.close();
			fis.close();
			this.setCachedPossibleValues(ret);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void main(String args[]) {
		final List<ControlVocabularyTerm> possibleValues = MainTaxonomies
				.getInstance(null).getPossibleValues();
		System.out.println(possibleValues.size() + " terms");
		for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
			System.out.println(controlVocabularyTerm.getTermAccession() + "-"
					+ controlVocabularyTerm.getPreferredName() + "-"
					+ controlVocabularyTerm.getCVRef());
		}
	}
}
