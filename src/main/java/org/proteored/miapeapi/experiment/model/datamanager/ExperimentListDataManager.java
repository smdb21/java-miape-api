package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.List;

import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.filters.Filter;

/**
 * Class that contains all proteins and peptides identified. It manage the fdr
 * filters and mantains sorted the list of proteins and peptides
 *
 * @author Salva
 *
 */
public class ExperimentListDataManager extends DataManager {

	public ExperimentListDataManager(IdentificationSet idSet, List<DataManager> dataManagers,
			boolean groupingAtExperimentListLevel, List<Filter> filters, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Integer minPeptideLength, boolean processInParallel) {
		super(idSet, dataManagers, doNotGroupNonConclusiveProteins, separateNonConclusiveProteins,
				groupingAtExperimentListLevel, filters, minPeptideLength, processInParallel);

	}

}
