package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ProteinGroup;

import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.set.hash.TIntHashSet;

public class PeptidesFromProteinGroupsParallelExtractor extends Thread {

	private final ParIterator<ProteinGroup> iterator;
	private final Reducible<List<ExtendedIdentifiedPeptide>> reduciblePeptides;
	private final int minPeptideLength;

	public PeptidesFromProteinGroupsParallelExtractor(ParIterator<ProteinGroup> iterator,
			Reducible<List<ExtendedIdentifiedPeptide>> reduciblePeptides, int minPeptideLength) {
		this.iterator = iterator;
		this.reduciblePeptides = reduciblePeptides;
		this.minPeptideLength = minPeptideLength;
	}

	@Override
	public void run() {
		List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		TIntHashSet peptideIds = new TIntHashSet();
		reduciblePeptides.set(ret);

		while (iterator.hasNext()) {
			try {
				ProteinGroup proteinGroup = iterator.next();
				final List<ExtendedIdentifiedPeptide> peptidesFromProteins = proteinGroup.getPeptides();
				if (peptidesFromProteins != null && !peptidesFromProteins.isEmpty()) {
					for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptidesFromProteins) {
						// Do not take the same peptide several times ->
						// This happens when a peptide is shared by several
						// proteins
						if (!peptideIds.contains(extendedIdentifiedPeptide.getId())) {
							if (minPeptideLength > extendedIdentifiedPeptide.getSequence().length())
								continue;
							ret.add(extendedIdentifiedPeptide);
							peptideIds.add(extendedIdentifiedPeptide.getId());
						} else {
							// log.info("Peptide is already in the list");
						}
					}
				} else {
					// log.warn("this protein has no peptides!: " +
					// protein.getAccession());
				}
			} catch (Exception e) {
				iterator.register(e);
			}
		}
	}

}
