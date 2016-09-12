package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.cv.ms.MassToleranceUnit;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class InputParameterBuilder {
	String name;
	String taxonomy;
	String cleavageRules;
	String aparameters;
	String misscleavages;
	String additionalCleavages;
	String aaModif;
	String minScore;
	String precursorMassTolerance;
	String massTolerancePmf;
	Set<Algorithm> scoringAlgorithms;
	Set<Database> databases;
	Set<AdditionalParameter> additionalParameters;
	String fragmentMassTolerance;
	String fragmentMassToleranceUnit;
	String massTolerancePmfUnit;
	String precursorMassToleranceUnit;
	String numEntries;
	String cleavageName;
	String scoringAlgorithm;
	String searchType;
	Software software;
	int id;

	InputParameterBuilder(String name) {
		this.name = name;
	}

	public InputParameterBuilder taxonomy(String value) {
		taxonomy = value;
		return this;
	}

	public InputParameterBuilder cleavageRules(String value) {
		cleavageRules = value;
		return this;
	}

	public InputParameterBuilder misscleavages(String value) {
		misscleavages = value;
		return this;
	}

	public InputParameterBuilder aparameters(String value) {
		aparameters = value;
		return this;
	}

	public InputParameterBuilder additionalCleavages(String value) {
		additionalCleavages = value;
		return this;
	}

	public InputParameterBuilder aaModif(String value) {
		aaModif = value;
		return this;
	}

	public InputParameterBuilder minScore(String value) {
		minScore = value;
		return this;
	}

	public InputParameterBuilder precursorMassTolerance(String value) {
		precursorMassTolerance = value;
		return this;
	}

	public InputParameterBuilder massTolerancePmf(String value) {
		massTolerancePmf = value;
		return this;
	}

	public InputParameterBuilder scoringAlgorithms(Set<Algorithm> value) {
		scoringAlgorithms = value;
		return this;
	}

	public InputParameterBuilder databases(Set<Database> value) {
		databases = value;
		return this;
	}

	public InputParameterBuilder additionalParameters(Set<AdditionalParameter> value) {
		additionalParameters = value;
		return this;
	}

	public InputParameterBuilder fragmentMassTolerance(String value) {
		fragmentMassTolerance = value;
		return this;
	}

	public InputParameterBuilder cleavageName(String value) {
		cleavageName = value;
		return this;
	}

	public InputParameterBuilder numEntries(String value) {
		numEntries = value;
		return this;
	}

	/**
	 * Set the fragment mass tolerance unit. It should be one of the possible
	 * values from {@link MassToleranceUnit}
	 **/
	public InputParameterBuilder fragmentMassToleranceUnit(String value) {
		fragmentMassToleranceUnit = value;
		return this;
	}

	/**
	 * Set the mass tolerance unit for PMF experiments. It should be one of the
	 * possible values from {@link MassToleranceUnit}
	 **/
	public InputParameterBuilder massTolerancePmfUnit(String value) {
		massTolerancePmfUnit = value;
		return this;
	}

	/**
	 * Set the precursor mass tolerance unit. It should be one of the possible
	 * values from {@link MassToleranceUnit}
	 **/
	public InputParameterBuilder precursorMassToleranceUnit(String value) {
		precursorMassToleranceUnit = value;
		return this;
	}

	public InputParameterBuilder scoringAlgorithm(String scoringAlgorithm) {
		this.scoringAlgorithm = scoringAlgorithm;
		return this;
	}

	public InputParameterBuilder searchType(String value) {
		this.searchType = value;
		return this;
	}

	public InputParameterBuilder software(Software value) {
		this.software = value;
		return this;
	}

	public InputParameterBuilder id(int value) {
		this.id = value;
		return this;
	}

	public InputParameter build() {
		return new InputParameterImpl(this);
	}

}