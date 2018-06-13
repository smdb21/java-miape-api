package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public class LightMiapeMSIListAdapter implements Adapter<List<MiapeMSIDocument>> {
	private final List<MiapeMSIDocument> miapeMSIs;

	public LightMiapeMSIListAdapter(List<MiapeMSIDocument> miapeMSIs) {
		this.miapeMSIs = miapeMSIs;
	}

	@Override
	public List<MiapeMSIDocument> adapt() {
		final List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		for (final MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
			ret.add(new LightMiapeMSIAdapter(miapeMSIDocument).adapt());
		}
		return ret;
	}

}
