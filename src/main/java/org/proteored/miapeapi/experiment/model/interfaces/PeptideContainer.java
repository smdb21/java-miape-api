package org.proteored.miapeapi.experiment.model.interfaces;

import java.util.List;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;

public interface PeptideContainer {

	public Float getBestPeptideScore();

	public Float getBestPeptideScore(String scoreName);

	public ExtendedIdentifiedPeptide getBestPeptide();

	public ExtendedIdentifiedPeptide getBestPeptide(String scoreName);

	public List<ExtendedIdentifiedPeptide> getPeptides();

}
