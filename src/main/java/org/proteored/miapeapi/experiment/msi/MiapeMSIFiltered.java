package org.proteored.miapeapi.experiment.msi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.InterruptedMIAPEThreadException;
import org.proteored.miapeapi.exceptions.MiapeDataInconsistencyException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.factories.msi.MiapeMSIDocumentFactory;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class MiapeMSIFiltered implements MiapeMSIDocument {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final MiapeMSIDocument miapeMSI;
	private final IdentificationSet idSet;
	private final List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
	private final Set<IdentifiedProteinSet> proteinSets = new THashSet<IdentifiedProteinSet>();

	private final MiapeMSIXmlFactory xmlFactory;

	private final ControlVocabularyManager cvManager;

	private boolean throwIncosistencies = true;

	public MiapeMSIFiltered(MiapeMSIDocument miapeMSI, IdentificationSet idSet, ControlVocabularyManager cvManager) {
		this.miapeMSI = miapeMSI;
		if (miapeMSI == null)
			throw new IllegalMiapeArgumentException();
		this.idSet = idSet;
		xmlFactory = MiapeMSIXmlFactory.getFactory();
		this.cvManager = cvManager;
		if (miapeMSI.getProject() != null
				&& "ProteoRed Multicentric Experiment 6".equals(miapeMSI.getProject().getName())) {
			throwIncosistencies = false;
		}
		filterOutProteinsAndPeptidesNEW();
	}

	private void filterOutProteinsAndPeptidesNEW() {
		if (idSet != null) {
			log.info("Filtering MIAPE MSI " + miapeMSI.getId() + ": " + miapeMSI.getName() + " by idSet:  "
					+ idSet.getName());
			TIntHashSet miapeProteinIds = getMiapeProteinIds(miapeMSI);
			TIntHashSet miapePeptideIds = getMiapePeptideIds(miapeMSI);

			final List<ProteinGroup> validProteinGroups = idSet.getIdentifiedProteinGroups();
			TIntObjectHashMap<ExtendedIdentifiedProtein> validProteinMap = new TIntObjectHashMap<ExtendedIdentifiedProtein>();
			for (ProteinGroup validProteinGroup : validProteinGroups) {
				for (ExtendedIdentifiedProtein validProtein : validProteinGroup) {
					validProteinMap.put(validProtein.getId(), validProtein);
				}
			}
			final List<ExtendedIdentifiedPeptide> validPeptides = idSet.getIdentifiedPeptides();
			TIntObjectHashMap<ExtendedIdentifiedPeptide> validPeptideMap = new TIntObjectHashMap<ExtendedIdentifiedPeptide>();
			for (ExtendedIdentifiedPeptide validPeptide : validPeptides) {
				validPeptideMap.put(validPeptide.getId(), validPeptide);

			}

			Map<String, IdentifiedProtein> resultingFilteredProteins = new THashMap<String, IdentifiedProtein>();
			int total = validProteinGroups.size();
			log.info("Filtering " + total + " protein groups");
			for (int proteinId : miapeProteinIds._set) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedMIAPEThreadException("Task cancelled");
				}
				if (validProteinMap.containsKey(proteinId)) {
					ExtendedIdentifiedProtein validProtein = validProteinMap.get(proteinId);
					List<ExtendedIdentifiedPeptide> peptides2 = validProtein.getPeptides();
					// Just store if the protein has peptides
					if (peptides2 != null && !peptides2.isEmpty()) {
						IdentifiedProteinFiltered proteinFiltered = new IdentifiedProteinFiltered(validProtein);
						resultingFilteredProteins.put(proteinFiltered.getAccession(), proteinFiltered);
					} else {
						log.warn("The protein " + proteinId + " has no peptides in the idSet, so it will skipped");
					}
				} else {
					// log.info("The protein " + proteinId
					// + " has been filtered out from the MIAPE");
				}
			}
			total = validPeptides.size();
			log.info("Filtering " + total + " peptides");
			for (int peptideId : miapePeptideIds._set) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedMIAPEThreadException("Task cancelled");
				}
				if (validPeptideMap.containsKey(peptideId)) {
					ExtendedIdentifiedPeptide validPeptide = validPeptideMap.get(peptideId);
					List<ExtendedIdentifiedProtein> proteins = validPeptide.getProteins();
					// Just store if the peptide has associated proteins
					if (proteins != null && !proteins.isEmpty()) {
						peptides.add(new IdentifiedPeptideFiltered(validPeptide));
					} else {
						log.warn("Peptide " + peptideId + " without proteins");
					}
				}
			}

			// Link proteins and peptides
			linkProteinsAndPeptides(resultingFilteredProteins, peptides);

			for (IdentifiedPeptide peptide : peptides) {
				List<IdentifiedProtein> identifiedProteins = peptide.getIdentifiedProteins();
				if (identifiedProteins == null || identifiedProteins.isEmpty())
					log.warn("Peptide " + peptide.getId() + " without proteins");
			}

			for (IdentifiedProtein protein : resultingFilteredProteins.values()) {
				List<IdentifiedPeptide> identifiedPeptides1 = protein.getIdentifiedPeptides();
				if (identifiedPeptides1 == null || identifiedPeptides1.isEmpty())
					log.warn("Protein " + protein.getId() + " without peptides");
			}

			log.info("Filtered " + miapePeptideIds.size() + " peptides to " + peptides.size() + " peptides");
			log.info("Filtered " + miapeProteinIds.size() + " proteins to " + resultingFilteredProteins.size()
					+ " proteins");
			log.info(validPeptideMap.size() + " = " + peptides.size() + " peptides");
			log.info(validProteinMap.size() + " =  " + resultingFilteredProteins.size() + " proteins");

			IdentifiedProteinSet miapeProteinSet = miapeMSI.getIdentifiedProteinSets().iterator().next();
			IdentifiedProteinSet proteinSet = MiapeMSIDocumentFactory
					.createIdentifiedProteinSetBuilder(miapeProteinSet.getName())
					.fileLocation(miapeProteinSet.getFileLocation()).inputDataSets(miapeProteinSet.getInputDataSets())
					.identifiedProteins(resultingFilteredProteins).build();
			proteinSets.add(proteinSet);

		}

	}

	private void linkProteinsAndPeptides(Map<String, IdentifiedProtein> identifiedProteins,
			List<IdentifiedPeptide> identifiedPeptides) {
		if (!identifiedProteins.isEmpty() && !identifiedPeptides.isEmpty()) {
			log.info("Linking " + identifiedProteins.size() + " proteins with " + identifiedPeptides.size()
					+ " peptides");
			// Create PEPTIDE MAP
			TIntObjectHashMap<IdentifiedPeptide> peptideMap = new TIntObjectHashMap<IdentifiedPeptide>();
			for (IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
				peptideMap.put(identifiedPeptide.getId(), identifiedPeptide);
			}
			// Create PROTEIN MAP
			TIntObjectHashMap<IdentifiedProtein> proteinMap = new TIntObjectHashMap<IdentifiedProtein>();
			for (IdentifiedProtein identifiedProtein : identifiedProteins.values()) {
				proteinMap.put(identifiedProtein.getId(), identifiedProtein);
			}
			log.info("Linking " + proteinMap.size() + " with " + peptideMap.size() + " peptides");
			// ITERATE OVER PROTEINS
			for (IdentifiedProtein identifiedProtein : identifiedProteins.values()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedMIAPEThreadException("Task cancelled");
				}
				IdentifiedProteinFiltered filteredProtein = (IdentifiedProteinFiltered) identifiedProtein;
				TIntHashSet peptidesFromProteinIds = filteredProtein.getValidPeptideIds();
				if (peptidesFromProteinIds != null) {
					TIntIterator iterator = peptidesFromProteinIds.iterator();
					while (iterator.hasNext()) {
						int identifiedPeptideID = iterator.next();
						if (peptideMap.containsKey(identifiedPeptideID)) {
							IdentifiedPeptideFiltered peptideInMap = (IdentifiedPeptideFiltered) peptideMap
									.get(identifiedPeptideID);
							filteredProtein.addPeptide(peptideInMap);
							peptideInMap.addProtein(identifiedProtein);
						} else {
							if (throwIncosistencies) {
								throw new MiapeDataInconsistencyException(identifiedPeptideID
										+ ", a peptide from protein " + identifiedProtein.getId() + " is not found");
							}
						}
					}
				} else {
					log.warn("Peptide without proteins");
				}
			}
			// ITERATE OVER PEPTIDES
			for (IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
				IdentifiedPeptideFiltered filteredPeptide = (IdentifiedPeptideFiltered) identifiedPeptide;
				TIntHashSet proteinsFromPeptideIds = filteredPeptide.getValidProteinIds();
				if (proteinsFromPeptideIds != null) {
					TIntIterator iterator = proteinsFromPeptideIds.iterator();
					while (iterator.hasNext()) {
						int identifiedProteinID = iterator.next();
						if (proteinMap.containsKey(identifiedProteinID)) {
							IdentifiedProteinFiltered proteinInMap = (IdentifiedProteinFiltered) proteinMap
									.get(identifiedProteinID);
							filteredPeptide.addProtein(proteinInMap);
							proteinInMap.addPeptide(identifiedPeptide);
						} else {
							if (throwIncosistencies) {
								throw new MiapeDataInconsistencyException(identifiedProteinID
										+ ", a protein from peptide " + identifiedPeptide.getId() + " is not found");
							}
						}
					}
				} else {
					log.warn("Protein without peptides");
				}
			}
		}
		log.info("End linking proteins and peptides");
	}

	private TIntHashSet getMiapeProteinIds(MiapeMSIDocument miapeMSI2) {
		TIntHashSet ret = new TIntHashSet();
		if (miapeMSI2 != null) {
			Set<IdentifiedProteinSet> proteinSets = miapeMSI2.getIdentifiedProteinSets();
			if (proteinSets != null) {
				for (IdentifiedProteinSet proteinset : proteinSets) {
					Map<String, IdentifiedProtein> identifiedProteins = proteinset.getIdentifiedProteins();
					if (identifiedProteins != null) {
						for (IdentifiedProtein protein : identifiedProteins.values()) {
							ret.add(protein.getId());
						}
					}
					log.info(ret.size() + " proteins from the MIAPE MSI ID:" + miapeMSI2.getId());
				}
			}
		}
		return ret;
	}

	private TIntHashSet getMiapePeptideIds(MiapeMSIDocument miapeMSI2) {
		TIntHashSet ret = new TIntHashSet();
		if (miapeMSI2 != null) {
			List<IdentifiedPeptide> identifiedPeptides = miapeMSI2.getIdentifiedPeptides();
			if (identifiedPeptides != null) {
				for (IdentifiedPeptide identifiedPeptide : identifiedPeptides) {
					ret.add(identifiedPeptide.getId());
				}
				log.info(ret.size() + " peptides from the MIAPE MSI ID:" + miapeMSI2.getId());
			}
		}
		return ret;
	}

	// private void filterOutProteinsAndPeptides2() {
	// if (this.idSet != null) {
	// log.info("Filtering MIAPE MSI " + miapeMSI.getName()
	// + " by idSet: " + this.idSet.getName());
	//
	// // filter peptides
	// final List<ExtendedIdentifiedPeptide> identifiedPeptides = this.idSet
	// .getIdentifiedPeptides();
	// HashMap<String, IdentifiedProtein> proteins = new THashMap<String,
	// IdentifiedProtein>();
	// for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide :
	// identifiedPeptides) {
	// if (extendedIdentifiedPeptide.getMiapeMSI().getId() == this.miapeMSI
	// .getId()) {
	// extendedIdentifiedPeptide.setAsFiltered(true);
	// this.peptides.add(extendedIdentifiedPeptide);
	// List<ExtendedIdentifiedProtein> proteinsFromPeptide =
	// extendedIdentifiedPeptide
	// .getProteins();
	// for (ExtendedIdentifiedProtein extendedIdentifiedProtein :
	// proteinsFromPeptide) {
	// if (!proteins.containsKey(extendedIdentifiedProtein
	// .getAccession())) {
	// extendedIdentifiedProtein.setAsFiltered(true);
	// proteins.put(
	// extendedIdentifiedProtein.getAccession(),
	// extendedIdentifiedProtein);
	// } else {
	//
	// }
	// }
	// } else {
	// // log.warn("This peptide should be assigned to the MIAPE MSI "
	// // + this.miapeMSI.getId());
	// }
	//
	// }
	//
	// log.info("Keeping " + this.peptides.size()
	// + " peptides and should be " + identifiedPeptides.size()
	// + " and " + proteins.size() + " proteins");
	//
	// IdentifiedProteinSet firstProteinSet = miapeMSI
	// .getIdentifiedProteinSets().iterator().next();
	// IdentifiedProteinSet proteinSet = MiapeMSIDocumentFactory
	// .createIdentifiedProteinSetBuilder(
	// firstProteinSet.getName())
	// .fileLocation(firstProteinSet.getFileLocation())
	// .inputDataSets(firstProteinSet.getInputDataSets())
	// .identifiedProteins(proteins).build();
	// this.proteinSets.add(proteinSet);
	// log.info("Keeping " + proteins.size() + " proteins");
	// }
	//
	// }

	private TIntHashSet getPeptideIdentifiers(IdentificationSet idSet2) {
		TIntHashSet ret = new TIntHashSet();
		List<ExtendedIdentifiedPeptide> peptides = idSet2.getIdentifiedPeptides();
		for (ExtendedIdentifiedPeptide peptide : peptides) {
			ret.add(peptide.getId());
		}
		return ret;
	}

	private TIntHashSet getProteinIdentifiers(IdentificationSet idSet2) {
		TIntHashSet ret = new TIntHashSet();
		List<ProteinGroup> proteinGroups = idSet2.getIdentifiedProteinGroups();
		for (ProteinGroup proteinGroup : proteinGroups) {
			for (ExtendedIdentifiedProtein protein : proteinGroup) {
				ret.add(protein.getId());
			}
		}
		return ret;
	}

	@Override
	public int getId() {
		return miapeMSI.getId();
	}

	@Override
	public String getVersion() {
		return miapeMSI.getVersion();
	}

	@Override
	public Project getProject() {
		return miapeMSI.getProject();
	}

	@Override
	public User getOwner() {
		return miapeMSI.getOwner();
	}

	@Override
	public String getName() {
		return miapeMSI.getName();
	}

	@Override
	public MiapeDate getDate() {
		return miapeMSI.getDate();
	}

	@Override
	public Date getModificationDate() {
		return miapeMSI.getModificationDate();
	}

	@Override
	public Boolean getTemplate() {
		return miapeMSI.getTemplate();
	}

	@Override
	public String getAttachedFileLocation() {
		return miapeMSI.getAttachedFileLocation();
	}

	@Override
	public ValidationReport getValidationReport() {
		return miapeMSI.getValidationReport();
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		return miapeMSI.store();
	}

	@Override
	public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
		miapeMSI.delete(userName, password);

	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml() {
		if (xmlFactory != null) {
			return xmlFactory.toXml(this, cvManager);
		}
		throw new IllegalMiapeArgumentException("The xml Factory is not defined.");
	}

	@Override
	public int getMSDocumentReference() {
		return miapeMSI.getMSDocumentReference();
	}

	@Override
	public void setReferencedMSDocument(int id) {
		miapeMSI.setReferencedMSDocument(id);

	}

	@Override
	public String getGeneratedFilesURI() {
		return miapeMSI.getGeneratedFilesURI();
	}

	@Override
	public String getGeneratedFilesDescription() {
		return miapeMSI.getGeneratedFilesDescription();
	}

	@Override
	public Set<Software> getSoftwares() {
		return miapeMSI.getSoftwares();
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return miapeMSI.getInputDataSets();
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		return miapeMSI.getInputParameters();
	}

	@Override
	public Set<Validation> getValidations() {
		final Set<Validation> validations = miapeMSI.getValidations();
		try {
			validations.add(new ValidationImpl(idSet));
			return validations;
		} catch (IllegalMiapeArgumentException e) {

		}
		return null;
	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {
		return proteinSets;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return peptides;
	}

	@Override
	public MSContact getContact() {
		return miapeMSI.getContact();
	}

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		return miapeMSI.getAdditionalInformations();
	}

	@Override
	public void setId(int id) {
		miapeMSI.setId(id);

	}

}
