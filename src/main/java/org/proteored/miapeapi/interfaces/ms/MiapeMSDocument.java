package org.proteored.miapeapi.interfaces.ms;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.xml.XmlEntity;

/**
 * The Interface MiapeMSDocument represents a MiapeDocument for a Mass
 * Spectometry experiment
 */
public interface MiapeMSDocument extends MiapeDocument,
		XmlEntity<MiapeMSDocument> {

	@Override
	public MSContact getContact();

	/**
	 * Gets the Quantitation for selected ions Only applicable if a quantitation
	 * experiment has been performed. It includes: Experimental protocol,
	 * canonical reference where available with deviations Number of combined
	 * samples and MS runs analysed Quantitation approach (e.g. integration)
	 * Normalisation technique Location of quantitation data, with file name and
	 * type (where appropriate)
	 * 
	 * @return the quantitations
	 */
	/* public Set<Quantitation> getQuantitations(); */

	/**
	 * Gets the spectrometer properties.
	 * 
	 * @return the Equipment properties.
	 */
	public Set<Spectrometer> getSpectrometers();

	/**
	 * Gets the intrument configuration list
	 * 
	 * @return the instrument configurations
	 */
	public List<InstrumentConfiguration> getInstrumentConfigurations();

	/**
	 * Gets the acquisition informations. The instrument management and data
	 * analysis package name, and version; where there are several pieces of
	 * software involved, give name, version and role for each one. Mention also
	 * upgrades not reflected in the version number. The information on how the
	 * MS data have been generated. This can be a text description or a readable
	 * parameters file from the instrument software.
	 * 
	 * 
	 * @return the acquisitions
	 */
	public Set<Acquisition> getAcquisitions();

	/**
	 * The location and filename under which the original raw data file(s) from
	 * the mass spectrometer and the processed file(s) are stored. Also give the
	 * type of the file where appropriate. Ideally this should be a
	 * URI+filename. The chromatogram as array of time and intensity values.
	 * Provide the type and descriptors (for instance TIC with selected mass
	 * range when available, XIC with selected m/z and tolerance, BPC)
	 * 
	 * @return the resulting data
	 */
	public List<ResultingData> getResultingDatas();

	/**
	 * The MS data analysis package name, and version; where there are several
	 * pieces of software involved, give name, version and role for each one.
	 * Mention also upgrades not reflected in the version number. The
	 * information on how the spectra have been processed. This include the list
	 * of parameters triggering the generation of peak lists, chromatograms,
	 * images from raw data or already processed data and the order in which
	 * they have been used.This can be a list or a parameters file
	 * 
	 * @return the data analysis information
	 */
	public Set<DataAnalysis> getDataAnalysis();

	/**
	 * Gets the additional information: Any other relevant information that you
	 * want to add to the document, and that anyhow has an impact on the
	 * interpretation of the result for that experiment.
	 * 
	 * @return the additional information
	 */
	public List<MSAdditionalInformation> getAdditionalInformations();
}
