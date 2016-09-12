package org.proteored.miapeapi.interfaces.ms;

import java.util.List;
import java.util.Set;

/**
 * Defines the instrument configuration of an Spectrometer
 * 
 * @author Salva
 * 
 */
public interface InstrumentConfiguration {

	/**
	 * Gets an identifier name of the instrument configuration
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * The set of MADIs.MADI is an Ion source, matrix-assisted laser desorption
	 * ionisation (MALDI), electrospray ionisation.
	 * 
	 * @return the maldis
	 */
	public List<Maldi> getMaldis();

	/**
	 * The set of ESIs. ESI is an Ion source, Electrospray Ionisation (ESI)
	 * 
	 * @return the esis
	 */
	public List<Esi> getEsis();

	/**
	 * The set of other ion sources.
	 * 
	 * @return the ion source
	 */
	public List<Other_IonSource> getOther_IonSources();

	/**
	 * Gets the activation / dissociation information
	 * 
	 * @return the activation / dissociation information
	 */
	public Set<ActivationDissociation> getActivationDissociations();

	/**
	 * Gets the ion optics, hexapoles information
	 * 
	 * @return the ion optics
	 */
	// public Set<IonOptic> getIonOptics();

	/**
	 * Gets the analyzers information. 'Simple' quadrupoles, hexapoles, Paul
	 * trap, linear trap, FT-ICR, Orbitrap, magnetic sector, reflectron status
	 * of the Time-of-Flight drift tube (if applicable)
	 * 
	 * @return the analyzers
	 */
	public List<Analyser> getAnalyzers();
}
