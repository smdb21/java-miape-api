package org.proteored.miapeapi.xml.pride.ms;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.xml.pride.util.InstrumentManager;

public class InstrumentConfigurationImpl implements InstrumentConfiguration {
	private final InstrumentManager instrumentManager;

	public InstrumentConfigurationImpl(InstrumentManager instrumentManager) {
		this.instrumentManager = instrumentManager;
	}

	@Override
	public String getName() {
		return "IC1";
	}

	@Override
	public List<Maldi> getMaldis() {
		if (instrumentManager != null)
			return instrumentManager.getMaldis();
		return null;
	}

	@Override
	public List<Esi> getEsis() {
		if (instrumentManager != null)
			return instrumentManager.getEsis();
		return null;
	}

	@Override
	public List<Other_IonSource> getOther_IonSources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ActivationDissociation> getActivationDissociations() {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public Set<IonOptic> getIonOptics() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public List<Analyser> getAnalyzers() {
		if (instrumentManager != null)
			return instrumentManager.getAnalyzers();
		return null;
	}

}
