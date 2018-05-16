package org.proteored.miapeapi.psimod;

import java.io.File;
import java.io.IOException;

import edu.scripps.yates.utilities.index.TextFileIndex;

public class PSIModOBOPlainTextReader {
	private final File psiOBOFile;
	private TextFileIndex index;
	private static PSIModOBOPlainTextReader instance;
	private static final String fileName = "PSI-MOD.obo";

	public PSIModOBOPlainTextReader() throws IOException {
		psiOBOFile = new File(getClass().getResource(fileName).getFile());
		index();
	}

	public static PSIModOBOPlainTextReader getInstance() throws IOException {
		if (instance == null) {
			instance = new PSIModOBOPlainTextReader();
		}
		return instance;
	}

	private void index() throws IOException {
		index = new TextFileIndex(psiOBOFile, new PSIModTextFileIndexIO(psiOBOFile));
		index.indexFile();
	}

	public String getTermString(String id) {
		final String item = index.getItem(id);
		return item;
	}

	/**
	 * PArse the formula from the entry in OBO as xref: Formula: "C 3 H 4 N 1 O
	 * 2"
	 * 
	 * @param psiModID
	 * @return
	 */
	public String getTermXRefFormula(String psiModID) {
		final String termString = getTermString(psiModID);
		final String[] split = termString.split("\n");
		for (String line : split) {
			if (line.startsWith("xref:")) {
				line = line.substring("xref:".length()).trim();
				if (line.startsWith("Formula:")) {
					line = line.substring("Formula:".length()).trim();
					if (line.startsWith("\"")) {
						line = line.substring(1);
					}
					if (line.endsWith("\"")) {
						line = line.substring(0, line.length() - 1);
					}
					return line.replace(" ", "");
				}
			}
		}
		return null;
	}

	/**
	 * PArse the formula from the entry in OBO as xref: DiffFormula: "C 0 H -1 N
	 * 0 O 0" 2"
	 * 
	 * @param psiModID
	 * @return
	 */
	public String getTermXRefDiffFormula(String psiModID) {
		final String termString = getTermString(psiModID);
		final String[] split = termString.split("\n");
		for (String line : split) {
			if (line.startsWith("xref:")) {
				line = line.substring("xref:".length()).trim();
				if (line.startsWith("DiffFormula:")) {
					line = line.substring("DiffFormula:".length()).trim();
					if (line.startsWith("\"")) {
						line = line.substring(1);
					}
					if (line.endsWith("\"")) {
						line = line.substring(0, line.length() - 1);
					}
					return line.replace(" ", "");
				}
			}
		}
		return null;
	}

	/**
	 * PArse the formula from the entry in OBO as xref: DiffMono: "-1.007825" 2"
	 * 
	 * @param psiModID
	 * @return
	 */
	public Double getTermXRefDiffMono(String psiModID) {
		final String termString = getTermString(psiModID);
		final String[] split = termString.split("\n");
		for (String line : split) {
			if (line.startsWith("xref:")) {
				line = line.substring("xref:".length()).trim();
				if (line.startsWith("DiffMono:")) {
					line = line.substring("DiffMono:".length()).trim();
					if (line.startsWith("\"")) {
						line = line.substring(1);
					}
					if (line.endsWith("\"")) {
						line = line.substring(0, line.length() - 1);
					}
					try {
						return Double.valueOf(line);
					} catch (final NumberFormatException e) {

					}
				}
			}
		}
		return null;
	}
}
