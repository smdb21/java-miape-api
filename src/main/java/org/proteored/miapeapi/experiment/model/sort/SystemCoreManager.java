package org.proteored.miapeapi.experiment.model.sort;

public class SystemCoreManager {
	private static int numSystemCores = Runtime.getRuntime()
			.availableProcessors();

	public static int getNumSystemCores() {
		return numSystemCores;

	}

	public static int getAvailableNumSystemCores() {
		int usedCores = numSystemCores - 1;
		if (numSystemCores == 3)
			usedCores = 2;
		if (numSystemCores == 2)
			usedCores = 2;
		if (numSystemCores == 1)
			usedCores = 1;
		return usedCores;
	}

	public static int getAvailableNumSystemCores(int maximum) {
		int num = getAvailableNumSystemCores();
		if (num > maximum)
			return maximum;
		return num;
	}
}
