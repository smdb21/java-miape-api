package org.proteored.miapeapi.psimod;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.scripps.yates.utilities.index.TextFileIndexIO;
import gnu.trove.set.hash.THashSet;

public class PSIModTextFileIndexIO extends TextFileIndexIO {

	/**
	 * Creates a {@link TextFileIndexIO} with begin token as '[Term]' and end
	 * token as empty string ''
	 * 
	 * @param file
	 * @throws IOException
	 */
	public PSIModTextFileIndexIO(File file) throws IOException {
		super(file, "[Term]", "");
	}

	@Override
	protected Set<String> getKeys(String entryString) {
		final String[] split = entryString.split("\n");
		for (final String line : split) {
			if (line.startsWith("id:")) {
				final Set<String> set = new THashSet<String>();
				final String termID = line.substring(line.indexOf("id:") + 3).trim();
				set.add(termID);
				return set;
			}
		}
		return super.getKeys(entryString);
	}

}
