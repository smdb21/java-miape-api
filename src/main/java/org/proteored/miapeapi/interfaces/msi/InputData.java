package org.proteored.miapeapi.interfaces.msi;

/**
 * Input data and parameters: input data
 * 
 * @author Salvador
 * 
 */
public interface InputData {
	/**
	 * Gets the identifier that can be referenced from IdentifiedPeptide
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Provide a short description that can refer to the data in the experiment (e.g. LC-MS run1)
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Specify if the submitted data are raw full traces (either as proprietary binary format or
	 * exported readable format) or if they are peaklists files.
	 * 
	 * @return the explanation
	 */
	public String getDescription();

	/**
	 * Location of the input data
	 * 
	 * @return and location to the data
	 */
	public String getSourceDataUrl();

	/**
	 * original format (i.e. Bruker .yep, Applied Biosystems .wiff, mzData, dta (Sequest format),
	 * mgf (Mascot generic format), mzXML (Institute for Systems Biology xml format), other...
	 * 
	 * @return file type
	 */
	public String getMSFileType();

}
