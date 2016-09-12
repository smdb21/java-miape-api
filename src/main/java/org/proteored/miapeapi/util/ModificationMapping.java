package org.proteored.miapeapi.util;

import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.ClassPathResource;

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
	private final ClassPathResource resource = new ClassPathResource(
			"modification_mappings.xml");
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
				ModificationMapping.preferredModifications = PrideModController
						.parseSlimModCollection(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ModificationMapping.preferredModifications;
	}

	public SlimModCollection getModification(String modificationName,
			Double mass, String aaSpecificity) {
		SlimModCollection preferredModifications = loadPreferredModifications();
		SlimModCollection candidates = new SlimModCollection();

		// name and no MASS
		if (modificationName != null && mass == null) {
			SlimModification slimMod = preferredModifications
					.getbyName(modificationName);
			if (slimMod != null) {
				candidates.add(slimMod);
			}
			// MASS
		} else if (mass != null && aaSpecificity == null) {
			SlimModCollection slimMods = preferredModifications.getbyDelta(
					mass, ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
				}
			}
			// SPECIFICITY
		} else if (mass == null && aaSpecificity != null) {
			SlimModCollection slimMods = preferredModifications
					.getbySpecificity(aaSpecificity);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
				}
			}
			// MASS + SPECIFICITY
		} else if (mass != null && aaSpecificity != null) {
			SlimModCollection slimMods = preferredModifications
					.getbySpecificity(aaSpecificity, mass, ERROR_TOLERANCE);
			if (slimMods != null && !slimMods.isEmpty()) {
				for (SlimModification slimModification : slimMods) {
					candidates.add(slimModification);
				}
			}
		}
		return candidates;

	}
}
