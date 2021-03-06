package org.proteored.miapeapi.xml.pride.adapter.mzml;

import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;

import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * Class that contains the relationships between an spectrum identifier and the
 * peptide reference that the peptide should use to reference to the spectrum.
 * 
 * @author Salva
 * 
 */
public class MzMLSpectrumIDManager {

	private final TObjectIntHashMap<String> identifiersMapping = new TObjectIntHashMap<String>();

	/**
	 * Note that the spectrumRef should contain the offset if more than one mzML
	 * files are readed
	 * 
	 * @param spectrumIdentifier
	 * @param spectrumRef
	 */
	public void addNewSpectrumMapping(String spectrumIdentifier, int spectrumRef) {
		if (!identifiersMapping.containsKey(spectrumIdentifier)) {
			identifiersMapping.put(spectrumIdentifier, spectrumRef);
		} else {
			throw new IllegalMiapeArgumentException(
					"The spectrum ID: '" + spectrumIdentifier + "' has already been included in the mapping!");
		}
	}

	public int getSpectrumIndex(String spectrumIdentifier) {
		if (this.identifiersMapping.containsKey(spectrumIdentifier))
			return this.identifiersMapping.get(spectrumIdentifier);
		return -1;
	}

	public void clear() {
		this.identifiersMapping.clear();

	}
}
