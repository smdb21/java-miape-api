package org.proteored.miapeapi.experiment.model;

import java.util.List;

import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public abstract class IdentificationItem {
	/**
	 * Gets the MIAPE MSI document were this item was reported
	 * 
	 * @return
	 */
	public abstract MiapeMSIDocument getMiapeMSI();

	/**
	 * Get the identifier of the MIAPE MS document were the identification item
	 * is reported
	 * 
	 * @return
	 */
	public abstract Integer getMiapeMSReference();

	/**
	 * Gets the score value specified by a name
	 * 
	 * @param score
	 *            name
	 * @return
	 */
	public abstract Float getScore(String scoreName);

	/**
	 * Gets the default (if defined) score value
	 * 
	 * @param score
	 *            name
	 * @return
	 */
	public abstract Float getScore();

	/**
	 * Indicate if the identification item is a decoy hit or not according to an
	 * {@link FDRFilter}
	 * 
	 * @param filter
	 * @return
	 */

	public abstract boolean isDecoy(FDRFilter filter);

	public abstract void setDecoy(boolean isDecoy);

	/**
	 * Gets the list of scores
	 * 
	 * @return
	 */
	public abstract List<String> getScoreNames();

	public abstract boolean isDecoy();

}
