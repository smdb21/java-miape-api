package org.proteored.miapeapi.proteomicsmodel;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import edu.scripps.yates.utilities.proteomicsmodel.AbstractPSM;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMPosition;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PTMEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;

public class PSMFromMIAPEMSI extends AbstractPSM {
	private final IdentifiedPeptide identifiedPeptide;
	private final MSRunProviderByInputData msRunProviderByInputData;

	public PSMFromMIAPEMSI(IdentifiedPeptide identifiedPeptide, MSRun msRun,
			MSRunProviderByInputData msRunProviderByInputData) {
		this.identifiedPeptide = identifiedPeptide;
		this.msRunProviderByInputData = msRunProviderByInputData;
		setMSRun(msRun);
		setSequence(identifiedPeptide.getSequence());
		setKey(getIdentifier());
	}

	@Override
	public String getIdentifier() {
		if (super.getIdentifier() == null) {
			setIdentifier(identifiedPeptide.getInputData().getName() + "-" + identifiedPeptide.getId() + "-"
					+ identifiedPeptide.getSpectrumRef() + "-" + identifiedPeptide.getSequence() + "-"
					+ identifiedPeptide.getCharge());
		}
		return super.getIdentifier();
	}

	@Override
	public Set<Protein> getProteins() {
		if (super.getProteins() == null || super.getProteins().isEmpty()) {
			for (final IdentifiedProtein identifiedProtein : identifiedPeptide.getIdentifiedProteins()) {
				Protein protein = null;
				if (StaticProteomicsModelStorage.containsProtein(getMSRun(), null, identifiedProtein.getAccession())) {
					protein = StaticProteomicsModelStorage.getSingleProtein(getMSRun().getRunId(), null,
							identifiedProtein.getAccession());
				} else {
					protein = new ProteinFromMIAPEMSI(identifiedProtein, msRunProviderByInputData);
					StaticProteomicsModelStorage.addProtein(protein, getMSRun(), null);
				}
				addProtein(protein, true);
			}
		}
		return super.getProteins();
	}

	@Override
	public List<PTM> getPTMs() {
		if (super.getPTMs() == null || super.getPTMs().isEmpty()) {
			if (identifiedPeptide.getModifications() != null) {
				for (final PeptideModification peptideModification : identifiedPeptide.getModifications()) {
					final PTM ptm = new PTMEx(peptideModification.getMonoDelta(), peptideModification.getResidues(),
							peptideModification.getPosition(),
							PTMPosition.getPTMPositionFromSequence(getSequence(), peptideModification.getPosition()));
					addPTM(ptm);
				}
			}
		}
		return super.getPTMs();
	}

}
