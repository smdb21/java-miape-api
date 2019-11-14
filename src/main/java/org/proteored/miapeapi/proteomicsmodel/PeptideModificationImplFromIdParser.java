package org.proteored.miapeapi.proteomicsmodel;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;
import uk.ac.ebi.pride.utilities.pridemod.model.Specificity;

public class PeptideModificationImplFromIdParser implements PeptideModification {
	private final edu.scripps.yates.utilities.proteomicsmodel.PTM ptmModification;
	private final ModReader modReader = ModReader.getInstance();
	private PTM ptm;
	private final PTMSite ptmSite;
	private String name;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(PeptideModificationImplFromIdParser.class);
	private static Set<String> errorMessages = new THashSet<String>();

	public PeptideModificationImplFromIdParser(edu.scripps.yates.utilities.proteomicsmodel.PTM dtaSelectPTM,
			PTMSite ptmSite) {
		ptmModification = dtaSelectPTM;
		this.ptmSite = ptmSite;
		final double delta = dtaSelectPTM.getMassShift();

		// map by delta

		if (dtaSelectPTM.getName() == null || "unknown".equalsIgnoreCase(dtaSelectPTM.getName())
				|| "".equals(dtaSelectPTM.getName())) {
			final double precision = 0.001;
			List<uk.ac.ebi.pride.utilities.pridemod.model.PTM> ptms = modReader.getPTMListByMonoDeltaMass(delta,
					precision / 10.0);
			if (ptms == null || ptms.isEmpty()) {
				ptms = modReader.getPTMListByMonoDeltaMass(delta, precision);
			}

			if (ptms != null && !ptms.isEmpty()) {
				for (final uk.ac.ebi.pride.utilities.pridemod.model.PTM ptm : ptms) {

					if (ptm.getSpecificityCollection() != null && !ptm.getSpecificityCollection().isEmpty()) {
						boolean specificityOK = false;
						for (final Specificity specificity : ptm.getSpecificityCollection()) {
							if (specificity.getName().name().equalsIgnoreCase(ptmSite.getAA())) {
								specificityOK = true;
								break;
							}
						}
						if (specificityOK) {
							this.ptm = ptm;
							name = ptm.getName();
							break;
						}
					}
				}
			} else {
				final String message = "Peptide modification with delta mass=" + delta
						+ " is not recognized in the system. Please, contact system administrator in order to add it as a supported PTM in the system.";
				if (!errorMessages.contains(message)) {
					log.warn(message);
					errorMessages.add(message);
				}
				ptm = null;
			}
		} else {
			name = dtaSelectPTM.getName();
		}

	}

	@Override
	public String getName() {

		return name;

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
