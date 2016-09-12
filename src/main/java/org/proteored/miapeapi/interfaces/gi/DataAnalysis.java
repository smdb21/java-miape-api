package org.proteored.miapeapi.interfaces.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * analysis protocol used, repeat the steps as necessary, 
 * that created the data reported in Section 7.
 * @author Salvador
 *
 */
public interface DataAnalysis {

	/**
	 * State the data analysis method used (such as student t-test, 
	 * heuristic clustering, PCA, determination of the false-discovery rate).
	 * @return the name of the data analysis
	 */
	public String getName();

	/**
	 * The type of the data analysis method
	 * @return the type of the data analysis
	 */
	public String getType();

	/**
	 * Describe the analysis intent (for example "finding the features with 
	 * a statistical significant differential expression"). 
	 * @return the analysis intent
	 */
	public String getIntent();

	/** 
	 * Parameters used in applying the method (blind or not, number of classes, 
	 * method for handling missing values and/or outliers, etc.) if applicable
	 * @return the used parameters
	 */
	public String getParameters();

	/**
	 * Source data used as input to the method (for example intensity, area, 
	 * volume normalized or not, etc.), provide a description of any selection 
	 * or filtering procedures used on the outputs of Section 5.
	 * @return the source data used as input to the method
	 */
	public String getInputData();

	/**
	 * Describe any transformations performed on the data 
	 * (with parameters where appropriate) prior to the analysis e.g. 
	 * log transformation, median centering.
	 * @return a set of {@link Algorithm}
	 */
	public Set<Algorithm> getDataAnalysisTransformations();

	/**
	 * Software used for the data analysis, if different from the software 
	 * defined in Section 1.6. Software name and version number, vendor 
	 * (or if not available, provide a literature reference or a URI). 
	 * @return a set of {@link ImageAnalysisSoftware}
	 */
	public Set<ImageAnalysisSoftware> getDataAnalysisSoftwares();
}
