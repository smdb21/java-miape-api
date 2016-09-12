package org.proteored.miapeapi.interfaces.gi;

public interface AnalysisDesign {

	/**
	 * 
	 * @return an descriptive name of the analysis design
	 */
	public String getName();

	/**
	 * State the analysis design with respect to the assignment of images to
	 * groups. For example, directed (when groups are defined) or blind (groups
	 * are unknown).
	 * 
	 * @return the name
	 */
	public String getType();

	/**
	 * Type (for example biological or technical) and number of replicates.
	 * 
	 * @return the replicate types
	 */
	public String getReplicates();

	/**
	 * Collection of images based on user-defined classification (for example
	 * control or disease set, time course, doses, stimuli etc). State the
	 * images within each group (with references to the images documented in
	 * Section 1.5).
	 * 
	 * @return the groups
	 */
	public String getGroups();

	/**
	 * internal standards: an additional sample run in the same physical gel for
	 * a specific purpose, for example, an internal standard in a DIGE
	 * experiment;
	 * 
	 * @return the internal standards information
	 */
	public String getStandard();

	/**
	 * external standards: any additional sample run in different gels for a
	 * specific purpose (for example characteristic alignment);
	 * 
	 * @return the external standards information
	 */
	public String getExternalStandard();

	/**
	 * or within sample standards: a protein or protein mixture added in a
	 * defined amount for purposes such as normalization or alignment.
	 * 
	 * @return the within sample standards
	 */
	public String getWithinSampleStandard();
}
