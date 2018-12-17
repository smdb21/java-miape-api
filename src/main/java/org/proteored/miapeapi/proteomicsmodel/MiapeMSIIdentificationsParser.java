package org.proteored.miapeapi.proteomicsmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.LocalOboControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.xml.msi.MIAPEMSIXmlFile;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.parsers.idparser.IdentificationsParser;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.factories.MSRunEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class MiapeMSIIdentificationsParser extends IdentificationsParser
		implements HasMiapeMSIs, MSRunProviderByInputData {
	private final static Logger log = Logger.getLogger(MiapeMSIIdentificationsParser.class);
	private final boolean processInParallel = true;
	private ControlVocabularyManager cvManager;
	private final Map<String, MSRun> msRunsbyInputData = new THashMap<String, MSRun>();
	private ArrayList<MiapeMSIDocument> miapeMSIs;

	public MiapeMSIIdentificationsParser(URL u) throws IOException {
		super(u);
	}

	public MiapeMSIIdentificationsParser(String runid, RemoteSSHFileReference s) throws FileNotFoundException {
		super(runid, s);
	}

	public MiapeMSIIdentificationsParser(Map<String, RemoteSSHFileReference> s) throws FileNotFoundException {
		super(s);
	}

	public MiapeMSIIdentificationsParser(Collection<File> s) throws FileNotFoundException {
		super(s);
	}

	public MiapeMSIIdentificationsParser(File file) throws FileNotFoundException {
		super(file);
	}

	public MiapeMSIIdentificationsParser(String runId, InputStream f) {
		super(runId, f);

	}

	private List<MiapeMSIDocument> getMiapeMSIDocuments() {
		if (miapeMSIs == null) {
			miapeMSIs = new ArrayList<MiapeMSIDocument>();
			for (final InputStream is : super.fs.values()) {
				final MiapeMSIDocument miapeMSI = parseMiapeMSIFromInputStream(is);
				if (miapeMSI != null) {
					miapeMSIs.add(miapeMSI);
				}
			}
		}
		return miapeMSIs;
	}

	/**
	 * Any class that extends this class, would have to override this method in
	 * order to read the miapeMSI from other type of file
	 * 
	 * @param is
	 * 
	 * @return
	 */
	@Override
	public MiapeMSIDocument parseMiapeMSIFromInputStream(InputStream is) {
		try {
			final MiapeXmlFile<MiapeMSIDocument> xmlFile = new MIAPEMSIXmlFile(IOUtils.toByteArray(is));
			final MiapeMSIDocument miapeMSI = MiapeMSIXmlFactory.getFactory().toDocument(xmlFile,
					getControlVocabularyManager(), null, null, null);
			return miapeMSI;
		} catch (MiapeDatabaseException | MiapeSecurityException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void process(boolean checkFormat) {
		int numDecoy = 0;
		for (final MiapeMSIDocument miapeMSI : getMiapeMSIDocuments()) {

			if (miapeMSI.getSoftwares() != null) {
				if (!miapeMSI.getSoftwares().isEmpty()) {
					for (final Software software : miapeMSI.getSoftwares()) {
						searchEngines.add(software.getName());
					}
				}
			}
			final Set<IdentifiedProteinSet> identifiedProteinSets = miapeMSI.getIdentifiedProteinSets();
			for (final IdentifiedProteinSet identifiedProteinSet : identifiedProteinSets) {
				final String proteinSetName = identifiedProteinSet.getName();
				final Set<MSRun> msRuns = new THashSet<MSRun>();
				for (final InputDataSet inputDataSet : identifiedProteinSet.getInputDataSets()) {
					for (final InputData inputData : inputDataSet.getInputDatas()) {
						msRuns.add(getMSRunByInputData(inputData));
					}
				}
				for (final IdentifiedProtein protein : identifiedProteinSet.getIdentifiedProteins().values()) {
					Protein p = null;
					final String acc = FastaParser.getACC(protein.getAccession()).getAccession();
					if (StaticProteomicsModelStorage.containsProtein(msRuns, null, acc)) {
						p = StaticProteomicsModelStorage.getSingleProtein(null, acc, msRuns);
					} else {
						// if (dbIndex == null) {
						p = new ProteinFromMIAPEMSI(protein, this);
					}
					if (proteinsByAccession.containsKey(p.getAccession())) {
						final Protein p2 = proteinsByAccession.get(p.getAccession());
						p.mergeWithProtein(p2);
					}
					if (!searchEngines.isEmpty()) {
						p.setSearchEngine(searchEngines.iterator().next());
					}
					boolean skip = false;
					if (decoyPattern != null) {
						final Matcher matcher = decoyPattern.matcher(p.getAccession());
						if (matcher.find()) {
							numDecoy++;
							skip = true;
						}
					}
					if (!skip) {
						proteinsByAccession.put(p.getAccession(), p);
						final List<IdentifiedPeptide> identifiedPeptides = protein.getIdentifiedPeptides();
						for (final IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
							final MSRun msRun = getMSRunByInputData(identifiedPeptide.getInputData());
							PSM psm = new PSMFromMIAPEMSI(identifiedPeptide, msRun, this);
							if (StaticProteomicsModelStorage.containsPSM(msRun, null, psm.getIdentifier())) {
								psm = StaticProteomicsModelStorage.getSinglePSM(msRun, null, psm.getIdentifier());
							}
							psmTableByPSMID.put(psm.getIdentifier(), psm);
						}
					}
				}
			}
		}
		if (numDecoy > 0) {
			log.info(numDecoy + " proteins were discarded as decoy");
		}
	}

	@Override
	public MSRun getMSRunByInputData(InputData inputData) {
		if (!msRunsbyInputData.containsKey(inputData.getName())) {
			MSRun msRun = null;
			if (StaticProteomicsModelStorage.containsMSRun(inputData.getName())) {
				msRun = StaticProteomicsModelStorage.getMSRun(inputData.getName());
			} else {
				msRun = new MSRunEx(inputData.getName(), inputData.getSourceDataUrl());
				StaticProteomicsModelStorage.addMSRun(msRun);
			}
			msRunsbyInputData.put(inputData.getName(), msRun);
		}
		return msRunsbyInputData.get(inputData.getName());
	}

	protected ControlVocabularyManager getControlVocabularyManager() {
		if (cvManager == null) {
			cvManager = new LocalOboControlVocabularyManager();
		}
		return cvManager;
	}

	public boolean isProcessInParallel() {
		return processInParallel;
	}

}
