package org.proteored.miapeapi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.springframework.core.io.ClassPathResource;

import com.compomics.util.protein.AASequenceImpl;
import com.compomics.util.protein.ModificationImplementation;

import edu.scripps.yates.utilities.masses.AssignMass;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;
import uk.ac.ebi.pride.utilities.pridemod.model.Specificity;

/**
 * This class helps to identify a modification and get the appropriate name and
 * cv id, using either a name, a mass delta or a aminoacid specificity
 * 
 * @author Salva
 * 
 */
public class ModificationMapping {
	private static final double ERROR_TOLERANCE = 0.001;
	private final ClassPathResource resource = new ClassPathResource("modification_mappings.xml");
	private static ModReader instance = ModReader.getInstance();

	private ModificationMapping() {

	}

	public List<PTM> getModification(String modificationName, Double mass, String aaSpecificity) {
		final List<PTM> candidates = new ArrayList<PTM>();

		// name and no MASS
		if (modificationName != null && mass == null) {
			final List<PTM> slimMod = instance.getPTMListByEqualName(modificationName);
			if (slimMod != null) {
				candidates.addAll(slimMod);
			}
			// MASS
		} else if (mass != null && aaSpecificity == null) {
			final List<PTM> slimMods = instance.getPTMListByMonoDeltaMass(mass, ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				candidates.addAll(slimMods);
			}
			// SPECIFICITY
		} else if (mass == null && aaSpecificity != null) {
			final Specificity specificity = new Specificity(aaSpecificity, null);
			final List<PTM> slimMods = instance.getPTMListBySpecificity(specificity);
			if (slimMods != null && !slimMods.isEmpty()) {
				candidates.addAll(slimMods);
			}
			// MASS + SPECIFICITY
		} else if (mass != null && aaSpecificity != null) {
			final List<PTM> slimMods = instance.getPTMListByMonoDeltaMass(mass, ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (final PTM ptm : slimMods) {
					for (final Specificity specificity : ptm.getSpecificityCollection()) {
						if (specificity.getName().name().equals(specificity)) {
							candidates.add(ptm);
						}
					}
				}
			}
		}
		return candidates;

	}

	/**
	 * Creates a {@link AASequenceImpl} which can be used for calculating
	 * masses, etc...
	 * 
	 * @param sequence
	 * @param modifications
	 */
	public static AASequenceImpl getAASequenceImpl(String sequence, Set<PeptideModification> modifications) {
		return new AASequenceImpl(sequence, getModificationsImplementations(modifications));
	}

	private static Vector<ModificationImplementation> getModificationsImplementations(
			Set<PeptideModification> modifications) {
		final Vector<ModificationImplementation> ret = new Vector<ModificationImplementation>();
		if (modifications == null) {
			return ret;
		}
		for (final PeptideModification peptideModification : modifications) {

			final HashMap<String, double[]> delta = new HashMap<String, double[]>();
			final double[] deltas = new double[2];
			if (peptideModification.getMonoDelta() != null) {
				AssignMass.getInstance(true);
				deltas[0] = peptideModification.getMonoDelta()
						+ AssignMass.getMass(peptideModification.getResidues().charAt(0));
			} else {
				deltas[0] = 0.0;
			}
			if (peptideModification.getAvgDelta() != null) {
				AssignMass.getInstance(false);
				deltas[1] = peptideModification.getAvgDelta()
						+ AssignMass.getMass(peptideModification.getResidues().charAt(0));
			} else {
				deltas[1] = deltas[0];
			}
			delta.put(peptideModification.getResidues(), deltas);
			final ModificationImplementation mod = new ModificationImplementation(peptideModification.getName(), null,
					delta, peptideModification.getPosition());
			ret.add(mod);

		}
		return ret;
	}
}
