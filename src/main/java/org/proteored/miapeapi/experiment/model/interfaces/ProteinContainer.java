package org.proteored.miapeapi.experiment.model.interfaces;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;

public interface ProteinContainer {

	public Float getBestProteinScore();

	public ExtendedIdentifiedProtein getBestProtein();

	public Float getBestProteinScore(String scoreName);

	public ExtendedIdentifiedProtein getBestProtein(String scoreName);

}
