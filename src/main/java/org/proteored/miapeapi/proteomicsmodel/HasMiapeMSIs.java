package org.proteored.miapeapi.proteomicsmodel;

import java.io.InputStream;

import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public interface HasMiapeMSIs {
	public MiapeMSIDocument parseMiapeMSIFromInputStream(InputStream is);
}
