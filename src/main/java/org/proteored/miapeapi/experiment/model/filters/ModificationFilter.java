package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import gnu.trove.set.hash.TIntHashSet;

public class ModificationFilter implements Filter {
	private final List<ModificationFilterItem> modificationItemList = new ArrayList<ModificationFilterItem>();;

	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public static final String NOT_MODIFIED = "NOT MODIFIED";
	private final Software software;

	private final boolean separateNonConclusiveProteins;
	private final boolean doNotGroupNonConclusiveProteins;

	public void ModificationFilterItem() {

	}

	public ModificationFilter(String modificationName, LogicOperator operator, boolean contain, int number,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Software software) {
		ModificationFilterItem modifItem = new ModificationFilterItem(operator, modificationName, contain, number);
		this.modificationItemList.add(modifItem);
		this.software = software;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;

	}

	public ModificationFilter(boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins,
			Software software) {
		this.software = software;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;

	}

	public void addModificationItem(String modificationName, LogicOperator operator, boolean contain, int number) {
		ModificationFilterItem modifItem = new ModificationFilterItem(operator, modificationName, contain, number);
		this.modificationItemList.add(modifItem);

	}

	public void addModificationItem(ModificationFilterItem modifItem) {
		this.modificationItemList.add(modifItem);
	}

	public void removeModificationItem(int index) {
		this.modificationItemList.remove(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModificationFilter) {
			ModificationFilter filter = (ModificationFilter) obj;
			List<ModificationFilterItem> modificationFilterItems = filter.getModificationFilterItems();
			List<ModificationFilterItem> localModificationFilterItems = this.getModificationFilterItems();
			if (modificationFilterItems.size() != localModificationFilterItems.size())
				return false;

			for (int i = 0; i < localModificationFilterItems.size(); i++) {
				ModificationFilterItem localModificationFilterItem = localModificationFilterItems.get(i);
				ModificationFilterItem modificationFilterItem = modificationFilterItems.get(i);
				if (!localModificationFilterItem.equals(modificationFilterItem))
					return false;
			}
			return true;
		} else
			return super.equals(obj);
	}

	private TIntHashSet filterPeptides(List<ExtendedIdentifiedPeptide> identifiedPeptides,
			IdentificationSet currentIdSet) {
		log.info("Filtering by Modification: " + this);
		TIntHashSet ret = new TIntHashSet();
		for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
			if (passFilter(peptide)) {
				if (!ret.contains(peptide.getId()))
					ret.add(peptide.getId());
				else
					log.info("This peptide has passed already the threshold");
			}

		}
		log.info("Filtered " + ret.size() + " out of " + identifiedPeptides.size() + " peptides");
		return ret;
	}

	private boolean passFilter(ExtendedIdentifiedPeptide peptide) {
		LogicOperator operator = null;
		Boolean passFilter = false;
		for (int i = 0; i < this.modificationItemList.size(); i++) {

			operator = this.modificationItemList.get(i).getOperator();

			ModificationFilterItem modificationFilterItem = this.modificationItemList.get(i);
			boolean passFilterItemTMP = passFilterItem(peptide, modificationFilterItem);
			// if operator==null -> if this is the first modificationItem
			if (operator == null) {
				passFilter = passFilterItemTMP;
			} else {
				if (operator == LogicOperator.AND) {
					if (!passFilter || !passFilterItemTMP)
						passFilter = false;
					else
						passFilter = true;
				} else if (operator == LogicOperator.OR) {
					if (passFilter || passFilterItemTMP)
						passFilter = true;
					else
						passFilter = false;
				}
			}
		}

		return passFilter;
	}

	private boolean passFilterItem(ExtendedIdentifiedPeptide peptide, ModificationFilterItem filterItem) {
		if (peptide == null)
			return false;
		final Set<PeptideModification> modifications = peptide.getModifications();
		if (modifications != null && !modifications.isEmpty()) {
			boolean containsThisModification = false;
			int num = 0;
			for (PeptideModification modification : modifications) {
				if (modification.getName().equalsIgnoreCase(filterItem.getModifName())) {
					containsThisModification = true;
					num++;
				}
			}
			final Integer number = filterItem.getNumber();
			// log.info("Number=" + number);
			if (filterItem.isContain() && containsThisModification && (number == null || number.equals(0)))
				return true;

			if (filterItem.isContain() && containsThisModification && number.equals(num))
				return true;

			if (!filterItem.isContain() && !containsThisModification)
				return true;
		} else {
			if (ModificationFilter.NOT_MODIFIED.equals(filterItem.getModifName())) {
				return true;
			}
		}
		return false;

	}

	@Override
	public String toString() {
		String ret = "";
		for (int i = 0; i < this.modificationItemList.size(); i++) {
			ModificationFilterItem modifItem = this.modificationItemList.get(i);

			LogicOperator operator = this.modificationItemList.get(i).getOperator();
			if (operator != null)
				ret = " ( " + ret + " " + operator + " ";

			ret += modifItem.toString();
			if (operator != null)
				ret += " ) ";
		}
		return "Peptides " + ret;
	}

	public List<ModificationFilterItem> getModificationFilterItems() {
		return this.modificationItemList;
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
		List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager.getPeptidesFromProteinGroups(proteinGroups);
		TIntHashSet filteredPeptides = filterPeptides(identifiedPeptides, currentIdSet);
		return DataManager.filterProteinGroupsByPeptides(proteinGroups, doNotGroupNonConclusiveProteins,
				separateNonConclusiveProteins, filteredPeptides, currentIdSet.getCvManager());
	}

	@Override
	public boolean appliedToProteins() {

		return false;
	}

	@Override
	public boolean appliedToPeptides() {
		return true;
	}

	@Override
	public Software getSoftware() {
		return software;
	}

}
