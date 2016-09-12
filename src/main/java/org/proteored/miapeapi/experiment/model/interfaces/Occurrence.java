package org.proteored.miapeapi.experiment.model.interfaces;

import java.util.List;
import java.util.Set;

public interface Occurrence<T> {

	public T getFirstOccurrence();

	public void addOccurrence(T t);

	public List<T> getItemList();

	public Set<String> getScoreNames();
}
