package org.proteored.miapeapi.xml.pepxml;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import edu.scripps.yates.utilities.masses.AssignMass;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.ModAminoacidMass;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SubInfoDataType;

public class PeptideModificationImplFromPepXML implements PeptideModification {
	private final SubInfoDataType aaSubstitution;
	private final String peptideSequence;
	private final int position;
	private final Double delta;
	private PTM ptm;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(PeptideModificationImplFromPepXML.class);
	private static Set<String> errorMessages = new THashSet<String>();

	public PeptideModificationImplFromPepXML(String peptideSequence, ModAminoacidMass pepXMLModification) {
		this(peptideSequence, pepXMLModification.getPosition(),
				pepXMLModification.getMass() - getMassOfAA(peptideSequence, pepXMLModification.getPosition()), null);

	}

	private static double getMassOfAA(String peptideSequence, Integer position) {
		if (position != null && peptideSequence != null && peptideSequence.length() >= position) {
			AssignMass.getInstance(true);
			return AssignMass.getMass(peptideSequence.charAt(position - 1));
		}
		return 0;
	}

	public PeptideModificationImplFromPepXML(String peptideSequence, SubInfoDataType aaSubstitution) {
		this(peptideSequence, aaSubstitution.getPosition(), null, aaSubstitution);
	}

	public PeptideModificationImplFromPepXML(String sequence, int position, Double delta,
			SubInfoDataType aaSubstitution) {
		peptideSequence = sequence;
		this.position = position;
		this.delta = delta;

		final double precision = 0.01;
		// map by delta
		if (delta != null) {
			final List<PTM> filteredMods = ModReader.getInstance().getPTMListByMonoDeltaMass(delta, precision);
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
		} else {
			ptm = null;
		}
		this.aaSubstitution = aaSubstitution;
	}

	@Override
	public String getName() {
		if (ptm != null)
			return ptm.getName();
		if (aaSubstitution != null) {
			return "substitution of " + aaSubstitution.getOrigAa() + " by " + getResidues() + " at "
					+ aaSubstitution.getPosition();
		}
		return "unknown";
	}

	@Override
	public int getPosition() {

		return position;

	}

	@Override
	public String getResidues() {

		if (position == 0) {
			return "NTerm";
		} else if (position == peptideSequence.length() + 1) {
			return "CTerm";
		} else {
			return String.valueOf(peptideSequence.charAt(getPosition() - 1));
		}
	}

	@Override
	public Double getMonoDelta() {
		return delta;
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
		if (aaSubstitution != null) {
			aaSubstitution.getOrigAa();
		}
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
