package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DissociationMethod extends ControlVocabularySet {
	public static final Accession DISSOCIATION_METHOD_ACCESSION = new Accession("MS:1000044");
	/*
	 * ELECTRON_TRANSFER_DISSOCIATION("MS:1000598",
	 * "electron transfer dissociation"),
	 * COLLISION_INDUCED_DISSOCIATION("MS:1000133",
	 * "collision-induced dissociation"), DISSOCIATION_METHOD("MS:1000044",
	 * "dissociation method"),
	 * BLACKBODY_INFRARED_RADIATIVE_DISSOCIATION("MS:1000242",
	 * "blackbody infrared radiative dissociation"),
	 * SUSTAINED_OFF_RESONANCE_IRRADIATION("MS:1000282",
	 * "sustained off-resonance irradiation"),
	 * INFRARED_MULTIPHOTON_DISSOCIATION("MS:1000262",
	 * "infrared multiphoton dissociation"),
	 * ELECTRON_CAPTURE_DISSOCIATION("MS:1000250",
	 * "electron capture dissociation"), PHOTODISSOCIATION("MS:1000435",
	 * "photodissociation"),
	 * LOW_ENERGY_COLLISION_INDUCED_DISSOCIATION("MS:1000433",
	 * "low-energy collision-induced dissociation"),
	 * PLASMA_DESORPTION("MS:1000134", "plasma desorption"),
	 * SURFACE_INDUCED_DISSOCIATION("MS:1000136",
	 * "surface-induced dissociation"), POST_SOURCE_DECAY("MS:1000135",
	 * "post-source decay"), PULSED_Q_DISSOCIATION("MS:1000599",
	 * "pulsed q dissociation"),
	 * HIGH_ENERGY_COLLISION_INDUCED_DISSOCIATION("MS:1000422",
	 * "high-energy collision-induced dissociation"), ;
	 */
	private static DissociationMethod instance;

	public static DissociationMethod getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DissociationMethod(cvManager);
		return instance;
	}

	private DissociationMethod(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000044" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 217;

	}

}
