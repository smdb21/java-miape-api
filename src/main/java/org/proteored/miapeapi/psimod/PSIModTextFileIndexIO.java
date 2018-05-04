package org.proteored.miapeapi.psimod;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.utilities.index.TextFileIndexIO;

public class PSIModTextFileIndexIO extends TextFileIndexIO {

	public PSIModTextFileIndexIO(File file) throws IOException {
		super(file, "[Term]", "");
	}

	@Override
	protected Set<String> getKeys(String entryString) {
		final String[] split = entryString.split("\n");
		for (final String line : split) {
			if (line.startsWith("id:")) {
				final Set<String> set = new HashSet<String>();
				final String termID = line.substring(line.indexOf("id:") + 3).trim();
				set.add(termID);
				return set;
			}
		}
		return super.getKeys(entryString);
	}

}
