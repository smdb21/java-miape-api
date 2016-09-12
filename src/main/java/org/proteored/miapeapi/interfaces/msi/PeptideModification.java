package org.proteored.miapeapi.interfaces.msi;

import org.proteored.miapeapi.cv.msi.PeptideModificationName;

public interface PeptideModification {

	/**
	 * The name of the modification. It should be one of the possible values from
	 * {@link PeptideModificationName}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the location of the modification within the peptide - position in peptide sequence,
	 * counted from the N-terminus residue, starting at position 1. Specific modifications to the
	 * N-terminus should be given the location 0. Modification to the C-terminus should be given as
	 * peptide length + 1.
	 * 
	 * @return the position (starting by 1)
	 */
	public int getPosition();

	/**
	 * Gets the residue in which the modification is present. In case of residue replacement
	 * modification, gets the original residue before replacement
	 * 
	 * @return the residue in the peptide sequence
	 */
	public String getResidues();

	/**
	 * Atomic mass delta when assuming only the most common isotope of elements in Daltons.
	 * 
	 * @return the mass difference in Daltons
	 */
	public Double getMonoDelta();

	/**
	 * Atomic mass delta considering the natural istribution of isotopes in Daltons.
	 * 
	 * @return the mass difference in Daltons
	 */
	public Double getAvgDelta();

	/**
	 * The residue that replaced the originalResidue
	 * 
	 * @return the residue
	 */
	public String getReplacementResidue();

	/**
	 * Gets the value in Daltons of the neutral loss modification
	 * 
	 * @return the mass difference in Daltons
	 */
	public Double getNeutralLoss();

	/**
	 * Evidence for the presence and location of the modifications. <br>
	 * Provide evidence for the presence and location of modifications. When a choice among possible
	 * isobaric or conflicting interpretations are reported, a justification should be applied (I/L,
	 * acetylation/trimethylation). If any additional software or approach has been used to
	 * determine the specific location of modification on a peptide, these scores should be reported
	 * (Ascore, PTM score, localization probabilities). The evidence can also for instance be an
	 * annotated spectrum.
	 * 
	 * @return
	 */
	public String getModificationEvidence();
}
