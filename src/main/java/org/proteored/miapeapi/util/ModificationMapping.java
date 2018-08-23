package org.proteored.miapeapi.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.springframework.core.io.ClassPathResource;

import com.compomics.util.protein.AASequenceImpl;
import com.compomics.util.protein.ModificationImplementation;

import edu.scripps.yates.utilities.masses.AssignMass;
import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;
import uk.ac.ebi.pridemod.slimmod.model.SlimModification;

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
	private static ModificationMapping instance = null;
	private static SlimModCollection preferredModifications;

	private ModificationMapping() {

	}

	public static ModificationMapping getInstance() {
		if (instance == null)
			instance = new ModificationMapping();
		return instance;
	}

	private SlimModCollection loadPreferredModifications() {
		if (ModificationMapping.preferredModifications == null) {
			URL url;
			try {
				url = resource.getURL();
				ModificationMapping.preferredModifications = PrideModController.parseSlimModCollection(url);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return ModificationMapping.preferredModifications;
	}

	public SlimModCollection getModification(String modificationName, Double mass, String aaSpecificity) {
		final SlimModCollection preferredModifications = loadPreferredModifications();
		final SlimModCollection candidates = new SlimModCollection();

		// name and no MASS
		if (modificationName != null && mass == null) {
			final SlimModification slimMod = preferredModifications.getbyName(modificationName);
			if (slimMod != null) {
				candidates.add(slimMod);
			}
			// MASS
		} else if (mass != null && aaSpecificity == null) {
			final SlimModCollection slimMods = preferredModifications.getbyDelta(mass, ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (final SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
				}
			}
			// SPECIFICITY
		} else if (mass == null && aaSpecificity != null) {
			final SlimModCollection slimMods = preferredModifications.getbySpecificity(aaSpecificity);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (final SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
				}
			}
			// MASS + SPECIFICITY
		} else if (mass != null && aaSpecificity != null) {
			final SlimModCollection slimMods = preferredModifications.getbySpecificity(aaSpecificity, mass,
					ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (final SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
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
