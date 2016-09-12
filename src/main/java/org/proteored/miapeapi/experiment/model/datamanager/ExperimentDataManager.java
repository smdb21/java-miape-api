package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.experiment.model.filters.OccurrenceFilter;

/**
 * Class that contains all proteins and peptides identified. It manage the fdr
 * filters and maintains sorted the list of proteins and peptides
 *
 * @author Salva
 *
 */
public class ExperimentDataManager extends DataManager {
	private final OccurrenceFilter occurrenceFilter = null;

	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public ExperimentDataManager(IdentificationSet idSet, List<DataManager> dataManagers, List<Filter> filters,
			Integer minPeptideLength, boolean processInParallel) {
		super(idSet, dataManagers, minPeptideLength, filters, processInParallel);

	}

}
