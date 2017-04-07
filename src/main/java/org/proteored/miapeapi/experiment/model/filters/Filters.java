/**
 * 
 */
package org.proteored.miapeapi.experiment.model.filters;

/**
 * @author Salva
 *
 */
public interface Filters<T> {
	public boolean isValid(T t);

	public boolean canCheck(Object obj);
}
