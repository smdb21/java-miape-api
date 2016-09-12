package org.proteored.miapeapi.interfaces.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * Describe the process by which the features detected have been quantified. 
 * In terms of measurement used, quantitation method, background substraction 
 * (if used), normalization method.
 * @author Salvador
 *
 */
public interface FeatureQuantitation {

	public String getName();

	public String getType();

	public String getStepOrder();

	public Set<Algorithm> getFeatureQuantitationAlgorithms();

	public Set<Algorithm> getFeatureQuantitationBackgrounds();

	public Set<Algorithm> getFeatureQuantitationNormalizations();
}
