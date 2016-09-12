package org.proteored.miapeapi.xml.merge;

public interface MiapeMerger<T> {

	public T merge(T miapeDataOriginal, T miapeDataMetadata);
}
