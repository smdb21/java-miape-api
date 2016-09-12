package org.proteored.miapeapi.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.apache.log4j.Logger;
import org.seqxml.TSeq;
import org.seqxml.TSeqSet;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DatabaseInfo;
import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfConnException;
import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfException;
import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.DbfParamsException;
import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.InputException;
import uk.ac.ebi.www.ws.services.WSDbfetchDoclit.WSDBFetchServerProxy;

import com.compomics.util.protein.Protein;

public class ProteinSequenceRetrieval {
	private static final WSDBFetchServerProxy ws = new WSDBFetchServerProxy();
	private static DatabaseInfo[] databases;
	private static String[] proteinDBs = { "uniprotkb", "ipi" };
	private static HashMap<String, String> sequenceMap = new HashMap<String, String>();
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");

	private static JAXBContext jc;

	/**
	 * If not found, return NULL;
	 * 
	 * @param proteinAcc
	 * @return
	 * @throws DbfConnException
	 * @throws DbfParamsException
	 * @throws DbfException
	 * @throws InputException
	 * @throws RemoteException
	 */
	public static String getProteinSequence(String proteinAcc,
			boolean retrieveFromInternetIfNotCached) {

		List<String> proteinAccs = new ArrayList<String>();
		proteinAccs.add(proteinAcc);
		HashMap<String, String> proteinSequence = ProteinSequenceRetrieval
				.getProteinSequence(proteinAccs,
						retrieveFromInternetIfNotCached);
		return proteinSequence.get(proteinAcc);

	}

	public static String getProteinNameFromUniprot(String proteinAcc) {
		String urlText = "http://www.uniprot.org/uniprot/" + proteinAcc
				+ ".xml";
		return ProteinSequenceRetrieval.parseProteinNameXMLFromUniprot(urlText);

	}

	public static HashMap<String, String> getProteinSequence(
			List<String> proteinAccs, boolean retrieveFromInternetIfNotCached) {
		HashMap<String, String> ret = new HashMap<String, String>();

		try {
			List<String> uniprotAccs = new ArrayList<String>();
			List<String> ncbiAccs = new ArrayList<String>();

			for (String proteinAcc : proteinAccs) {

				if (sequenceMap.containsKey(proteinAcc)) {
					ret.put(proteinAcc, sequenceMap.get(proteinAcc));
					continue;
				}
				if (!retrieveFromInternetIfNotCached)
					continue;

				if (isUniProtACC(proteinAcc)) {
					uniprotAccs.add(proteinAcc);
				} else {
					ncbiAccs.add(proteinAcc);
				}

			}

			// Retrieve Uniprot Accs
			for (String proteinAcc : uniprotAccs) {
				String urlText = "http://www.uniprot.org/uniprot/" + proteinAcc
						+ ".xml";
				// String seqXML = URLDownloader.downloadURL(new URL(
				// URLParamEncoder.encode(urlText)));
				String seq = parseSeqXMLFromUniprot(urlText);
				seq = skipFastaHeader(seq);
				sequenceMap.put(proteinAcc, seq);
				ret.put(proteinAcc, seq);
				log.debug("Sequence from protein: " + proteinAcc
						+ " has been stored.");
				log.debug(sequenceMap.size() + " sequences stored");
			}

			// Retrieve NCBI Accs
			String csvNcbiAccs = "";
			for (String proteinAcc : ncbiAccs) {
				if (!"".equals(csvNcbiAccs))
					csvNcbiAccs += ",";
				csvNcbiAccs += proteinAcc;
			}
			if (!"".equals(csvNcbiAccs)) {
				if (jc == null)
					jc = JAXBContext.newInstance("org.seqxml");

				String urlText = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&rettype=fasta&retmode=xml&id="
						+ csvNcbiAccs;
				// String seqXML = URLDownloader.downloadURL(new URL(
				// URLParamEncoder.encode(urlText)));
				HashMap<String, String> seqs = parseSeqXMLFromNCBI(urlText);
				for (String proteinAcc : seqs.keySet()) {
					String seq = seqs.get(proteinAcc);
					sequenceMap.put(proteinAcc, seq);
					ret.put(proteinAcc, seq);
					log.info("Sequence from protein: " + proteinAcc
							+ " has been stored.");
					log.info(sequenceMap.size() + " sequences stored");
				}

			}

		} catch (JAXBException e) {

			e.printStackTrace();
		}
		return ret;
	}

	private static HashMap<String, String> parseSeqXMLFromNCBI(String seqXML) {
		HashMap<String, String> ret = new HashMap<String, String>();

		try {

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Object unmarshal = unmarshaller.unmarshal(new URL(seqXML));
			TSeqSet tSeqSet = (TSeqSet) unmarshal;

			List<TSeq> entries = tSeqSet.getTSeq();
			for (TSeq entry : entries) {
				String aAseq = entry.getTSeqSequence();
				if (aAseq != null) {
					ret.put("gi|" + entry.getTSeqGi(), aAseq);
				}
			}

		} catch (JAXBException e) {
			log.warn(e.getMessage());
		} catch (MalformedURLException e) {
			log.warn(e.getMessage());
		}
		return ret;
	}

	private static String parseSeqXMLFromUniprot(String seqXML) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			org.w3c.dom.Document doc = dBuilder.parse(seqXML);
			doc.getDocumentElement().normalize();

			// System.out.println("root of xml file"
			// + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("sequence");

			for (int i = 0; i < nodes.getLength(); i++) {
				org.w3c.dom.Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String attribute = element.getAttribute("length");
					if (!"".equals(attribute)) {
						NodeList childNodes = element.getChildNodes();
						return childNodes.item(0).getNodeValue().trim();
					}

				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String parseProteinNameXMLFromUniprot(String seqXML) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			org.w3c.dom.Document doc = dBuilder.parse(seqXML);
			doc.getDocumentElement().normalize();

			// System.out.println("root of xml file"
			// + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("fullName");

			for (int i = 0; i < nodes.getLength(); i++) {
				org.w3c.dom.Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					NodeList childNodes = element.getChildNodes();
					return childNodes.item(0).getNodeValue().trim();

				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean isUniProtACC(String proteinAcc) {
		if (proteinAcc.length() == 6)
			return true;
		if (proteinAcc.length() > 6
				&& (proteinAcc.substring(6, 7).equals(".") || proteinAcc
						.substring(6, 7).equals("-")))
			return true;
		return false;
	}

	public static String[] getValidDatabases() {
		final DatabaseInfo[] databases = ProteinSequenceRetrieval
				.getDatabases();
		String[] ret = new String[databases.length];
		int i = 0;
		for (DatabaseInfo databaseInfo : databases) {
			ret[i] = databaseInfo.getName();
			i++;
		}
		return ret;
	}

	private static String getValidDatabasesAsCSV() {
		StringBuilder sb = new StringBuilder();
		final String[] validDatabases = getValidDatabases();
		for (String string : validDatabases) {
			sb.append(string);
			sb.append(",");
		}
		return sb.toString();
	}

	private static DatabaseInfo[] getDatabases() {
		if (databases == null)
			try {
				databases = ws.getDatabaseInfoList();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		return databases;
	}

	private static boolean isNotValidDatabase(String database) {
		try {
			if (databases == null)
				databases = ws.getDatabaseInfoList();

			for (DatabaseInfo databaseInfo : databases) {
				if (databaseInfo.getName().equalsIgnoreCase(database)) {
					return false;
				}
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}

	private static String skipFastaHeader(String fetchBatch) {
		String newLine = System.getProperty("line.separator");
		if (fetchBatch != null) {
			// if (fetchBatch.contains(newLine)) {
			// StringBuilder sb = new StringBuilder();
			// final String[] split = fetchBatch.split(newLine);
			// for (int i = 0; i < split.length; i++) {
			// if (!split[i].startsWith(">"))
			// sb.append(split[i]);
			// }
			// return sb.toString();
			// } else {
			try {
				Protein protein = new Protein(fetchBatch);
				String sequence = protein.getSequence().getSequence();
				return sequence;
			} catch (IllegalArgumentException e) {

			}
			// }
		}
		return fetchBatch;
	}
}
