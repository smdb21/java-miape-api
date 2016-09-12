package org.proteored.miapeapi.experiment.model.sort;


/**
 * Represents the parameters needed to sort a list of proteins or peptides, that
 * is the score that is going to determine the order, and the order (ASCNEDANT
 * or DESCENDANT)
 * 
 * @author Salva
 * 
 */
public class SortingParameters {

	private final String scoreName;
	private final Order order;

	public SortingParameters(String scoreName, Order order) {
		this.order = order;
		this.scoreName = scoreName;
	}

	/**
	 * @return the scoreName
	 */
	public String getScoreName() {
		return scoreName;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SortingParameters) {
			SortingParameters sorting = (SortingParameters) obj;
			if (!this.order.equals(sorting.order))
				return false;
			if (!this.scoreName.equals(sorting.scoreName))
				return false;
			return true;
		}
		return super.equals(obj);
	}

}
