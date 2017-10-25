package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class Score extends ControlVocabularySet {
	/*
	 * SEARCH_ENGINE_SPECIFIC_SCORE_FOR_PEPTIDES("MS:1001143",
	 * "search engine specific score for peptides", "MS"),
	 * SEQUEST_DELTACN("MS:1001156", "sequest:deltacn", "MS"),
	 * SEQUEST_XCORR("MS:1001155", "sequest:xcorr", "MS"),
	 * SEQUEST_PROBABILITY("MS:1001154", "sequest:probability", "MS"),
	 * SPECTRUMMILL__DISCRIMINANT_SCORE("MS:1001580",
	 * "SpectrumMill: Discriminant Score", "MS"), PERCOLAROR_PEP("MS:1001493",
	 * "percolaror:PEP", "MS"), PERCOLATOR_SCORE("MS:1001492",
	 * "percolator:score", "MS"), PERCOLATOR_Q_VALUE("MS:1001491",
	 * "percolator:Q value", "MS"), PROFOUND_Z_VALUE("MS:1001498",
	 * "Profound:z value", "MS"), PROTEINSCAPE_SEARCHEVENTID("MS:1001496",
	 * "ProteinScape:SearchEventId", "MS"),
	 * PROTEINSCAPE_SEARCHRESULTID("MS:1001495", "ProteinScape:SearchResultId",
	 * "MS"), PHENYX_MODIF("MS:1001398", "Phenyx:Modif", "MS"),
	 * PHENYX_NUMBEROFMC("MS:1001397", "Phenyx:NumberOfMC", "MS"),
	 * PHENYX_PEPPVALUE("MS:1001396", "Phenyx:PepPvalue", "MS"),
	 * PHENYX_PEPZSCORE("MS:1001395", "Phenyx:Pepzscore", "MS"),
	 * PHENYX_USER("MS:1001394", "Phenyx:User", "MS"), PHENYX_AUTO("MS:1001393",
	 * "Phenyx:Auto", "MS"), OMSSA_EVALUE("MS:1001328", "OMSSA:evalue", "MS"),
	 * SEQUEST_SF("MS:1001160", "sequest:sf", "MS"), OMSSA_PVALUE("MS:1001329",
	 * "OMSSA:pvalue", "MS"), PROFOUND_CLUSTER("MS:1001499", "Profound:Cluster",
	 * "MS"), SPECTRUMMILL__SCORE("MS:1001572", "SpectrumMill: Score", "MS"),
	 * SPECTRUMMILL__SPI("MS:1001573", "SpectrumMill: SPI", "MS"),
	 * SEQUEST_TOTAL_IONS("MS:1001162", "sequest:total ions", "MS"),
	 * SEQUEST_MATCHED_IONS("MS:1001161", "sequest:matched ions", "MS"),
	 * PROTEINLYNX__LOG_LIKELIHOOD("MS:1001570", "ProteinLynx: Log Likelihood",
	 * "MS"), PROTEINLYNX__LADDER_SCORE("MS:1001571",
	 * "ProteinLynx: Ladder Score", "MS"), IDENTITYE_SCORE("MS:1001569",
	 * "IdentityE Score", "MS"), SCAFFOLD__PEPTIDE_PROBABILITY("MS:1001568",
	 * "Scaffold: Peptide Probability", "MS"), XTANDEM_EXPECT("MS:1001330",
	 * "xtandem:expect", "MS"), XTANDEM_HYPERSCORE("MS:1001331",
	 * "xtandem:hyperscore", "MS"), MASCOT_TOTAL_IONS("MS:1001174",
	 * "mascot:total ions", "MS"), MASCOT_EXPECTATION_VALUE("MS:1001172",
	 * "mascot:expectation value", "MS"), MASCOT_MATCHED_IONS("MS:1001173",
	 * "mascot:matched ions", "MS"), SEQUEST_PEPTIDEIDNUMBER("MS:1001219",
	 * "sequest:PeptideIdnumber", "MS"), SPECTRAST_DELTA("MS:1001420",
	 * "SpectraST:delta", "MS"), SEQUEST_PEPTIDENUMBER("MS:1001218",
	 * "Sequest:PeptideNumber", "MS"), SEQUEST_PEPTIDERANKSP("MS:1001217",
	 * "Sequest:PeptideRankSp", "MS"), MASCOT_SCORE("MS:1001171",
	 * "Mascot:score", "MS"), SEQUEST_PEPTIDESP("MS:1001215",
	 * "sequest:PeptideSp", "MS"), SPECTRAST_DOT("MS:1001417", "SpectraST:dot",
	 * "MS"), SPECTRAST_DOT_BIAS("MS:1001418", "SpectraST:dot_bias", "MS"),
	 * MASCOT_IDENTITY_THRESHOLD("MS:1001371", "mascot:identity threshold",
	 * "MS"), MASCOT_HOMOLOGY_THRESHOLD("MS:1001370",
	 * "mascot:homology threshold", "MS"),
	 * SPECTRAST_DISCRIMINANT_SCORE_F("MS:1001419",
	 * "SpectraST:discriminant score F", "MS"), SONAR_SCORE("MS:1001502",
	 * "Sonar:Score", "MS"), MSFIT_MOWSE_SCORE("MS:1001501", "MSFit:Mowse score"
	 * , "MS"), PROFOUND_CLUSTERRANK("MS:1001500", "Profound:ClusterRank",
	 * "MS"), PROTEINSCAPE_SEQUESTMETASCORE("MS:1001506",
	 * "ProteinScape:SequestMetaScore", "MS"),
	 * PROTEINSCAPE_INTENSITYCOVERAGE("MS:1001505",
	 * "ProteinScape:IntensityCoverage", "MS"),
	 * PROTEINSCAPE_PFFSOLVERSCORE("MS:1001504", "ProteinScape:PFFSolverScore",
	 * "MS"), PROTEINSCAPE_PFFSOLVEREXP("MS:1001503",
	 * "ProteinScape:PFFSolverExp", "MS"),
	 * PROTEINSCAPE_PROFOUNDPROBABILITY("MS:1001597",
	 * "ProteinScape:ProfoundProbability", "MS"),
	 * PEPTIDE_QUALITY_ESTIMATION_MEASURE("MS:1001092",
	 * "peptide quality estimation measure", "MS"), LOCAL_FDR("MS:1001250",
	 * "local FDR", "MS"), MANUAL_VALIDATION("MS:1001125", "manual validation",
	 * "MS"), EXPECT_VALUE("MS:1001192", "Expect value", "MS"),
	 * CONFIDENCE_SCORE("MS:1001193", "confidence score", "MS"),
	 * P_VALUE("MS:1001191", "p-value", "MS"),
	 * PROTEIN_QUALITY_ESTIMATION_MEASURE("MS:1001198",
	 * "protein quality estimation measure", "MS"),
	 * SEARCH_ENGINE_SPECIFIC_SCORE("MS:1001153", "search engine specific score"
	 * , "MS"), PHENYX_ID("MS:1001389", "Phenyx:ID", "MS"),
	 * SEQUEST_SP("MS:1001157", "Sequest:sp", "MS"),
	 * PHENYX_PEPTIDES1("MS:1001391", "Phenyx:Peptides1", "MS"),
	 * PHENYX_SCORE("MS:1001390", "Phenyx:Score", "MS"),
	 * PHENYX_PEPTIDES2("MS:1001392", "Phenyx:Peptides2", "MS"),
	 * PARAGON_SCORE("MS:1001166", "paragon:score", "MS"),
	 * PARAGON_TOTAL_PROTSCORE("MS:1001165", "paragon:total protscore", "MS"),
	 * PARAGON_EXPRESSION_ERROR_FACTOR("MS:1001168",
	 * "paragon:expression error factor", "MS"),
	 * PARAGON_CONFIDENCE("MS:1001167", "paragon:confidence", "MS"),
	 * PARAGON_UNUSED_PROTSCORE("MS:1001164", "paragon:unused protscore", "MS"),
	 * SEQUEST_CONSENSUS_SCORE("MS:1001163", "sequest:consensus score", "MS"),
	 * SEQUEST_UNIQ("MS:1001158", "sequest:Uniq", "MS"),
	 * SEQUEST_EXPECTATION_VALUE("MS:1001159", "sequest:expectation value",
	 * "MS"), PARAGON_CONTRIB("MS:1001170", "paragon:contrib", "MS"),
	 * SEQUEST_TIC("MS:1001373", "sequest:TIC", "MS"),
	 * SEQUEST_SEQUENCES("MS:1001372", "sequest:Sequences", "MS"),
	 * PARAGON_EXPRESSION_CHANGE_P_VALUE("MS:1001169",
	 * "paragon:expression change p-value", "MS"), SEQUEST_SUM("MS:1001374",
	 * "sequest:Sum", "MS"), PROTEINEXTRACTOR_SCORE("MS:1001507",
	 * "ProteinExtractor:Score", "MS"), PHENYX_AC("MS:1001388", "Phenyx:AC",
	 * "MS"), PRIDE_DELTA_CN("PRIDE:0000012", "Delta Cn", "PRIDE"),
	 * PRIDE_TANDEM_HYPERSCORE("PRIDE:0000176", "X!Tandem Hyperscore", "PRIDE"),
	 * PRIDE_SPECTRUM_MILL_PEPTIDE_SCORE("PRIDE:0000177",
	 * "Spectrum Mill peptide score", "PRIDE"),
	 * PRIDE_SEQUEST_SCORE("PRIDE:0000053", "Sequest score", "PRIDE"),
	 * PRIDE_SP("PRIDE:0000054", "Sp", "PRIDE"), PRIDE_RANK_SP("PRIDE:0000050",
	 * "Rank/Sp", "PRIDE"), PRIDE_CN("PRIDE:0000052", "Cn", "PRIDE"),
	 * PRIDE_MASCOT_SCORE("PRIDE:0000069", "Mascot score", "PRIDE"),
	 * PRIDE_PEPTIDEPROPHET_PROBABILITY_SCORE("PRIDE:0000099",
	 * "PeptideProphet probability score", "PRIDE"),
	 * PRIDE_PROTEINPROPHET_PROBABILITY_SCORE("PRIDE:0000100",
	 * "ProteinProphet probability score", "PRIDE"),
	 * PRIDE_OMSSA_E_VALUE("PRIDE:0000185", "OMSSA E-value", "PRIDE"),
	 * PRIDE_OMSSA_P_VALUE("PRIDE:0000186", "OMSSA P-value", "PRIDE"),
	 * PRIDE_RSP("PRIDE:0000062", "rsp", "PRIDE"),
	 * PRIDE_DISCRIMINANT_SCORE("PRIDE:0000138", "Discriminant score", "PRIDE"),
	 * PRIDE_PEPSPLICE_SCORE_COUNT("PRIDE:0000150", "PepSplice Score Count",
	 * "PRIDE"), PRIDE_XTANDEM_HYPERSCORE("PRIDE:0000176", "X!Tandem Hyperscore"
	 * , "PRIDE");
	 */
	private static final Accession EXPECT_VALUE = new Accession("MS:1001192");
	private static final Accession XTANDEM_EVALUE_ACC = new Accession("MS:1001330");
	private static final Accession XTANDEM_HYPERSWCORE = new Accession("MS:1001331");
	private static final Accession LOCAL_FDR_ACC = new Accession("MS:1001250");
	private static final Accession SEQUEST_DELTA_CN = new Accession("MS:1001156");
	private static final Accession PROLUCID_DELTA_CN = new Accession("MS:1002535");
	private static final Accession SEQUEST_XCORR = new Accession("MS:1001155");
	private static final Accession PROLUCID_XCORR = new Accession("MS:1002534");
	private static final Accession SEQUEST_DELTACNSTAR = new Accession("MS:1002250");
	private static final Accession SEQUEST_SPSCORE = new Accession("MS:1002248");
	private static final Accession SEQUEST_SPRANK = new Accession("MS:1002249");
	private static final Accession COMET_XCORR = new Accession("MS:1002252");
	private static final Accession COMET_DELTACN = new Accession("MS:1002253");
	private static final Accession COMET_DELTACNSTAR = new Accession("MS:1002254");
	private static final Accession COMET_SPSCORE = new Accession("MS:1002255");
	private static final Accession COMET_SPRANK = new Accession("MS:1002256");
	private static Score instance;

	public static Score getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new Score(cvManager);
		return instance;
	}

	private Score(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001143", "MS:1001092", "MS:1001198", "MS:1001153", "MS:1001147",
				"PRIDE:0000013", "PRIDE:0000047", "MS:1002347", "MS:1002072", "MS:1001968" };
		parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = { "PRIDE:0000052", "PRIDE:0000012", "PRIDE:0000138", "PRIDE:0000069",
				"PRIDE:0000185", "PRIDE:0000186", "PRIDE:0000099", "PRIDE:0000091", "PRIDE:0000050", "PRIDE:0000053",
				"PRIDE:0000284", "PRIDE:0000054", "PRIDE:0000177", "PRIDE:0000275", "PRIDE:0000275", "PRIDE:0000150",
				"PRIDE:0000100", "PRIDE:0000176", "PRIDE:0000062", "PRIDE:0000058", "PRIDE:0000149", "PRIDE:0000147",
				"PRIDE:0000148", "PRIDE:0000151", "PRIDE:0000150", "PRIDE:0000214", "PRIDE:0000215", "PRIDE:0000212",
				"MS:1001579",
				// ProteomeDiscoverer:Mascot:Protein CutOff Score: to support to
				// ProCon:
				"MS:1001660" //
		}; // slomo
			// score,
			// pepsplice
		explicitAccessions = explicitAccessionsTMP;

		miapeSection = 308;

	}

	public static ControlVocabularyTerm getXTandemExpectValueTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(XTANDEM_EVALUE_ACC);
	}

	public static ControlVocabularyTerm getXTandemHyperScoreTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(XTANDEM_HYPERSWCORE);
	}

	public static ControlVocabularyTerm getLocalFDRTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(LOCAL_FDR_ACC);
	}

	public static ControlVocabularyTerm getSequestDeltaCNTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(SEQUEST_DELTA_CN);
	}

	public static ControlVocabularyTerm getCometDeltaCNTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMET_DELTACN);
	}

	public static ControlVocabularyTerm getProLuCIDDeltaCNTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(PROLUCID_DELTA_CN);
	}

	public static ControlVocabularyTerm getSequestXCorrTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(SEQUEST_XCORR);
	}

	public static ControlVocabularyTerm getProLuCIDXCorrCNTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(PROLUCID_XCORR);
	}

	public static ControlVocabularyTerm getCometXCorrTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMET_XCORR);
	}

	public static ControlVocabularyTerm getSequestDeltaCNStarTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(SEQUEST_DELTACNSTAR);

	}

	public static ControlVocabularyTerm getSequestSPScoreTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(SEQUEST_SPSCORE);

	}

	public static ControlVocabularyTerm getSequestSPRankTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(SEQUEST_SPRANK);

	}

	public static ControlVocabularyTerm getCometDeltaCNStarTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMET_DELTACNSTAR);

	}

	public static ControlVocabularyTerm getCometSPScoreTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMET_SPSCORE);

	}

	public static ControlVocabularyTerm getCometSPRankTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMET_SPRANK);

	}

	public static ControlVocabularyTerm getExpectValueTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(EXPECT_VALUE);
	}
}
