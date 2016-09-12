package org.proteored.miapeapi.interfaces.xml;

import java.io.File;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public interface MiapePrideXmlFile {
	public MiapeMSDocument toMiapeMS() throws MiapeDatabaseException, MiapeSecurityException;

	public MiapeMSIDocument toMiapeMSI() throws MiapeDatabaseException, MiapeSecurityException;

	public File toFile();

	public String getFileUrl();
}
