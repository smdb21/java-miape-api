package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.ElectrophoresisType;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.xml.XmlEntity;

/**
 * This module identifies guidelines for the minimum information to report about
 * the use of n-dimensional gel electrophoresis in a proteomics experiment, in a
 * manner compliant with the aims as laid out in the MIAPE Principles document
 * (latest version available from http://psidev.sf.net/miape/).
 */
public interface MiapeGEDocument extends MiapeDocument,
		XmlEntity<MiapeGEDocument> {

	/**
	 * Gets the gel electrophoresis type; e.g. two-dimensional or
	 * one-dimensional. The value should be one of the possible values in
	 * {@link ElectrophoresisType}
	 * 
	 * @return the electrophoresis type
	 */
	public String getElectrophoresisType();

	@Override
	public GEContact getContact();

	/**
	 * Gets the sample information: Name of sample(s) including any label,
	 * marker or tag applied that will be used for protein detection, such as
	 * radiolabels or fluorescent labels (by name only). From the sample
	 * described above identify control, standard and test samples.
	 * 
	 * @return the sample information
	 */
	public Set<Sample> getSamples();

	public Set<IndirectDetection> getIndirectDetections();

	public Set<DirectDetection> getDirectDetections();

	public Set<Protocol> getProtocols();

	public Set<ImageGelElectrophoresis> getImages();

	public Set<ImageAcquisition> getImageAcquisitions();

	/**
	 * Gets the additional information: Any other relevant information that you
	 * want to add to the document, and that anyhow has an impact on the
	 * interpretation of the result for that experiment.
	 * 
	 * @return the additional information
	 */
	public Set<GEAdditionalInformation> getAdditionalInformations();
}
