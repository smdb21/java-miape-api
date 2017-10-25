package org.proteored.miapeapi.test.factories;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.SpectrometerName;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.ProjectBuilder;
import org.proteored.miapeapi.factories.UserBuilder;
import org.proteored.miapeapi.factories.ms.AnalyserBuilder;
import org.proteored.miapeapi.factories.ms.EsiBuilder;
import org.proteored.miapeapi.factories.ms.InstrumentConfigurationBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.factories.ms.SpectrometerBuilder;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.text.tsv.MiapeTSVFile;
import org.proteored.miapeapi.text.tsv.msi.TableTextFileSeparator;
import org.proteored.miapeapi.xml.dtaselect.MiapeDTASelectFile;
import org.proteored.miapeapi.xml.ms.MiapeMSXmlFactory;
import org.proteored.miapeapi.xml.mzidentml_1_1.MiapeMzIdentMLFile;
import org.proteored.miapeapi.xml.mzml.MiapeMzMLFile;
import org.proteored.miapeapi.xml.pride.MiapeFullPrideXMLFile;
import org.proteored.miapeapi.xml.xtandem.MiapeXTandemFile;

public class FactoriesTest {

	@Test
	public void createMiapeMS(PersistenceManager databaseManager, ControlVocabularyManager cvManager) {
		// project
		ProjectBuilder projectBuilder = MiapeDocumentFactory.createProjectBuilder("my project")
				.date(new MiapeDate(new Date()));

		// User (if needed for persistence, like a database)
		UserBuilder userBuilder = MiapeDocumentFactory.createUserBuilder("myUserName", "myPassword", databaseManager);

		// Spectrometer
		SpectrometerBuilder spectrometerBuilder = (SpectrometerBuilder) MiapeMSDocumentFactory
				.createSpectrometerBuilder(SpectrometerName.LTQ_ORBITRAP_XL_NAME).manufacturer("Thermo Scientific")
				.version("version XL").catalogNumber("#12345").model("The new Orbitrap XL");

		// Analyzer
		AnalyserBuilder analyserBuilder = MiapeMSDocumentFactory.createAnalyserBuilder("orbitrap")
				.description("Description of the orbitrap");

		// Ion source
		EsiBuilder esiBuilder = MiapeMSDocumentFactory.createEsiBuilder("nano-ESI").parameters("xx Volts")
				.supplyType("regular supply");

		// Instrument Configuration (analiser + esi)
		InstrumentConfigurationBuilder instrumentConfigurationBuilder = MiapeMSDocumentFactory
				.createInstrumentConfigurationBuilder("LTQ configuration").analyser(analyserBuilder.build())
				.esi(esiBuilder.build());

		// MIAPE MS document (user + miape project + spectrometer + instrument
		// configuration
		MiapeMSDocument miapeMS = (MiapeMSDocument) MiapeMSDocumentFactory
				.createMiapeMSDocumentBuilder(projectBuilder.build(), "my first miape document", userBuilder.build())
				.instrumentConfiguration(instrumentConfigurationBuilder.build())
				.spectrometer(spectrometerBuilder.build()).cvManager(cvManager).dbManager(databaseManager) // needed
																											// for
																											// later
																											// use
																											// of
																											// .store()
				.build();

		// Save MIAPE MS to XML file
		MiapeXmlFile<MiapeMSDocument> miapeMSXML = MiapeMSXmlFactory.getFactory().toXml(miapeMS, cvManager);
		try {
			miapeMSXML.saveAs("/home/username/myFirstMiapeMS.xml");
			System.out.println("New file created at :" + miapeMSXML.getPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Store it in the database
		try {
			int identifier = miapeMS.store();
			System.out.println("Document stored in the database with identifier " + identifier);
		} catch (MiapeDatabaseException | MiapeSecurityException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void extractMIAPEInformationFromFiles(ControlVocabularyManager cvManager)
			throws MiapeDatabaseException, MiapeSecurityException {
		File prideXMLFile = new File("path_to_pride_xml");
		File xtandemXMLFile = new File("path_to_xtandem_xml");
		File mzIdentMLFile = new File("path_to_mzIdentML");
		File mzMLFile = new File("path_to_mzML");
		File tsvFile = new File("path_to_tsv_xml");
		File dtaSelectFile = new File("path_to_dtaSelect_xml");

		// PRIDE XML
		MiapeFullPrideXMLFile miapeFullPride = new MiapeFullPrideXMLFile(prideXMLFile);
		miapeFullPride.setCVManager(cvManager);
		// because all MIAPEs are under a project
		miapeFullPride.setProjectName("my project");
		MiapeMSDocument miapeMSFromPRIDEXML = miapeFullPride.toMiapeMS();
		MiapeMSIDocument miapeMSIromPRIDEXML = miapeFullPride.toMiapeMSI();
		printMiapeMS(miapeMSFromPRIDEXML);
		printMiapeMSI(miapeMSIromPRIDEXML);

		// XTANDEM XML
		MiapeXTandemFile miapeXTandemXMLFile = new MiapeXTandemFile(xtandemXMLFile);
		miapeXTandemXMLFile.setCvManager(cvManager);
		miapeXTandemXMLFile.setProjectName("my project");
		MiapeMSIDocument miapeMSIFromXTandem = miapeXTandemXMLFile.toDocument();
		printMiapeMSI(miapeMSIFromXTandem);

		// MZIDENTML
		MiapeMzIdentMLFile miapeMzIdentMLFile = new MiapeMzIdentMLFile(mzIdentMLFile);
		miapeMzIdentMLFile.setCvManager(cvManager);
		miapeMzIdentMLFile.setProjectName("my project");
		MiapeMSIDocument miapeMSIFromMzIdentML = miapeMzIdentMLFile.toDocument();
		printMiapeMSI(miapeMSIFromMzIdentML);

		// MZML
		MiapeMzMLFile miapeMzMLFile = new MiapeMzMLFile(mzMLFile);
		miapeMzMLFile.setCvManager(cvManager);
		miapeMzMLFile.setProjectName("my project");
		MiapeMSDocument miapeMSFromMzML = miapeMzMLFile.toDocument();
		printMiapeMS(miapeMSFromMzML);

		// TAB SEPARATED FILE
		MiapeTSVFile miapeTSVFile = new MiapeTSVFile(tsvFile, TableTextFileSeparator.TAB);
		miapeTSVFile.setCvManager(cvManager);
		miapeTSVFile.setProjectName("my project");
		MiapeMSIDocument miapeMSIFromTSV = miapeTSVFile.toDocument();
		printMiapeMSI(miapeMSIFromTSV);

		// DTASELECT
		MiapeDTASelectFile miapeDTASelectFile = new MiapeDTASelectFile(dtaSelectFile);
		miapeDTASelectFile.setCvManager(cvManager);
		miapeDTASelectFile.setProjectName("my project");
		MiapeMSIDocument miapeMSIFromDTASelect = miapeDTASelectFile.toDocument();
		printMiapeMSI(miapeMSIFromDTASelect);
	}

	private void printMiapeMSI(MiapeMSIDocument miapeMSI) {
		System.out.println("MIAPE MSI: ");
		System.out.println("Name: " + miapeMSI.getName());
		System.out.println("");
		System.out.println("Total number of PSMs: " + miapeMSI.getIdentifiedPeptides().size());
		for (IdentifiedProteinSet proteinSet : miapeMSI.getIdentifiedProteinSets()) {
			InputParameter ip = proteinSet.getInputParameter();
			System.out.println("Input parameters: " + ip.getName());
			System.out.println("Search engine: " + ip.getSoftware().getName());
			System.out.println(
					"Parent tolerance: " + ip.getPrecursorMassTolerance() + " " + ip.getPrecursorMassToleranceUnit());
			System.out.println(
					"Fragment tolerance: " + ip.getFragmentMassTolerance() + " " + ip.getFragmentMassToleranceUnit());
			System.out.println("Num miss-cleavages: " + ip.getMisscleavages());

			System.out.println("Number of proteins: " + proteinSet.getIdentifiedProteins().size());
			for (String proteinACC : proteinSet.getIdentifiedProteins().keySet()) {
				IdentifiedProtein protein = proteinSet.getIdentifiedProteins().get(proteinACC);
				System.out.println(
						"Protein " + proteinACC + " contains " + protein.getIdentifiedPeptides().size() + " PSMs");
				for (IdentifiedPeptide peptide : protein.getIdentifiedPeptides()) {
					System.out.println("PSM " + peptide.getSpectrumRef() + " with sequence " + peptide.getSequence()
							+ " and charge " + peptide.getCharge());
				}
			}
		}

	}

	private void printMiapeMS(MiapeMSDocument miapeMS) {
		System.out.println("MIAPE MS: ");
		System.out.println("Name: " + miapeMS.getName());
		System.out.println("");
		for (InstrumentConfiguration ic : miapeMS.getInstrumentConfigurations()) {
			for (Analyser analyser : ic.getAnalyzers()) {
				System.out.println("Analyser: " + analyser.getName());
				System.out.println("Description: " + analyser.getDescription());
			}
			for (Esi esi : ic.getEsis()) {
				System.out.println("ESI: " + esi.getName());
				System.out.println("Parameters: " + esi.getParameters());
				System.out.println("Supply type: " + esi.getSupplyType());
			}
			for (ActivationDissociation ad : ic.getActivationDissociations()) {
				System.out.println("Name: " + ad.getName());
				System.out.println("Activation type: " + ad.getActivationType());
				System.out.println("Gas type: " + ad.getGasType());
				System.out.println("Gas pressure: " + ad.getGasPressure() + " " + ad.getPressureUnit());
			}
		}

	}
}
