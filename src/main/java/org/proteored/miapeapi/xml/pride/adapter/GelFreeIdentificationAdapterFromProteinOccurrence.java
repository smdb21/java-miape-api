package org.proteored.miapeapi.xml.pride.adapter;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.MatchedPeaks;
import org.proteored.miapeapi.cv.msi.ProteinDescription;
import org.proteored.miapeapi.cv.msi.ProteinGroupRelationship;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.cv.msi.ValidationType;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.ProteinOccurrence;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.xml.pride.autogenerated.GelFreeIdentificationType;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.autogenerated.Peptide;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class GelFreeIdentificationAdapterFromProteinOccurrence implements Adapter<GelFreeIdentificationType> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final ObjectFactory factory;
	private final ControlVocabularyManager cvManager;
	private final PrideControlVocabularyXmlFactory prideCvUtil;
	private final ProteinOccurrence proteinOccurrence;
	private final Float proteinLocalFDR;
	private final boolean includeSpectra;

	public GelFreeIdentificationAdapterFromProteinOccurrence(ObjectFactory factory, ControlVocabularyManager cvManager,
			ProteinOccurrence proteinOccurrence, Float proteinLocalFDR, boolean includeSpectra) {
		this.factory = factory;
		this.cvManager = cvManager;
		this.proteinOccurrence = proteinOccurrence;
		prideCvUtil = new PrideControlVocabularyXmlFactory(factory, cvManager);
		this.proteinLocalFDR = proteinLocalFDR;
		this.includeSpectra = includeSpectra;
	}

	@Override
	public GelFreeIdentificationType adapt() {
		final GelFreeIdentificationType gelFreeIdentification = factory.createGelFreeIdentificationType();

		String accession = null;
		try {
			accession = proteinOccurrence.getKey().trim();
		} catch (final UnsupportedOperationException ex) {
			accession = "Not assigned protein";
		}

		try {
			if (accession.contains(".")) {
				log.info("adding gelfreeidentification con version: " + accession);
				final String[] tmp = accession.split(".");
				accession = tmp[0];
				log.info("adding gelfreeidentification sin version: " + accession);
				final String accVersion = tmp[1];
				log.info("adding gelfreeidentification version: " + accVersion);
				gelFreeIdentification.setAccessionVersion(accVersion);
			}
		} catch (final IndexOutOfBoundsException ex) {
			// do nothing
		}
		log.debug("Adding gelFreeIndentification: " + accession);
		gelFreeIdentification.setAccession(accession);

		try {
			final Double proteinCoverage = proteinOccurrence.getProteinCoverage();
			if (proteinCoverage != null)
				gelFreeIdentification.setSequenceCoverage(Double.valueOf(proteinCoverage) / 100);
		} catch (final Exception e) {
			// DO NOTHING
		}
		try {
			// Get the best score of the proteinOccurrence:
			final Float proteinScoreValue = proteinOccurrence.getBestProteinScore();
			if (proteinScoreValue != null)
				// take the value of the score
				gelFreeIdentification.setScore(proteinScoreValue.doubleValue());

		} catch (final Exception e) {
			// DO NOTHING
		}
		// Check if databases are stated in MIAPE MSI. Only the first will be
		// added to the protein
		final List<Database> proteinDatabases = proteinOccurrence.getProteinDatabases();
		if (proteinDatabases != null) {
			final String dataBaseNames = getDatabaseNameString(proteinDatabases);
			if (!"".equals(dataBaseNames)) {
				gelFreeIdentification.setDatabase(dataBaseNames);

				final String numVersion = getDatabaseVersionString(proteinDatabases);
				if (numVersion != null && !"".equals(numVersion) && !dataBaseNames.equals(numVersion))
					gelFreeIdentification.setDatabaseVersion(numVersion);
			}
		}
		final List<Software> softwares = proteinOccurrence.getSoftwares();
		final String softwareNames = getSoftwareNamesString(softwares);
		if (!"".equals(softwareNames))
			gelFreeIdentification.setSearchEngine(softwareNames);

		addPeptides(gelFreeIdentification, proteinOccurrence.getPeptides());

		addAdditionalParameters(gelFreeIdentification);
		return gelFreeIdentification;

	}

	private String getSoftwareNamesString(List<Software> softwares) {
		String softwareNames = "";
		final Set<String> set = new THashSet<String>();
		if (softwares != null)
			for (final Software software : softwares) {
				set.add(software.getName());

			}
		for (final String string : set) {
			if (!"".equals(softwareNames))
				softwareNames = softwareNames + ", ";
			softwareNames = softwareNames + string;
		}
		return softwareNames;
	}

	private String getDatabaseNameString(List<Database> databases) {
		String dataBaseNames = "";
		final Set<String> set = new THashSet<String>();
		if (databases != null)
			for (final Database database : databases) {
				set.add(database.getName());
			}
		for (final String string : set) {
			if (!"".equals(dataBaseNames))
				dataBaseNames = dataBaseNames + ", ";
			dataBaseNames = dataBaseNames + string;
		}

		return dataBaseNames;
	}

	private String getDatabaseVersionString(List<Database> databases) {
		String dataBaseVersions = "";
		final Set<String> set = new THashSet<String>();
		if (databases != null)
			for (final Database database : databases) {
				set.add(database.getNumVersion());
			}
		for (final String version : set) {
			if (!"".equals(dataBaseVersions))
				dataBaseVersions = dataBaseVersions + ", ";
			dataBaseVersions = dataBaseVersions + version;
		}

		if (dataBaseVersions.length() > 30) {
			log.info("Truncating database version to 30 characteres allowed in PRIDE DB");
			dataBaseVersions = dataBaseVersions.substring(0, 29);
		}
		return dataBaseVersions;
	}

	private void addPeptides(GelFreeIdentificationType xmlProtein, List<ExtendedIdentifiedPeptide> peptideList) {
		// log.info("adding peptides");
		final TIntHashSet peptideIds = new TIntHashSet();
		for (final ExtendedIdentifiedPeptide identifiedPeptide : peptideList) {
			if (!peptideIds.contains(identifiedPeptide.getId())) {
				peptideIds.add(identifiedPeptide.getId());
				try {
					final Peptide xmlPeptide = new PeptideAdapter(factory, cvManager, identifiedPeptide, includeSpectra)
							.adapt();
					xmlProtein.getPeptideItem().add(xmlPeptide);
				} catch (final IllegalMiapeArgumentException e) {
					log.warn(e.getMessage());
				}
			}
		}

	}

	private void addAdditionalParameters(GelFreeIdentificationType gelFreeIdentification) {
		try {
			if (gelFreeIdentification.getAdditional() == null)
				gelFreeIdentification.setAdditional(factory.createParamType());
			// Get the info from one protein of the occurrence
			final ExtendedIdentifiedProtein protein = proteinOccurrence.getFirstOccurrence();
			// TODO state the protein inference CVPARAM

			final ProteinEvidence evidence = protein.getEvidence();
			if (evidence != null) {
				final ControlVocabularyTerm cvTerm = getProteinGroupTerm(evidence);
				if (cvTerm != null) {
					prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(), cvTerm.getTermAccession(),
							cvTerm.getPreferredName(), null, cvTerm.getCVRef());
				} else {
					final ControlVocabularyTerm proteinInferenceConfidenceCategory = ProteinGroupRelationship
							.getInstance(cvManager)
							.getCVTermByAccession(ProteinGroupRelationship.PROTEIN_INFERENCE_CONFIDENCE_CATEGORY);
					prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(),
							proteinInferenceConfidenceCategory.getTermAccession(),
							proteinInferenceConfidenceCategory.getPreferredName(), evidence.toString(),
							proteinInferenceConfidenceCategory.getCVRef());
				}
			}

			if (protein.getDescription() != null && !"".equals(protein.getDescription())) {
				prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(),
						ProteinDescription.PSI_PROTEIN_DESCRIPTION.getTermAccession(),
						ProteinDescription.PSI_PROTEIN_DESCRIPTION.getPreferredName(), protein.getDescription(),
						ProteinDescription.PSI_PROTEIN_DESCRIPTION.getCVRef());
			}
			if (protein.getValidationStatus() != null && !"".equals(protein.getValidationStatus())) {
				prideCvUtil.addUserParamToParamType(gelFreeIdentification.getAdditional(),
						PrideControlVocabularyXmlFactory.VALIDATION_STATUS_TEXT,
						protein.getValidationStatus().toString());
			}
			if (protein.getValidationType() != null && !"".equals(protein.getValidationType())) {
				prideCvUtil.addCvParamOrUserParamToParamType(gelFreeIdentification.getAdditional(),
						protein.getValidationType(), protein.getValidationValue(),
						ValidationType.getInstance(cvManager));
			}
			if (protein.getPeaksMatchedNumber() != null && !"".equals(protein.getPeaksMatchedNumber())) {
				prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(),
						MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getTermAccession(),
						MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getPreferredName(), protein.getPeaksMatchedNumber(),
						MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getCVRef());
			}
			if (protein.getUnmatchedSignals() != null && !"".equals(protein.getUnmatchedSignals())) {
				prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(),
						MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getTermAccession(),
						MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getPreferredName(), protein.getUnmatchedSignals(),
						MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getCVRef());
			}

			// Report the number of occurrences of a protein
			final int numOccurrences = proteinOccurrence.getItemList().size();
			// Just in case of being more than 1
			if (numOccurrences > 1)
				prideCvUtil.addUserParamToParamType(gelFreeIdentification.getAdditional(), "Protein occurrence",
						String.valueOf(numOccurrences));

			// Report all the scores of the protein occurrence
			final Set<String> scoreNames = proteinOccurrence.getScoreNames();
			if (scoreNames != null)
				for (final String scoreName : scoreNames) {
					final TFloatArrayList scoreValues = proteinOccurrence.getScoreValues(scoreName);
					final String scoreValuesString = getScoreValuesString(scoreValues);
					prideCvUtil.addCvParamOrUserParamToParamType(gelFreeIdentification.getAdditional(), scoreName,
							scoreValuesString, Score.getInstance(cvManager));
				}

			// Local FDR
			if (proteinLocalFDR != null) {
				final ControlVocabularyTerm localFDRTerm = Score.getLocalFDRTerm(cvManager);
				if (localFDRTerm != null) {
					prideCvUtil.addCvParamToParamType(gelFreeIdentification.getAdditional(),
							localFDRTerm.getTermAccession(), localFDRTerm.getPreferredName(),
							proteinLocalFDR.toString(), localFDRTerm.getCVRef());
				}
			}

			// In order to support PRIDE Viewer bug, if additional is empty, put
			// something
			if (gelFreeIdentification.getAdditional().getCvParamOrUserParam() == null
					|| gelFreeIdentification.getAdditional().getCvParamOrUserParam().isEmpty()) {
				prideCvUtil.addCvParamOrUserParamToParamType(gelFreeIdentification.getAdditional(),
						"No additional information", null, null);
			}
		} catch (final UnsupportedOperationException ex) {
			// do nothing
		}
	}

	private ControlVocabularyTerm getProteinGroupTerm(ProteinEvidence evidence) {
		if (evidence.equals(ProteinEvidence.AMBIGUOUSGROUP))
			return ProteinGroupRelationship.getInstance(cvManager)
					.getCVTermByAccession(ProteinGroupRelationship.PANALYZER_AMBIGUOUS_GROUP_MEMBER_PROTEIN);
		if (evidence.equals(ProteinEvidence.CONCLUSIVE))
			return ProteinGroupRelationship.getInstance(cvManager)
					.getCVTermByAccession(ProteinGroupRelationship.PANALYZER_CONCLUSIVE_PROTEIN);
		if (evidence.equals(ProteinEvidence.INDISTINGUISHABLE))
			return ProteinGroupRelationship.getInstance(cvManager)
					.getCVTermByAccession(ProteinGroupRelationship.PANALYZER_INDISTINGUISABLE_PROTEIN);
		if (evidence.equals(ProteinEvidence.NONCONCLUSIVE))
			return ProteinGroupRelationship.getInstance(cvManager)
					.getCVTermByAccession(ProteinGroupRelationship.PANALYZER_NON_CONCLUSIVE_PROTEIN);
		return null;
	}

	private String getScoreValuesString(TFloatArrayList scoreValues) {
		String scoreValuesString = "";
		if (scoreValues != null)
			for (final float value : scoreValues.toArray()) {
				if (!"".equals(scoreValuesString))
					scoreValuesString = scoreValuesString + ", ";
				scoreValuesString = scoreValuesString + String.valueOf(value);
			}
		return scoreValuesString;
	}
}
