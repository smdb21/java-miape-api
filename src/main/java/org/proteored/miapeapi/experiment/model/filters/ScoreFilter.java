package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationItemEnum;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

import gnu.trove.set.hash.TIntHashSet;

public class ScoreFilter implements Filter {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final float threshold;
	private final ComparatorOperator operator;
	private boolean appliedToPeptides = false;
	private boolean appliedToProteins = false;
	private final String scoreName;

	private final Software software;

	/**
	 * 
	 * @param threshold
	 * @param scoreName
	 * @param includeOperator
	 *            indicate how to compare the scores with the threshold. Scores
	 *            that complies the comparison are not excluded in the filter
	 * @param item
	 */
	public ScoreFilter(float threshold, String scoreName, ComparatorOperator includeOperator,
			IdentificationItemEnum item, Software software) {
		this.operator = includeOperator;
		this.threshold = threshold;
		if (IdentificationItemEnum.PEPTIDE.equals(item))
			this.appliedToPeptides = true;
		else if (IdentificationItemEnum.PROTEIN.equals(item))
			this.appliedToProteins = true;
		this.scoreName = scoreName;
		this.software = software;
	}

	@Override
	public boolean equals(Object paramObject) {
		if (paramObject != null)
			if (paramObject instanceof ScoreFilter) {
				ScoreFilter filter = (ScoreFilter) paramObject;
				if (filter.threshold != this.threshold)
					return false;
				if (filter.scoreName != null) {
					if (!filter.scoreName.equals(this.scoreName))
						return false;
				} else {
					if (this.scoreName != null)
						return false;
				}
				if (filter.operator != this.operator)
					return false;
				if ((filter.appliedToPeptides ^ this.appliedToPeptides)
						|| (filter.appliedToProteins ^ this.appliedToProteins))
					return false;

				return true;
			}
		return super.equals(paramObject);
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
		if (appliedToPeptides) {
			List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
					.getPeptidesFromProteinGroupsInParallel(proteinGroups);
			TIntHashSet filteredPeptides = filterPeptides(identifiedPeptides);
			return DataManager.filterProteinGroupsByPeptides(proteinGroups, filteredPeptides,
					currentIdSet.getCvManager());

		} else {
			log.info("Filtering " + proteinGroups.size() + " protein groups by " + this.scoreName + " " + operator + " "
					+ threshold);
			List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
			for (ProteinGroup proteinGroup : proteinGroups) {
				for (ExtendedIdentifiedProtein identifiedProtein : proteinGroup) {

					Float scoreValue = identifiedProtein.getScore(scoreName);
					// pass the threshold even if the protein has not that
					// scoreName
					if (scoreValue == null || passThreshold(scoreValue))
						proteins.add(identifiedProtein);

				}
			}
			log.info("Running PAnalyzer before to return the groups in the Score filter");
			PAnalyzer pAnalyzer = new PAnalyzer(false);
			List<ProteinGroup> ret = pAnalyzer.run(proteins);
			log.info("Resulting " + ret.size() + " after filtering " + proteinGroups.size() + " protein groups");
			return ret;
		}
	}

	private boolean passThreshold(double scoreValue) {
		if (this.operator.equals(ComparatorOperator.EQUAL)) {
			return scoreValue == this.threshold;
		} else if (this.operator.equals(ComparatorOperator.LESS)) {
			return scoreValue < this.threshold;
		} else if (this.operator.equals(ComparatorOperator.LESS_OR_EQUAL)) {
			return scoreValue <= this.threshold;
		} else if (this.operator.equals(ComparatorOperator.MORE)) {
			return scoreValue > this.threshold;
		} else if (this.operator.equals(ComparatorOperator.MORE_OR_EQUAL)) {
			return scoreValue >= this.threshold;
		}
		throw new UnsupportedOperationException("The filter has not a valid comparator");
	}

	private TIntHashSet filterPeptides(List<ExtendedIdentifiedPeptide> identifiedPeptides) {
		log.info("Filtering " + identifiedPeptides.size() + " peptides by " + this.scoreName + " " + operator + " "
				+ threshold);

		TIntHashSet ret = new TIntHashSet();
		for (ExtendedIdentifiedPeptide identifiedPeptide : identifiedPeptides) {

			try {
				Float scoreValue = identifiedPeptide.getScore(scoreName);
				// pass the peptide even if it has not the scoreName
				if (scoreValue == null || passThreshold(scoreValue)) {
					if (!ret.contains(identifiedPeptide.getId()))
						ret.add(identifiedPeptide.getId());
					else
						log.info("This peptide has passed already the threshold");
				}
			} catch (NumberFormatException ex) {
				// do nothing
			}

		}
		log.info("Resulting " + ret.size() + " peptides after filtering " + identifiedPeptides.size() + " peptides");
		return ret;
	}

	private double getScore(ExtendedIdentifiedPeptide identifiedPeptide) {
		if (identifiedPeptide != null && identifiedPeptide.getScores() != null) {
			for (PeptideScore score : identifiedPeptide.getScores()) {
				if (score.getName().equalsIgnoreCase(scoreName))
					if (score.getValue() != null)
						return Double.valueOf(score.getValue());
			}
		}

		throw new NumberFormatException();
	}

	@Override
	public boolean appliedToProteins() {
		return this.appliedToProteins;
	}

	@Override
	public boolean appliedToPeptides() {
		return this.appliedToPeptides;
	}

	@Override
	public String toString() {
		String level;
		if (appliedToPeptides)
			level = "Peptide score";
		else
			level = "Protein score";
		String eq = "";
		if (operator.equals(ComparatorOperator.EQUAL))
			eq = "=";
		else if (operator.equals(ComparatorOperator.LESS))
			eq = "<";
		else if (operator.equals(ComparatorOperator.LESS_OR_EQUAL))
			eq = "<=";
		else if (operator.equals(ComparatorOperator.MORE))
			eq = ">";
		else if (operator.equals(ComparatorOperator.MORE_OR_EQUAL))
			eq = ">=";
		return level + " '" + scoreName + "' " + eq + " " + threshold;
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
