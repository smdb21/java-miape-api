package org.proteored.miapeapi.factories.ms;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;

public class InstrumentConfigurationImpl implements InstrumentConfiguration {

	private String name;
	private List<Maldi> maldis;
	private List<Esi> esis;
	private Set<ActivationDissociation> collisionCells;
	private List<Other_IonSource> otherIonSources;
	// private Set<IonOptic> ionOptics;
	private List<Analyser> analyzers;

	public InstrumentConfigurationImpl(InstrumentConfigurationBuilder instrumentConfigurationBuilder) {
		this.name = instrumentConfigurationBuilder.name;
		this.analyzers = instrumentConfigurationBuilder.analyzers;
		this.collisionCells = instrumentConfigurationBuilder.activationDissociation;
		this.esis = instrumentConfigurationBuilder.esis;
		// this.ionOptics = instrumentConfigurationBuilder.ionOptics;
		this.maldis = instrumentConfigurationBuilder.maldis;
		this.otherIonSources = instrumentConfigurationBuilder.otherIonSources;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<Maldi> getMaldis() {
		return this.maldis;
	}

	@Override
	public List<Esi> getEsis() {
		return this.esis;
	}

	@Override
	public List<Other_IonSource> getOther_IonSources() {
		return this.otherIonSources;
	}

	@Override
	public Set<ActivationDissociation> getActivationDissociations() {
		return this.collisionCells;
	}

	// @Override
	// public Set<IonOptic> getIonOptics() {
	// return this.ionOptics;
	// }

	@Override
	public List<Analyser> getAnalyzers() {
		return this.analyzers;
	}

}
