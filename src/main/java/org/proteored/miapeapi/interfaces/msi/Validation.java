package org.proteored.miapeapi.interfaces.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Software;

/**
 * Interpretation and validation
 * 
 * @author Salvador
 * 
 */
public interface Validation {

	/**
	 * The descriptive name of the interpretation and validation information
	 * 
	 * @return the name
	 */
	public String getName();

	// /**
	// * Inclusion/exclusion of the output of the software are provided (description of what part of
	// * the output has been kept, what part has been rejected)
	// *
	// * @return the output of the software
	// */
	// public String getOutputPart();
	//
	// /**
	// * Access to the output data.
	// *
	// * @return the URI
	// */
	// public String getOutputURI();

	/**
	 * Describe any approaches used for re-scoring or post-processing the initial results from the
	 * search engine, used to generate statistical measures of correctness or improve the
	 * sensitivity of the search. Examples include methods for calculating p-values or q-values.
	 * 
	 * @return the validation methods
	 */
	public Set<PostProcessingMethod> getPostProcessingMethods();

	/**
	 * Describe any software used for re-scoring or post-processing the initial results from the
	 * search engine, used to generate statistical measures of correctness or improve the
	 * sensitivity of the search. Examples include software for calculating p-values or q-values.
	 * 
	 * @return the validation softwares
	 */
	public Set<Software> getPostProcessingSoftwares();

	/**
	 * Describe any statistical analysis performed (if performed), for example to calculate the
	 * false positive or false discovery rate. This includes for instance a peptide level FDR
	 * obtained from a query in a concatenated forward-revers database<br>
	 * 
	 * @return the results
	 */
	public String getStatisticalAnalysisResults();

	/**
	 * Describe any thresholds that have been applied to the complete list of peptide or protein
	 * identifications, before and/or after the use of the method described for post-processing or
	 * rescoring or re-ranking, used to accept or reject a reported result, such as p-value < 0.05,
	 * q-value < 0.01, search engine score > x, PTMscore > x and so on. In the case of manual
	 * evaluation or other empirical filters, provide explicit rules and criteria
	 * 
	 * @return
	 */
	public String getGlobalThresholds();
}
