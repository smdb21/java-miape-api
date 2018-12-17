package org.proteored.miapeapi.proteomicsmodel;

import org.proteored.miapeapi.interfaces.msi.InputData;

import edu.scripps.yates.utilities.proteomicsmodel.MSRun;

public interface MSRunProviderByInputData {
	MSRun getMSRunByInputData(InputData inputData);
}
