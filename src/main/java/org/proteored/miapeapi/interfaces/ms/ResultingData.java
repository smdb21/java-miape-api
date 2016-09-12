package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.ChromatogramType;
import org.proteored.miapeapi.cv.ms.MSFileType;

/**
 * The location and filename under which the original raw data file(s) from the
 * mass spectrometer and the processed file(s) are stored. Also give the type of
 * the file where appropriate. Ideally this should be a URI+filename. The
 * chromatogram as array of time and intensity values. Provide the type and
 * descriptors (for instance TIC with selected mass range when available, XIC
 * with selected m/z and tolerance, BPC)
 * 
 * @author Salva
 * 
 */
public interface ResultingData {
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the raw or processed file uri.
	 * 
	 * @return the raw or processed file uri
	 */
	public String getDataFileUri();

	/**
	 * Gets the file type. It should be one of the possible values from
	 * {@link MSFileType}
	 * 
	 * @return the file type
	 */
	public String getDataFileType();

	/**
	 * Gets an additional uri.
	 * 
	 * @return the additional uri
	 */
	public String getAdditionalUri();

	/**
	 * Gets the location of a file with the chromatogram(s) for SRM data or
	 * other relevant cases (as an array of time and intensity values).
	 * 
	 * @return the chromatogram(s) for SRM.
	 */
	public String getSRMUri();

	/**
	 * Gets the chromatogram(s) type for SRM. It should be one of the possible
	 * values from {@link ChromatogramType}
	 * 
	 * @return the SRM type.
	 */
	public String getSRMType();

	/**
	 * Gets the SRM descriptors (for instance TIC with selected mass range when
	 * available, XIC with selected m/z and tolerance, BPC)
	 * 
	 * @return the SRM descriptor.
	 */
	public String getSRMDescriptor();

	/**
	 * Gets the spectrum descriptions. It includes: MS level for this spectrum
	 * Ion mode for this spectrum Precursor m/z and charge, with the full mass
	 * spectrum containing that peak (for MS level 2 and higher)
	 * 
	 * @return the spectrum descriptions
	 */
	// public List<SpectrumDescription> getSpectrumDescriptions();
}
