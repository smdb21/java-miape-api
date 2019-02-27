package org.proteored.miapeapi.proteomicsmodel;

import java.util.List;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;

import edu.scripps.yates.utilities.proteomicsmodel.AbstractProtein;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;

public class ProteinFromMIAPEMSI extends AbstractProtein {

	private final IdentifiedProtein protein;
	private final MSRunProviderByInputData msRunProviderByInputData;

	public ProteinFromMIAPEMSI(IdentifiedProtein protein, MSRunProviderByInputData msRunProviderByInputData) {
		this.protein = protein;
		this.msRunProviderByInputData = msRunProviderByInputData;
		setKey(getAccession());
	}

	@Override
	public Integer getSpectrumCount() {
		return protein.getIdentifiedPeptides().size();
	}

	@Override
	public String getDescription() {
		return protein.getDescription();
	}

	@Override
	public String getAccession() {
		if (super.getAccession() == null) {
			setPrimaryAccession(protein.getAccession());
		}
		return super.getAccession();
	}

	@Override
	public Accession getPrimaryAccession() {
		if (super.getPrimaryAccession() == null) {
			setPrimaryAccession(protein.getAccession());
		}
		return super.getPrimaryAccession();
	}

	@Override
	public Float getCoverage() {
		if (super.getCoverage() == null) {
			try {
				setCoverage(Float.valueOf(protein.getCoverage()));
			} catch (final NumberFormatException e) {
			}
		}
		return super.getCoverage();
	}

	@Override
	public Integer getLength() {
		return null;
	}

	@Override
	public List<PSM> getPSMs() {
		if (super.getPSMs().isEmpty()) {
			for (final IdentifiedPeptide psmFromMIAPE : protein.getIdentifiedPeptides()) {
				final InputData inputData = psmFromMIAPE.getInputData();
				final MSRun msRun = msRunProviderByInputData.getMSRunByInputData(inputData);
				PSM psm = new PSMFromMIAPEMSI(psmFromMIAPE, msRun, msRunProviderByInputData);
				if (StaticProteomicsModelStorage.containsPSM(msRun, null, psm.getIdentifier())) {
					psm = StaticProteomicsModelStorage.getSinglePSM(msRun, null, psm.getIdentifier());
				} else {
					StaticProteomicsModelStorage.addPSM(psm, msRun, null);
				}
				addPSM(psm, true);
			}
		}
		return super.getPSMs();
	}

}
