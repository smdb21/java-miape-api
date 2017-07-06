package org.proteored.miapeapi.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

public class UniprotId2AccMapping {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static UniprotId2AccMapping instance;
	private static Map<String, String> uniprotIDACCMap = new THashMap<String, String>();
	private static Map<String, String> uniprotACCIDMap = new THashMap<String, String>();
	public static final String mappingFileName = "Uniprot_ID_AC_Mapping.csv";

	/**
	 * Reads a provided mapping file and create an instance. The mapping file is
	 * a CSV file, with the first column the uniprot ID, and the second column
	 * with the uniprot accession
	 * 
	 * @param mappingFileName
	 * @return
	 * @throws IOException
	 */
	public static UniprotId2AccMapping getInstance(String mappingFileName) throws IOException {
		if (instance == null) {
			instance = new UniprotId2AccMapping(mappingFileName);
		}
		return instance;
	}

	/**
	 * Reads the default mapping file and create an instance
	 * 
	 * @return
	 * @throws IOException
	 */
	public static UniprotId2AccMapping getInstance() throws IOException {
		if (instance == null) {
			try {
				instance = new UniprotId2AccMapping(mappingFileName);
			} catch (Exception e) {

			}
		}
		return instance;
	}

	private UniprotId2AccMapping(String mappingFileName) throws IOException {
		log.info("REading mapping file: " + mappingFileName);
		long t1 = System.currentTimeMillis();
		ClassLoader cl = this.getClass().getClassLoader();

		InputStream istream = cl.getResourceAsStream(mappingFileName);
		DataInputStream in = new DataInputStream(istream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.contains(",")) {
				final String[] split = line.split(",");
				uniprotIDACCMap.put(split[0], split[1]);
				uniprotACCIDMap.put(split[1], split[0]);
			}
		}
		br.close();

		log.info(uniprotIDACCMap.size() + " entries mapped");
		log.info("in " + (System.currentTimeMillis() - t1) / 1000 + " sg");
	}

	public String getAccFromID(String uniprotID) {
		return UniprotId2AccMapping.uniprotIDACCMap.get(uniprotID);
	}

	public String getIDFromAcc(String uniprotACC) {
		return UniprotId2AccMapping.uniprotACCIDMap.get(uniprotACC);
	}
}
