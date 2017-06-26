package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.pi.ParIterator;
import edu.scripps.yates.pi.reductions.Reducible;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

public class ProteinDetectionHypothesisParallelProcesser extends Thread {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final ParIterator<ProteinDetectionHypothesis> sirParallelIterator;
	private int iNumber = -1;
	private final Reducible<Map<String, ProteinDetectionHypothesis>> pdhWithPeptideEvidenceLocal;

	public ProteinDetectionHypothesisParallelProcesser(
			ParIterator<ProteinDetectionHypothesis> iterator, int processID,
			Reducible<Map<String, ProteinDetectionHypothesis>> reduciblePDHs) {
		sirParallelIterator = iterator;
		iNumber = processID;
		pdhWithPeptideEvidenceLocal = reduciblePDHs;
	}

	@Override
	public void run() {
		Map<String, ProteinDetectionHypothesis> pdhMap = new HashMap<String, ProteinDetectionHypothesis>();
		Map<ProteinDetectionHypothesis, DBSequence> pdhDBSeqMap = new HashMap<ProteinDetectionHypothesis, DBSequence>();
		pdhWithPeptideEvidenceLocal.set(pdhMap);

		while (sirParallelIterator.hasNext()) {
			try {
				ProteinDetectionHypothesis proteinHypothesisXML = sirParallelIterator
						.next();

				for (PeptideHypothesis peptideHypothesisXML : proteinHypothesisXML
						.getPeptideHypothesis()) {
					if (!pdhMap.containsKey(peptideHypothesisXML
							.getPeptideEvidenceRef()))
						pdhMap.put(
								peptideHypothesisXML.getPeptideEvidenceRef(),
								proteinHypothesisXML);
				}
			} catch (Exception e) {
				sirParallelIterator.register(e);
			}
		}
	}

}
