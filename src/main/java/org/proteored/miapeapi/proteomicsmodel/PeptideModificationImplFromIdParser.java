package org.proteored.miapeapi.proteomicsmodel;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

public class PeptideModificationImplFromIdParser implements PeptideModification {
	private final edu.scripps.yates.utilities.proteomicsmodel.PTM ptmModification;
	private final ModReader modReader = ModReader.getInstance();
	private PTM ptm;
	private final PTMSite ptmSite;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(PeptideModificationImplFromIdParser.class);
	private static Set<String> errorMessages = new THashSet<String>();

	public PeptideModificationImplFromIdParser(edu.scripps.yates.utilities.proteomicsmodel.PTM dtaSelectPTM,
			PTMSite ptmSite) {
		ptmModification = dtaSelectPTM;
		this.ptmSite = ptmSite;
		final double delta = dtaSelectPTM.getMassShift();
		final double precision = 0.01;
		// map by delta
		final List<PTM> filteredMods = modReader.getPTMListByMonoDeltaMass(delta, precision);

		if (!filteredMods.isEmpty()) {
			ptm = filteredMods.get(0);
		} else {
			final String message = "Peptide modification with delta mass=" + delta
					+ " is not recognized in the system. Please, contact system administrator in order to add it as a supported PTM in the system.";
			if (!errorMessages.contains(message)) {
				log.warn(message);
				errorMessages.add(message);
			}
			ptm = null;
		}
	}

	@Override
	public String getName() {
		if (ptm != null) {
			return ptm.getName();
		}

		return ptmModification.getName();

	}

	@Override
	public int getPosition() {
		return ptmSite.getPosition();
	}

	@Override
	public String getResidues() {
		return ptmSite.getAA();
	}

	@Override
	public Double getMonoDelta() {
		if (ptmModification.getMassShift() != null) {
			return ptmModification.getMassShift();
		}
		if (ptm != null) {
			return ptm.getMonoDeltaMass();
		}
		return null;
	}

	@Override
	public Double getAvgDelta() {
		if (ptm != null) {
			return ptm.getAveDeltaMass();
		}
		return null;
	}

	@Override
	public String getReplacementResidue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getNeutralLoss() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModificationEvidence() {
		// TODO Auto-generated method stub
		return null;
	}

}
