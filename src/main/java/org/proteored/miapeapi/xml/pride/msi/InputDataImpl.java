package org.proteored.miapeapi.xml.pride.msi;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData;

public class InputDataImpl implements InputData {
	private final MzData mzData;
	private final Integer identifier;

	public InputDataImpl(MzData mzData, Integer identifier) {
		this.mzData = mzData;
		this.identifier = identifier;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		return identifier;
	}

	@Override
	public String getMSFileType() {
		if (mzData.getDescription()!= null) {
			if (mzData.getDescription().getAdmin() != null) {
				if (mzData.getDescription().getAdmin().getSourceFile() != null) {
					if (mzData.getDescription().getAdmin().getSourceFile().getFileType() != null) {
						return mzData.getDescription().getAdmin().getSourceFile().getFileType();
					}
				}
			}
		}
		return "Input data " + mzData.getAccessionNumber() + " " + mzData.getVersion();
	}

	@Override
	public String getName() {
		if (mzData.getDescription()!= null) {
			if (mzData.getDescription().getAdmin() != null) {
				if (mzData.getDescription().getAdmin().getSourceFile() != null) {
					if (mzData.getDescription().getAdmin().getSourceFile().getNameOfFile() != null) {
						return mzData.getDescription().getAdmin().getSourceFile().getNameOfFile();
					}
				}
			}
		}
		return "Input data " + mzData.getAccessionNumber() + " " + mzData.getVersion();
	}

	@Override
	public String getSourceDataUrl() {
		if (mzData.getDescription()!= null) {
			if (mzData.getDescription().getAdmin() != null) {
				if (mzData.getDescription().getAdmin().getSourceFile() != null) {
					if (mzData.getDescription().getAdmin().getSourceFile().getPathToFile() != null) {
						return mzData.getDescription().getAdmin().getSourceFile().getPathToFile();
					}
				}
			}
		}
		return null;
	}

}
