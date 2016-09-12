package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;

public class ValidationBuilder {
	String name;
	String results;
	Set<PostProcessingMethod> validationMethods;
	Set<Software> validationSoftwares;
	String globalThresholds;

	ValidationBuilder(String name) {
		this.name = name;
	}

	public ValidationBuilder results(String value) {
		results = value;
		return this;
	}

	public ValidationBuilder globalThresholds(String value) {
		globalThresholds = value;
		return this;
	}

	public ValidationBuilder validationMethods(Set<PostProcessingMethod> value) {
		validationMethods = value;
		return this;
	}

	public ValidationBuilder validationSoftwares(Set<Software> value) {
		validationSoftwares = value;
		return this;
	}

	public Validation build() {
		return new ValidationImpl(this);
	}
}