package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.interfaces.msi.InputData;

public class InputDataImpl implements InputData {
	private final String spectrumPath;
	private final Integer identifier;

	public InputDataImpl(String spectrumPath, Integer identifier) {
		this.spectrumPath = spectrumPath;
		this.identifier = identifier;
	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public String getName() {
		return spectrumPath;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceDataUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMSFileType() {
		// TODO Auto-generated method stub
		return null;
	}

}
