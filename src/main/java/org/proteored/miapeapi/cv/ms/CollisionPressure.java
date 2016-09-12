package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class CollisionPressure extends ControlVocabularySet {

	private static final Accession COLLISION_GAS_PRESSURE_ACC = new Accession("MS:1000869");
	private static final String COLLISION_GAS_PRESSURE_PREFERRED_NAME = "collision gas pressure";
	private static CollisionPressure instance;

	public static CollisionPressure getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new CollisionPressure(cvManager);
		return instance;
	}

	private CollisionPressure(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "MS:1000869" }; // collision gas
															// pressure
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = -1;
	}

	public static ControlVocabularyTerm getCollisionPressureTerm() {
		return new ControlVocabularyTermImpl(COLLISION_GAS_PRESSURE_ACC,
				COLLISION_GAS_PRESSURE_PREFERRED_NAME);
	}
}
