package org.proteored.miapeapi.experiment.model.sort;

import java.util.List;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.LocalOboTestControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.Score;

public enum SortingOrders {
	MAXQUANT_PTM_DELTA_SCORE_MS("MS:1001983", "MaxQuant:PTM Delta Score",
			Order.DESCENDANT), ASCORE_ASCORE_MS("MS:1001985", "Ascore:Ascore",
			Order.DESCENDANT), H_SCORE_MS("MS:1001986", "H-Score",
			Order.DESCENDANT), AMANDA_AMANDASCORE_MS("MS:1002319",
			"Amanda:AmandaScore", Order.DESCENDANT), SEQUEST_DELTACN_MS(
			"MS:1001156", "SEQUEST:deltacn", Order.DESCENDANT), SEQUEST_XCORR_MS(
			"MS:1001155", "SEQUEST:xcorr", Order.DESCENDANT), SEQUEST_PROBABILITY_MS(
			"MS:1001154", "SEQUEST:probability", Order.ASCENDANT), SPECTRUMMILL_DISCRIMINANT_SCORE_MS(
			"MS:1001580", "SpectrumMill:Discriminant Score", Order.DESCENDANT), MASCOT_PTM_SITE_ASSIGNMENT_CONFIDENCE_MS(
			"MS:1002012", "Mascot:PTM site assignment confidence",
			Order.DESCENDANT), SEQUEST_SF_MS("MS:1001160", "SEQUEST:sf",
			Order.DESCENDANT), SPECTRUMMILL_SCORE_MS("MS:1001572",
			"SpectrumMill:Score", Order.DESCENDANT), SPECTRUMMILL_SPI_MS(
			"MS:1001573", "SpectrumMill:SPI", Order.DESCENDANT), SEQUEST_TOTAL_IONS_MS(
			"MS:1001162", "SEQUEST:total ions", Order.DESCENDANT), SEQUEST_MATCHED_IONS_MS(
			"MS:1001161", "SEQUEST:matched ions", Order.DESCENDANT), PROTEINLYNX_LOG_LIKELIHOOD_MS(
			"MS:1001570", "ProteinLynx:Log Likelihood", Order.DESCENDANT), PROTEINLYNX_LADDER_SCORE_MS(
			"MS:1001571", "ProteinLynx:Ladder Score", Order.DESCENDANT), IDENTITYE_SCORE_MS(
			"MS:1001569", "IdentityE Score", Order.DESCENDANT), SCAFFOLD_PEPTIDE_PROBABILITY_MS(
			"MS:1001568", "Scaffold:Peptide Probability", Order.DESCENDANT), PTM_LOCALIZATION_SCORE_MS(
			"MS:1001968", "PTM localization score", Order.DESCENDANT), PROTEOMEDISCOVERER_PHOSPHORS_SCORE_MS(
			"MS:1001969", "ProteomeDiscoverer:phosphoRS score",
			Order.DESCENDANT), PROTEOMEDISCOVERER_PHOSPHORS_SITE_PROBABILITY_MS(
			"MS:1001971", "ProteomeDiscoverer:phosphoRS site probability",
			Order.ASCENDANT), PROTEOMEDISCOVERER_PHOSPHORS_SEQUENCE_PROBABILITY_MS(
			"MS:1001970", "ProteomeDiscoverer:phosphoRS sequence probability",
			Order.ASCENDANT), MSQUANT_PTM_SCORE_MS("MS:1001978",
			"MSQuant:PTM-score", Order.DESCENDANT), BYONIC__PEPTIDE_ABSLOGPROB_MS(
			"MS:1002309", "Byonic: Peptide AbsLogProb", Order.ASCENDANT), SONAR_SCORE_MS(
			"MS:1001502", "Sonar:Score", Order.DESCENDANT), MAXQUANT_PTM_SCORE_MS(
			"MS:1001979", "MaxQuant:PTM Score", Order.DESCENDANT), MSFIT_MOWSE_SCORE_MS(
			"MS:1001501", "MSFit:Mowse score", Order.DESCENDANT), PROFOUND_CLUSTERRANK_MS(
			"MS:1001500", "Profound:ClusterRank", Order.DESCENDANT), PROTEINSCAPE_SEQUESTMETASCORE_MS(
			"MS:1001506", "ProteinScape:SequestMetaScore", Order.DESCENDANT), DEBUNKER_SCORE_MS(
			"MS:1001974", "DeBunker:score", Order.DESCENDANT), PROTEINSCAPE_INTENSITYCOVERAGE_MS(
			"MS:1001505", "ProteinScape:IntensityCoverage", Order.DESCENDANT), PROTEINSCAPE_PFFSOLVERSCORE_MS(
			"MS:1001504", "ProteinScape:PFFSolverScore", Order.DESCENDANT), PROTEINSCAPE_PFFSOLVEREXP_MS(
			"MS:1001503", "ProteinScape:PFFSolverExp", Order.ASCENDANT), BYONIC_DELTA_SCORE_MS(
			"MS:1002263", "Byonic:Delta Score", Order.DESCENDANT), MYRIMATCH_MZFIDELITY_MS(
			"MS:1001590", "MyriMatch:mzFidelity", Order.DESCENDANT), BYONIC_DELTAMOD_SCORE_MS(
			"MS:1002264", "Byonic:DeltaMod Score", Order.DESCENDANT), FDRSCORE_MS(
			"MS:1001874", "FDRScore", Order.DESCENDANT), BYONIC_SCORE_MS(
			"MS:1002262", "Byonic:Score", Order.DESCENDANT), BYONIC_PEP_MS(
			"MS:1002265", "Byonic:PEP", Order.ASCENDANT), BYONIC_PEPTIDE_LOGPROB_MS(
			"MS:1002266", "Byonic:Peptide LogProb", Order.ASCENDANT), COMET_TOTAL_IONS_MS(
			"MS:1002259", "Comet:total ions", Order.DESCENDANT), MYRIMATCH_MVH_MS(
			"MS:1001589", "MyriMatch:MVH", Order.DESCENDANT), COMET_MATCHED_IONS_MS(
			"MS:1002258", "Comet:matched ions", Order.DESCENDANT), MAXQUANT_P_SITE_LOCALIZATION_PROBABILITY_MS(
			"MS:1001982", "MaxQuant:P-site localization probability",
			Order.ASCENDANT), BYONIC__PEPTIDE_ABSLOGPROB2D_MS("MS:1002311",
			"Byonic: Peptide AbsLogProb2D", Order.ASCENDANT), COMBINED_FDRSCORE_MS(
			"MS:1002125", "combined FDRScore", Order.DESCENDANT), MAXQUANT_PHOSPHO__STY__SCORE_DIFFS_MS(
			"MS:1001981", "MaxQuant:Phospho (STY) Score Diffs",
			Order.DESCENDANT), MAXQUANT_PHOSPHO__STY__PROBABILITIES_MS(
			"MS:1001980", "MaxQuant:Phospho (STY) Probabilities",
			Order.ASCENDANT), COMET_SPSCORE_MS("MS:1002255", "Comet:spscore",
			Order.DESCENDANT), COMET_DELTACNSTAR_MS("MS:1002254",
			"Comet:deltacnstar", Order.DESCENDANT), COMET_SPRANK_MS(
			"MS:1002256", "Comet:sprank", Order.DESCENDANT), MS_GF_RAWSCORE_MS(
			"MS:1002049", "MS-GF:RawScore", Order.DESCENDANT), SEQUEST_DELTACNSTAR_MS(
			"MS:1002250", "SEQUEST:deltacnstar", Order.DESCENDANT), COMET_DELTACN_MS(
			"MS:1002253", "Comet:deltacn", Order.DESCENDANT), COMET_XCORR_MS(
			"MS:1002252", "Comet:xcorr", Order.DESCENDANT), PERCOLATOR_PEP_MS(
			"MS:1001493", "percolator:PEP", Order.ASCENDANT), PERCOLATOR_SCORE_MS(
			"MS:1001492", "percolator:score", Order.DESCENDANT), MS_GF_ENERGY_MS(
			"MS:1002051", "MS-GF:Energy", Order.DESCENDANT), SQID_DELTASCORE_MS(
			"MS:1001888", "SQID:deltaScore", Order.DESCENDANT), PERCOLATOR_Q_VALUE_MS(
			"MS:1001491", "percolator:Q value", Order.DESCENDANT), SQID_SCORE_MS(
			"MS:1001887", "SQID:score", Order.DESCENDANT), PROFOUND_Z_VALUE_MS(
			"MS:1001498", "Profound:z value", Order.DESCENDANT), PROTEINSCAPE_PROFOUNDPROBABILITY_MS(
			"MS:1001497", "ProteinScape:ProfoundProbability", Order.ASCENDANT), PROTEINSCAPE_SEARCHEVENTID_MS(
			"MS:1001496", "ProteinScape:SearchEventId", Order.DESCENDANT), PROTEINSCAPE_SEARCHRESULTID_MS(
			"MS:1001495", "ProteinScape:SearchResultId", Order.DESCENDANT), MS_GF_PEP_MS(
			"MS:1002056", "MS-GF:PEP", Order.ASCENDANT), PHENYX_MODIF_MS(
			"MS:1001398", "Phenyx:Modif", Order.DESCENDANT), PHENYX_NUMBEROFMC_MS(
			"MS:1001397", "Phenyx:NumberOfMC", Order.DESCENDANT), PHENYX_PEPPVALUE_MS(
			"MS:1001396", "Phenyx:PepPvalue", Order.ASCENDANT), PHENYX_PEPZSCORE_MS(
			"MS:1001395", "Phenyx:Pepzscore", Order.ASCENDANT), MS_GF_SPECEVALUE_MS(
			"MS:1002052", "MS-GF:SpecEValue", Order.DESCENDANT), PHENYX_USER_MS(
			"MS:1001394", "Phenyx:User", Order.DESCENDANT), SEQUEST_SPSCORE_MS(
			"MS:1002248", "SEQUEST:spscore", Order.DESCENDANT), MS_GF_EVALUE_MS(
			"MS:1002053", "MS-GF:EValue", Order.DESCENDANT), PHENYX_AUTO_MS(
			"MS:1001393", "Phenyx:Auto", Order.DESCENDANT), MS_GF_QVALUE_MS(
			"MS:1002054", "MS-GF:QValue", Order.DESCENDANT), SEQUEST_SPRANK_MS(
			"MS:1002249", "SEQUEST:sprank", Order.DESCENDANT), MS_GF_PEPQVALUE_MS(
			"MS:1002055", "MS-GF:PepQValue", Order.ASCENDANT), OMSSA_EVALUE_MS(
			"MS:1001328", "OMSSA:evalue", Order.ASCENDANT), OMSSA_PVALUE_MS(
			"MS:1001329", "OMSSA:pvalue", Order.ASCENDANT), PROFOUND_CLUSTER_MS(
			"MS:1001499", "Profound:Cluster", Order.DESCENDANT), ZCORE_PROBSCORE_MS(
			"MS:1001952", "ZCore:probScore", Order.DESCENDANT), PEAKS_PEPTIDESCORE_MS(
			"MS:1001950", "PEAKS:peptideScore", Order.ASCENDANT), ANDROMEDA_SCORE_MS(
			"MS:1002338", "Andromeda:score", Order.DESCENDANT), X_TANDEM_EXPECT_MS(
			"MS:1001330", "X!Tandem:expect", Order.ASCENDANT), X_TANDEM_HYPERSCORE_MS(
			"MS:1001331", "X!Tandem:hyperscore", Order.DESCENDANT), PROTEINPROSPECTOR_EXPECTATION_VALUE_MS(
			"MS:1002045", "ProteinProspector:expectation value",
			Order.ASCENDANT), PROTEINPROSPECTOR_SCORE_MS("MS:1002044",
			"ProteinProspector:score", Order.DESCENDANT), MASCOT_TOTAL_IONS_MS(
			"MS:1001174", "Mascot:total ions", Order.DESCENDANT), MASCOT_EXPECTATION_VALUE_MS(
			"MS:1001172", "Mascot:expectation value", Order.ASCENDANT), MASCOT_MATCHED_IONS_MS(
			"MS:1001173", "Mascot:matched ions", Order.DESCENDANT), SPECTRAST_DELTA_MS(
			"MS:1001420", "SpectraST:delta", Order.DESCENDANT), SEQUEST_PEPTIDEIDNUMBER_MS(
			"MS:1001219", "SEQUEST:PeptideIdnumber", Order.ASCENDANT), SEQUEST_PEPTIDENUMBER_MS(
			"MS:1001218", "SEQUEST:PeptideNumber", Order.ASCENDANT), SEQUEST_PEPTIDERANKSP_MS(
			"MS:1001217", "SEQUEST:PeptideRankSp", Order.ASCENDANT), SEQUEST_PEPTIDESP_MS(
			"MS:1001215", "SEQUEST:PeptideSp", Order.ASCENDANT), MASCOT_SCORE_MS(
			"MS:1001171", "Mascot:score", Order.DESCENDANT), SPECTRAST_DOT_MS(
			"MS:1001417", "SpectraST:dot", Order.DESCENDANT), SPECTRAST_DOT_BIAS_MS(
			"MS:1001418", "SpectraST:dot_bias", Order.DESCENDANT), MASCOT_IDENTITY_THRESHOLD_MS(
			"MS:1001371", "Mascot:identity threshold", Order.DESCENDANT), MASCOT_HOMOLOGY_THRESHOLD_MS(
			"MS:1001370", "Mascot:homology threshold", Order.DESCENDANT), SPECTRAST_DISCRIMINANT_SCORE_F_MS(
			"MS:1001419", "SpectraST:discriminant score F", Order.DESCENDANT), MRMAID_PEPTIDE_SCORE_MS(
			"MS:1002221", "MRMaid:peptide score", Order.ASCENDANT), SEARCH_ENGINE_SPECIFIC_SCORE_FOR_PSMS_MS(
			"MS:1001143", "search engine specific score for PSMs",
			Order.DESCENDANT), LOCAL_FDR_MS("MS:1001250", "local FDR",
			Order.DESCENDANT), EXPECT_VALUE_MS("MS:1001192", "Expect value",
			Order.ASCENDANT), CONFIDENCE_SCORE_MS("MS:1001193",
			"confidence score", Order.DESCENDANT), P_VALUE_MS("MS:1001191",
			"p-value", Order.DESCENDANT), Q_VALUE_FOR_PEPTIDES_MS("MS:1001868",
			"q-value for peptides", Order.ASCENDANT), MANUAL_VALIDATION_MS(
			"MS:1001125", "manual validation", Order.DESCENDANT), P_VALUE_FOR_PEPTIDES_MS(
			"MS:1001870", "p-value for peptides", Order.ASCENDANT), E_VALUE_FOR_PEPTIDES_MS(
			"MS:1001872", "E-value for peptides", Order.ASCENDANT), PEPTIDE_IDENTIFICATION_CONFIDENCE_METRIC_MS(
			"MS:1001092", "peptide identification confidence metric",
			Order.ASCENDANT), P_VALUE_FOR_PROTEINS_MS("MS:1001871",
			"p-value for proteins", Order.DESCENDANT), E_VALUE_FOR_PROTEINS_MS(
			"MS:1001873", "E-value for proteins", Order.DESCENDANT), PARAGON_EXPRESSION_CHANGE_P_VALUE_MS(
			"MS:1001169", "Paragon:expression change p-value", Order.ASCENDANT), Q_VALUE_FOR_PROTEINS_MS(
			"MS:1001869", "q-value for proteins", Order.DESCENDANT), PROTEIN_IDENTIFICATION_CONFIDENCE_METRIC_MS(
			"MS:1001198", "protein identification confidence metric",
			Order.DESCENDANT), HIGHER_SCORE_BETTER_MS("MS:1002108",
			"higher score better", Order.DESCENDANT), LOWER_SCORE_BETTER_MS(
			"MS:1002109", "lower score better", Order.DESCENDANT), SEQUEST_SP_MS(
			"MS:1001157", "SEQUEST:sp", Order.DESCENDANT), SCAFFOLD_PROTEIN_PROBABILITY_MS(
			"MS:1001579", "Scaffold:Protein Probability", Order.ASCENDANT), PARAGON_SCORE_MS(
			"MS:1001166", "Paragon:score", Order.DESCENDANT), PARAGON_TOTAL_PROTSCORE_MS(
			"MS:1001165", "Paragon:total protscore", Order.DESCENDANT), PARAGON_EXPRESSION_ERROR_FACTOR_MS(
			"MS:1001168", "Paragon:expression error factor", Order.ASCENDANT), PARAGON_CONFIDENCE_MS(
			"MS:1001167", "Paragon:confidence", Order.DESCENDANT), PARAGON_UNUSED_PROTSCORE_MS(
			"MS:1001164", "Paragon:unused protscore", Order.DESCENDANT), SEQUEST_CONSENSUS_SCORE_MS(
			"MS:1001163", "SEQUEST:consensus score", Order.DESCENDANT), SEQUEST_UNIQ_MS(
			"MS:1001158", "SEQUEST:Uniq", Order.DESCENDANT), SEQUEST_EXPECTATION_VALUE_MS(
			"MS:1001159", "SEQUEST:expectation value", Order.ASCENDANT), BYONIC_BEST_SCORE_MS(
			"MS:1002269", "Byonic:Best Score", Order.DESCENDANT), PROTEINEXTRACTOR_SCORE_MS(
			"MS:1001507", "ProteinExtractor:Score", Order.DESCENDANT), BYONIC_PROTEIN_LOGPROB_MS(
			"MS:1002267", "Byonic:Protein LogProb", Order.DESCENDANT), BYONIC_BEST_LOGPROB_MS(
			"MS:1002268", "Byonic:Best LogProb", Order.DESCENDANT), BYONIC__PROTEIN_ABSLOGPROB_MS(
			"MS:1002310", "Byonic: Protein AbsLogProb", Order.DESCENDANT), COMET_EXPECTATION_VALUE_MS(
			"MS:1002257", "Comet:expectation value", Order.ASCENDANT), PHENYX_ID_MS(
			"MS:1001389", "Phenyx:ID", Order.DESCENDANT), PHENYX_PEPTIDES1_MS(
			"MS:1001391", "Phenyx:Peptides1", Order.ASCENDANT), PHENYX_SCORE_MS(
			"MS:1001390", "Phenyx:Score", Order.DESCENDANT), SQID_PROTEIN_SCORE_MS(
			"MS:1001889", "SQID:protein score", Order.DESCENDANT), MS_GF_DENOVOSCORE_MS(
			"MS:1002050", "MS-GF:DeNovoScore", Order.DESCENDANT), PHENYX_PEPTIDES2_MS(
			"MS:1001392", "Phenyx:Peptides2", Order.ASCENDANT), PEAKS_PROTEINSCORE_MS(
			"MS:1001951", "PEAKS:proteinScore", Order.DESCENDANT), PARAGON_CONTRIB_MS(
			"MS:1001170", "Paragon:contrib", Order.DESCENDANT), SEQUEST_TIC_MS(
			"MS:1001373", "SEQUEST:TIC", Order.DESCENDANT), SEQUEST_SEQUENCES_MS(
			"MS:1001372", "SEQUEST:Sequences", Order.DESCENDANT), SEQUEST_SUM_MS(
			"MS:1001374", "SEQUEST:Sum", Order.DESCENDANT), PHENYX_AC_MS(
			"MS:1001388", "Phenyx:AC", Order.DESCENDANT), SEARCH_ENGINE_SPECIFIC_SCORE_MS(
			"MS:1001153", "search engine specific score", Order.DESCENDANT), PROTEIN_RANK_MS(
			"MS:1001301", "protein rank", Order.DESCENDANT), PROTEOGROUPER_PAG_SCORE_MS(
			"MS:1002236", "ProteoGrouper:PAG score", Order.DESCENDANT), PROTEIN_AMBIGUITY_GROUP_RESULT_DETAILS_MS(
			"MS:1001147", "protein ambiguity group result details",
			Order.DESCENDANT), X_CORRELATION___2__PRIDE("PRIDE:0000015",
			"X correlation (+2)", Order.DESCENDANT), X_CORRELATION___3__PRIDE(
			"PRIDE:0000016", "X correlation (+3)", Order.DESCENDANT), X_CORRELATION___1__PRIDE(
			"PRIDE:0000014", "X correlation (+1)", Order.DESCENDANT), X_CORRELATION_PRIDE(
			"PRIDE:0000013", "X correlation", Order.DESCENDANT), EXPECT_PRIDE(
			"PRIDE:0000183", "expect", Order.ASCENDANT), DELTASTAR_PRIDE(
			"PRIDE:0000181", "deltastar", Order.DESCENDANT), ZSCORE_PRIDE(
			"PRIDE:0000182", "zscore", Order.DESCENDANT), DELTA_PRIDE(
			"PRIDE:0000180", "delta", Order.DESCENDANT), DOTPRODUCT_PRIDE(
			"PRIDE:0000179", "dotproduct", Order.DESCENDANT), X_TANDEM_PRIDE(
			"PRIDE:0000047", "X|Tandem", Order.DESCENDANT), CN_PRIDE(
			"PRIDE:0000052", "Cn", Order.DESCENDANT), DELTA_CN_PRIDE(
			"PRIDE:0000012", "Delta Cn", Order.DESCENDANT), DISCRIMINANT_SCORE_PRIDE(
			"PRIDE:0000138", "Discriminant score", Order.DESCENDANT), MASCOT_SCORE_PRIDE(
			"PRIDE:0000069", "Mascot score", Order.DESCENDANT), OMSSA_E_VALUE_PRIDE(
			"PRIDE:0000185", "OMSSA E-value", Order.DESCENDANT), OMSSA_P_VALUE_PRIDE(
			"PRIDE:0000186", "OMSSA P-value", Order.DESCENDANT), PEPTIDEPROPHET_PROBABILITY_SCORE_PRIDE(
			"PRIDE:0000099", "PeptideProphet probability score",
			Order.ASCENDANT), RANK_PRIDE("PRIDE:0000091", "Rank",
			Order.DESCENDANT), RANK_SP_PRIDE("PRIDE:0000050", "Rank/Sp",
			Order.DESCENDANT), SEQUEST_SCORE_PRIDE("PRIDE:0000053",
			"Sequest score", Order.DESCENDANT), SF_PRIDE("PRIDE:0000284", "Sf",
			Order.DESCENDANT), SP_PRIDE("PRIDE:0000054", "Sp", Order.DESCENDANT), SPECTRUM_MILL_PEPTIDE_SCORE_PRIDE(
			"PRIDE:0000177", "Spectrum Mill peptide score", Order.ASCENDANT), SLOMO_SCORE_PRIDE(
			"PRIDE:0000275", "SloMo score", Order.DESCENDANT), PEPSPLICE_SCORE_COUNT_PRIDE(
			"PRIDE:0000150", "PepSplice Score Count", Order.ASCENDANT), PROTEINPROPHET_PROBABILITY_SCORE_PRIDE(
			"PRIDE:0000100", "ProteinProphet probability score",
			Order.ASCENDANT), X_TANDEM_HYPERSCORE_PRIDE("PRIDE:0000176",
			"X!Tandem Hyperscore", Order.DESCENDANT), RSP_PRIDE(
			"PRIDE:0000062", "rsp", Order.DESCENDANT), Z_PRIDE("PRIDE:0000058",
			"z", Order.DESCENDANT), PEPSPLICE_DELTASCORE_PRIDE("PRIDE:0000149",
			"PepSplice Deltascore", Order.ASCENDANT), PEPSPLICE_FALSE_DISCOVERY_RATE_PRIDE(
			"PRIDE:0000147", "PepSplice False Discovery Rate", Order.ASCENDANT), PEPSPLICE_P_VALUE_PRIDE(
			"PRIDE:0000148", "PepSplice P-value", Order.ASCENDANT), PEPSPLICE_PENALTY_PRIDE(
			"PRIDE:0000151", "PepSplice Penalty", Order.ASCENDANT), INSPECT_MQSCORE_PRIDE(
			"PRIDE:0000214", "Inspect MQScore", Order.DESCENDANT), INSPECT_P_VALUE_PRIDE(
			"PRIDE:0000215", "Inspect p-value", Order.DESCENDANT), MASCOT_EXPECT_VALUE_PRIDE(
			"PRIDE:0000212", "Mascot expect value", Order.ASCENDANT), PROTEOMEDISCOVERER_MASCOT_PROTEIN_CUTOFF_SCORE_MS(
			"MS:1001660", "ProteomeDiscoverer:Mascot:Protein CutOff Score",
			Order.DESCENDANT), PROLUCID_XCORR("MS:1002534", "ProLuCID:xcorr",
			Order.DESCENDANT), PROLUCID_DELTACN("MS:1002535",
			"ProLuCID:deltacn", Order.DESCENDANT);
	private final String scoreName;
	private final Accession accession;
	private final Order sortingOrder;

	SortingOrders(String accession, String scoreName, Order sortingOrder) {
		this.scoreName = scoreName;
		this.accession = new Accession(accession);
		this.sortingOrder = sortingOrder;
	}

	public String getScoreName() {
		return scoreName;
	}

	public Accession getAccession() {
		return accession;
	}

	public Order getSortingOrder() {
		return sortingOrder;
	}

	public static Order getSortingOrder(String name) {
		final SortingOrders[] values = SortingOrders.values();
		for (SortingOrders sortingOrders : values) {
			if (sortingOrders.getScoreName().equalsIgnoreCase(name))
				return sortingOrders.getSortingOrder();
		}
		return null;

	}

	public static void main(String[] args) {
		ControlVocabularyManager cvManager = new LocalOboTestControlVocabularyManager();
		final List<ControlVocabularyTerm> possibleValues = Score.getInstance(
				cvManager).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
			String order = "Order.DESCENDANT";
			final String lowerCase = controlVocabularyTerm.getPreferredName()
					.toLowerCase();
			if (lowerCase.contains("exp") || lowerCase.contains("probabil")
					|| lowerCase.contains("pvalue")
					|| lowerCase.contains("pep"))
				order = "Order.ASCENDANT";

			System.out.println(controlVocabularyTerm.getPreferredName()
					.toUpperCase().replace(" ", "_").replace(":", "_")
					.replace("!", "_").replace("-", "_").replace("+", "_")
					.replace("/", "_").replace("|", "_").replace("(", "_")
					.replace(")", "_")
					+ "_"
					+ controlVocabularyTerm.getTermAccession().getCvRef()
					+ "("
					+ "\""
					+ controlVocabularyTerm.getTermAccession()
					+ "\",\""
					+ controlVocabularyTerm.getPreferredName()
					+ "\"," + order + "),");
		}
	}
}
