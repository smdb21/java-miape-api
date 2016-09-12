package org.proteored.miapeapi.xml.ms;

import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.xml.ms.autogenerated.MSOtherIonSource;
import org.proteored.miapeapi.xml.ms.util.MsControlVocabularyXmlFactory;

public class OtherIonSourceImpl implements Other_IonSource {
	private final MSOtherIonSource otherIonSource;

	public OtherIonSourceImpl(MSOtherIonSource msOtherIonSource) {
		this.otherIonSource= msOtherIonSource;
	}

	@Override
	public String getName() {
		return MsControlVocabularyXmlFactory.getName(this.otherIonSource.getName());
	}

	@Override
	public String getParameters() {
		return this.otherIonSource.getParameters();
	}

}
