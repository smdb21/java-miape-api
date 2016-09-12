package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;

public class ValidationImpl implements Validation {
	private final String name;
	private final String results;
	private final Set<PostProcessingMethod> validationMethods;
	private final Set<Software> validationSoftwares;
	private String globalThresholds;

	public ValidationImpl(ValidationBuilder validationBuilder) {
		this.name = validationBuilder.name;
		this.results = validationBuilder.results;
		this.validationMethods = validationBuilder.validationMethods;
		this.validationSoftwares = validationBuilder.validationSoftwares;
		this.globalThresholds = validationBuilder.globalThresholds;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getStatisticalAnalysisResults() {
		return results;
	}

	@Override
	public Set<PostProcessingMethod> getPostProcessingMethods() {
		return validationMethods;
	}

	@Override
	public Set<Software> getPostProcessingSoftwares() {
		return validationSoftwares;
	}

	@Override
	public String getGlobalThresholds() {
		return globalThresholds;
	}

}
