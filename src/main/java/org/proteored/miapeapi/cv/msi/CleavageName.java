package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class CleavageName extends ControlVocabularySet {
	/*
	 * CLEAVAGE_AGENT_NAME("MS:1001045", "cleavage agent name"),
	 * LYS_C("MS:1001309", "Lys-C"), TRYPSIN("MS:1001251", "Trypsin"),
	 * V8_DE("MS:1001314", "V8-DE"), V8_E("MS:1001315", "V8-E"),
	 * ARG_C("MS:1001303", "Arg-C"), TRYPCHYMO("MS:1001312", "TrypChymo"),
	 * ASP_N("MS:1001304", "Asp-N"), TRYPSIN_P("MS:1001313", "Trypsin/P"),
	 * NOENZYME("MS:1001091", "NoEnzyme"), ASP_N_AMBIC("MS:1001305",
	 * "Asp-N_ambic"), CHYMOTRYPSIN("MS:1001306", "Chymotrypsin"),
	 * CNBR("MS:1001307", "CNBr"), FORMIC_ACID("MS:1001308", "Formic_acid"),
	 * PEPSINA("MS:1001311", "PepsinA"), LYS_C_P("MS:1001310", "Lys-C/P");
	 */
	private static CleavageName instance;
	public static Accession CLEAVAGE_AGENT_NAME = new Accession("MS:1001045");

	public static CleavageName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new CleavageName(cvManager);
		return instance;
	}

	private CleavageName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { CLEAVAGE_AGENT_NAME.toString() };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 302;

	}
}
