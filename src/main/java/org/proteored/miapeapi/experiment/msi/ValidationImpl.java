package org.proteored.miapeapi.experiment.msi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;

public class ValidationImpl implements Validation {
	private static final Object NEW_LINE = System.getProperty("line.separator");;
	private List<Filter> filters;

	public ValidationImpl(IdentificationSet idSet) {
		if (idSet == null)
			throw new IllegalMiapeArgumentException("Identification set is null");
		final List<Filter> filters = idSet.getFilters();
		if (filters != null && !filters.isEmpty())
			this.filters = filters;
		else
			throw new IllegalMiapeArgumentException("Filters are null");
	}

	@Override
	public String getName() {
		final Software software = this.filters.get(0).getSoftware();
		if (software != null)
			return "Post-processing by " + software.getName();
		else
			return "Post-processing";
	}

	@Override
	public Set<PostProcessingMethod> getPostProcessingMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Software> getPostProcessingSoftwares() {
		Set<Software> softwares = new HashSet<Software>();
		final Software software = this.filters.get(0).getSoftware();
		if (software != null) {
			softwares.add(software);
			return softwares;
		} else
			return null;
	}

	@Override
	public String getStatisticalAnalysisResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGlobalThresholds() {
		return getStringFromFilters();
	}

	private String getStringFromFilters() {
		StringBuilder sb = new StringBuilder();
		for (Filter filter : this.filters) {
			if (!"".equals(sb.toString()))
				sb.append(NEW_LINE);
			sb.append(filter.toString());
		}
		return sb.toString();
	}

}
