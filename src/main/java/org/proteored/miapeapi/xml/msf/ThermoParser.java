package org.proteored.miapeapi.xml.msf;

import java.sql.SQLException;

import com.compomics.thermo_msf_parser.Parser;

public class ThermoParser {
	public void main() {
		String iMsfFileLocation = null;
		boolean iLowMemory = false;
		// ScoreType scoreType = new ScoreType();
		try {
			Parser msfParser = new Parser(iMsfFileLocation, iLowMemory);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
