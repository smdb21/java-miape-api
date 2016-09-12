package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class InputParameterImpl implements InputParameter {
	private final String name;
	private final String taxonomy;
	private final String cleavageRules;
	private final String aparameters;
	private final String misscleavages;
	private final String additionalCleavages;
	private final String aaModif;
	private final String minScore;
	private final String massToleranceMs;
	private final String massTolerancePmf;
	private final Set<Database> databases;
	private final Set<AdditionalParameter> additionalParameters;
	private final String fragmentMassTolerance;
	private final String fragmentMassToleranceUnit;
	private final String massTolerancePmfUnit;
	private final String precursorMassToleranceUnit;
	private final String numEntries;
	private final String cleavageName;
	private final String scoringAlgorithm;
	private final String searchType;
	private final Software software;
	private final int id;

	public InputParameterImpl(InputParameterBuilder inputParameterBuilder) {
		this.name = inputParameterBuilder.name;
		this.taxonomy = inputParameterBuilder.taxonomy;
		this.cleavageRules = inputParameterBuilder.cleavageRules;
		this.aparameters = inputParameterBuilder.aparameters;
		this.misscleavages = inputParameterBuilder.misscleavages;
		this.additionalCleavages = inputParameterBuilder.additionalCleavages;
		this.aaModif = inputParameterBuilder.aaModif;
		this.minScore = inputParameterBuilder.minScore;
		this.massToleranceMs = inputParameterBuilder.precursorMassTolerance;
		this.massTolerancePmf = inputParameterBuilder.massTolerancePmf;
		this.databases = inputParameterBuilder.databases;
		this.fragmentMassTolerance = inputParameterBuilder.fragmentMassTolerance;
		this.fragmentMassToleranceUnit = inputParameterBuilder.fragmentMassToleranceUnit;
		this.massTolerancePmfUnit = inputParameterBuilder.massTolerancePmfUnit;
		this.precursorMassToleranceUnit = inputParameterBuilder.precursorMassToleranceUnit;
		this.numEntries = inputParameterBuilder.numEntries;
		this.cleavageName = inputParameterBuilder.cleavageName;
		this.additionalParameters = inputParameterBuilder.additionalParameters;
		this.scoringAlgorithm = inputParameterBuilder.scoringAlgorithm;
		this.searchType = inputParameterBuilder.searchType;
		this.software = inputParameterBuilder.software;
		this.id = inputParameterBuilder.id;
	}

	@Override
	public String getAaModif() {
		return aaModif;
	}

	@Override
	public String getAdditionalCleavages() {
		return additionalCleavages;
	}

	@Override
	public String getCleavageRules() {
		return cleavageRules;
	}

	@Override
	public Set<Database> getDatabases() {
		return databases;
	}

	@Override
	public String getPrecursorMassTolerance() {
		return massToleranceMs;
	}

	@Override
	public String getPmfMassTolerance() {
		return massTolerancePmf;
	}

	@Override
	public String getMinScore() {
		return minScore;
	}

	@Override
	public String getMisscleavages() {
		return misscleavages;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTaxonomy() {
		return taxonomy;
	}

	@Override
	public String getFragmentMassTolerance() {
		return fragmentMassTolerance;
	}

	@Override
	public String getFragmentMassToleranceUnit() {
		return fragmentMassToleranceUnit;
	}

	@Override
	public String getPmfMassToleranceUnit() {
		return massTolerancePmfUnit;
	}

	@Override
	public String getPrecursorMassToleranceUnit() {
		return precursorMassToleranceUnit;
	}

	@Override
	public String getCleavageName() {
		return cleavageName;
	}

	@Override
	public String getNumEntries() {
		return numEntries;
	}

	@Override
	public Set<AdditionalParameter> getAdditionalParameters() {
		return additionalParameters;

	}

	@Override
	public String getScoringAlgorithm() {
		return this.scoringAlgorithm;
	}

	@Override
	public String getSearchType() {
		return this.searchType;
	}

	@Override
	public Software getSoftware() {
		return this.software;
	}

	@Override
	public int getId() {
		return id;
	}

}
