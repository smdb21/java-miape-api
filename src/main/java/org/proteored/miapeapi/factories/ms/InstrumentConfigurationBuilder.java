package org.proteored.miapeapi.factories.ms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;

import gnu.trove.set.hash.THashSet;

public class InstrumentConfigurationBuilder {

	final String name;
	List<Analyser> analyzers;
	Set<ActivationDissociation> activationDissociation;
	List<Esi> esis;
	// Set<IonOptic> ionOptics;
	List<Maldi> maldis;
	List<Other_IonSource> otherIonSources;

	InstrumentConfigurationBuilder(String name) {
		this.name = name;
	}

	public InstrumentConfigurationBuilder analysers(List<Analyser> value) {
		this.analyzers = value;
		return this;
	}

	public InstrumentConfigurationBuilder analyser(Analyser value) {
		if (analyzers == null)
			analyzers = new ArrayList<Analyser>();
		this.analyzers.add(value);
		return this;
	}

	public InstrumentConfigurationBuilder activationDissociations(Set<ActivationDissociation> value) {
		this.activationDissociation = value;
		return this;
	}

	public InstrumentConfigurationBuilder activationDissociation(ActivationDissociation value) {
		if (activationDissociation == null)
			activationDissociation = new THashSet<ActivationDissociation>();
		this.activationDissociation.add(value);
		return this;
	}

	public InstrumentConfigurationBuilder esis(List<Esi> value) {
		this.esis = value;
		return this;
	}

	public InstrumentConfigurationBuilder esi(Esi value) {
		if (this.esis == null)
			this.esis = new ArrayList<Esi>();
		this.esis.add(value);
		return this;
	}

	// public InstrumentConfigurationBuilder ionOptics(Set<IonOptic> value) {
	// this.ionOptics = value;
	// return this;
	// }

	public InstrumentConfigurationBuilder maldis(List<Maldi> value) {

		this.maldis = value;
		return this;
	}

	public InstrumentConfigurationBuilder maldi(Maldi value) {
		if (this.maldis == null)
			this.maldis = new ArrayList<Maldi>();
		this.maldis.add(value);
		return this;
	}

	public InstrumentConfigurationBuilder otherIonSources(List<Other_IonSource> value) {
		this.otherIonSources = value;
		return this;
	}

	public InstrumentConfigurationBuilder otherIonSource(Other_IonSource value) {
		if (this.otherIonSources == null)
			this.otherIonSources = new ArrayList<Other_IonSource>();
		this.otherIonSources.add(value);
		return this;

	}

	public InstrumentConfiguration build() {
		return new InstrumentConfigurationImpl(this);
	}

}
