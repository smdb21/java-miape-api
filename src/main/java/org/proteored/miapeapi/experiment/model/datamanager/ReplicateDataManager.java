package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.List;

import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

/**
 * Class that contains all proteins and peptides identified. It manage the fdr
 * filters and mantains sorted the list of proteins and peptides
 *
 * @author Salva
 *
 */
public class ReplicateDataManager extends DataManager {

	/**
	 * Create an identification set from the data extracted from a
	 * {@link MiapeMSIDocument}
	 *
	 * @param experimentName
	 *
	 * @param miapeMSIs
	 */
	public ReplicateDataManager(IdentificationSet idSet, List<MiapeMSIDocument> miapeMSIs, List<Filter> filters,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Integer minPeptideLength,
			boolean processInParallel) {

		super(idSet, miapeMSIs, filters, doNotGroupNonConclusiveProteins, separateNonConclusiveProteins,
				minPeptideLength, processInParallel);

	}

	// public ReplicateDataManager(IdentificationSet idSet,
	// List<MiapeMSIDocument> miapeMSIs, List<Filter> filters) {
	//
	// super(idSet, miapeMSIs, filters, null);
	//
	// }

}
