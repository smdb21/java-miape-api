package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.msi.IdentifiedProteinSetBuilder;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentBuilder;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentFactory;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import gnu.trove.set.hash.THashSet;

public class LightMiapeMSIAdapter implements Adapter<MiapeMSIDocument> {
	private final MiapeMSIDocument miapeMSI;

	public LightMiapeMSIAdapter(MiapeMSIDocument miapeMSIDocument) {
		miapeMSI = miapeMSIDocument;
	}

	@Override
	public MiapeMSIDocument adapt() {
		Project project = null;
		if (miapeMSI.getProject() != null) {
			project = MiapeDocumentFactory.createProjectBuilder(miapeMSI.getProject().getName()).build();
		}
		final MiapeMSIDocumentBuilder builder = MiapeMSIDocumentFactory.createMiapeDocumentMSIBuilder(project,
				miapeMSI.getName(), null);
		builder.id(miapeMSI.getId());

		final List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
		for (final IdentifiedPeptide peptide : miapeMSI.getIdentifiedPeptides()) {
			peptides.add(MiapeMSIDocumentFactory.createIdentifiedPeptideBuilder(peptide.getSequence())
					.id(peptide.getId()).build());
		}
		builder.identifiedPeptides(peptides);

		final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();
		for (final IdentifiedProteinSet proteinSet : miapeMSI.getIdentifiedProteinSets()) {
			final IdentifiedProteinSetBuilder proteinSetBuilder = MiapeMSIDocumentFactory
					.createIdentifiedProteinSetBuilder(proteinSet.getName());
			for (final IdentifiedProtein protein : proteinSet.getIdentifiedProteins().values()) {
				final IdentifiedProtein newProtein = MiapeMSIDocumentFactory
						.createIdentifiedProteinBuilder(protein.getAccession()).id(protein.getId()).build();
				proteinSetBuilder.identifiedProtein(newProtein);
			}
			proteinSets.add(proteinSetBuilder.build());
		}
		builder.identifiedProteinSets(proteinSets);

		return builder.build();
	}

}
