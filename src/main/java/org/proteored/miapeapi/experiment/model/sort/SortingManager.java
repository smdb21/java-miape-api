package org.proteored.miapeapi.experiment.model.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinOccurrence;
import org.proteored.miapeapi.spring.SpringHandler;

public class SortingManager {
	private static SortingManager instance;
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");

	private ControlVocabularyManager cvManager;

	public static synchronized SortingManager getInstance() {

		if (instance == null) {
			instance = getInstance(SpringHandler.getInstance().getCVManager());
			// instance = getInstance(new
			// LocalOboTestControlVocabularyManager());
		}
		return instance;
	}

	public static synchronized SortingManager getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null) {
			instance = new SortingManager();

			instance.cvManager = cvManager;
			if (instance.cvManager == null)
				instance.cvManager = SpringHandler.getInstance().getCVManager();

		}
		return instance;
	}

	public SortingParameters getProteinOccurrenceSortingByProteinScore(
			ProteinOccurrence proteinOccurrence) {
		if (proteinOccurrence == null)
			return null;
		for (ExtendedIdentifiedProtein protein : proteinOccurrence
				.getItemList()) {
			SortingParameters sorting = getProteinSortingByProteinScore(protein);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getProteinOccurrenceSortingByPeptideScore(
			ProteinOccurrence proteinOccurrence) {
		if (proteinOccurrence == null)
			return null;
		for (ExtendedIdentifiedProtein protein : proteinOccurrence
				.getItemList()) {
			SortingParameters sorting = getProteinSortingByPeptideScore(protein);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getProteinSortingByPeptideScore(
			ExtendedIdentifiedProtein protein) {
		if (protein == null)
			return null;
		for (ExtendedIdentifiedPeptide peptide : protein.getPeptides()) {
			SortingParameters sorting = getPeptideSortingByPeptideScore(peptide);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getSortingParameters(String scoreName) {

		Order order = SortingOrders.getSortingOrder(scoreName);
		if (order != null) {
			final SortingParameters sortingParameters = new SortingParameters(
					scoreName, order);

			return sortingParameters;
		}
		// by default it will be descendant
		// log.warn("There is no definition of sorting order for the score '"
		// + scoreName
		// + "'. By default will be treated as sorted in DESCENDENT order.");
		final SortingParameters sortingParameters = new SortingParameters(
				scoreName, Order.DESCENDANT);

		return sortingParameters;
	}

	public SortingParameters getProteinSortingByProteinScore(
			ExtendedIdentifiedProtein protein) {
		if (protein.getSortingParameters() != null)
			return protein.getSortingParameters();
		String scoreNamesString = "";
		if (protein != null) {

			final List<String> scoreNames = protein.getScoreNames();
			if (scoreNames == null || scoreNames.isEmpty())
				return null;
			// sort scorenames alphabetically
			Collections.sort(scoreNames);
			for (String score : scoreNames) {
				if (!"".equals(score))
					scoreNamesString = scoreNamesString + "'" + score + "'";
			}
			// sort scorenames alphabetically
			Collections.sort(scoreNames);
			for (String scoreName : scoreNames) {
				return getSortingParameters(scoreName);
			}
		}
		return null;

	}

	public SortingParameters getPeptideOccurrenceSortingByPeptideScore(
			PeptideOccurrence peptideOccurrence) {
		if (peptideOccurrence == null)
			return null;
		for (ExtendedIdentifiedPeptide peptide : peptideOccurrence
				.getItemList()) {
			SortingParameters sorting = getPeptideSortingByPeptideScore(peptide);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getPeptideSortingByPeptideScore(
			ExtendedIdentifiedPeptide peptide) {

		if (peptide.getSortingParameters() != null)
			return peptide.getSortingParameters();

		String scoreNamesString = "";
		if (peptide != null) {
			final List<String> scoreNamesTMP = peptide.getScoreNames();
			List<String> scoreNames = new ArrayList<String>();
			for (String scoreName : scoreNamesTMP) {
				scoreNames.add(scoreName.toLowerCase());
			}
			if (scoreNames == null || scoreNames.isEmpty())
				return null;
			// sort scorenames alphabetically
			Collections.sort(scoreNames);
			for (String score : scoreNames) {
				if (!"".equals(score))
					scoreNamesString = scoreNamesString + score;
			}
			// sort scorenames alphabetically
			Collections.sort(scoreNames);
			for (String scoreName : scoreNames) {
				return getSortingParameters(scoreName);
			}
		}

		return null;
	}

	public SortingParameters getProteinGroupOccurrenceSortingByProteinScore(
			ProteinGroupOccurrence proteinGroupOccurrence) {
		if (proteinGroupOccurrence == null)
			return null;
		for (ProteinGroup proteinGroup : proteinGroupOccurrence.getItemList()) {
			SortingParameters sorting = getProteinGroupSortingByProteinScore(proteinGroup);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getProteinGroupSortingByProteinScore(
			ProteinGroup proteinGroup) {
		if (proteinGroup == null)
			return null;
		for (ExtendedIdentifiedProtein protein : proteinGroup) {
			SortingParameters sorting = getProteinSortingByProteinScore(protein);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getProteinGroupOccurrenceSortingByPeptideScore(
			ProteinGroupOccurrence proteinGroupOccurrence) {
		if (proteinGroupOccurrence == null)
			return null;
		for (ExtendedIdentifiedPeptide peptide : proteinGroupOccurrence
				.getPeptides()) {
			SortingParameters sorting = getPeptideSortingByPeptideScore(peptide);
			if (sorting != null)
				return sorting;
		}
		return null;
	}

	public SortingParameters getProteinGroupSortingByPeptideScore(
			ProteinGroup proteinGroup) {
		if (proteinGroup == null)
			return null;
		for (ExtendedIdentifiedPeptide peptide : proteinGroup.getPeptides()) {
			SortingParameters sorting = getPeptideSortingByPeptideScore(peptide);
			if (sorting != null)
				return sorting;
		}
		return null;
	}
}
