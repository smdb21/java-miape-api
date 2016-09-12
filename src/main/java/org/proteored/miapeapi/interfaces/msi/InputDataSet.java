package org.proteored.miapeapi.interfaces.msi;

import java.util.Set;

/**
 * The input data set: a set of input data that usually corresponds to a
 * fraction in a LC-MS experiment.
 * 
 * @author Salvador
 * 
 */
public interface InputDataSet {

	/**
	 * Gets the identifier that can be referenced from IdentifiedProteinSet
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Gets the name of the input data set
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the input datas that belongs to this data set
	 * 
	 * @return a set of input datas.
	 */
	public Set<InputData> getInputDatas();

}
