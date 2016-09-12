package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.List;

import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Param;
import uk.ac.ebi.jmzidml.model.mzidml.SearchDatabase;

public class DatabaseImpl implements Database {
	private final SearchDatabase xmlDatabase;

	public DatabaseImpl(SearchDatabase databaseXML) {
		this.xmlDatabase = databaseXML;
	}

	@Override
	public String getDate() {
		if (xmlDatabase.getReleaseDate() == null)
			return null;
		return xmlDatabase.getReleaseDate().getTime().toGMTString();
	}

	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		if (xmlDatabase.getExternalFormatDocumentation() != null) {
			sb.append(Utils.EXTERNAL_FORMAT_DOCUMENTATION + ": ");
			sb.append(xmlDatabase.getExternalFormatDocumentation());
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		List<CvParam> parameters = xmlDatabase.getCvParam();
		for (CvParam param : parameters) {
			sb.append(param.getName());
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		return Utils.checkReturnedString(sb);

	}

	@Override
	public String getName() {
		Param databaseName = xmlDatabase.getDatabaseName();
		if (databaseName != null && databaseName.getCvParam() != null) {
			return databaseName.getCvParam().getName();
		}
		String name = xmlDatabase.getName();
		if (name != null && !"".equals(name))
			return name;
		if (databaseName != null && databaseName.getUserParam() != null) {
			return databaseName.getUserParam().getName();
		}
		return name;
	}

	@Override
	public String getNumVersion() {
		return xmlDatabase.getVersion();

	}

	@Override
	public String getSequenceNumber() {

		return String.valueOf(xmlDatabase.getNumDatabaseSequences());

	}

	@Override
	public String getUri() {
		return xmlDatabase.getLocation();
	}

	@Override
	public String toString() {
		return "DatabaseImpl [getDate()=" + getDate() + ", getDescription()=" + getDescription()
				+ ", getName()=" + getName() + ", getNumVersion()=" + getNumVersion()
				+ ", getSequenceNumber()=" + getSequenceNumber() + ", getUri()=" + getUri() + "]";
	}

}
