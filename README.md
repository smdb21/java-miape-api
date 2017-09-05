## Java MIAPE API
The **Java MIAPE API** is an open source Java API designed for the extraction and management of MIAPE information from commonly used proteomics data files.

What is the *MIAPE information*?
 - MIAPE stands for the **M**inimal **I**nformation **A**bout a **P**roteomics **E**xperiment, and it is a set of guidelines defined by the Human Proteome Organization's [Proteomics Standard Initiative (HUPO-PSI)](http://psidev.info)  <sup>[(1)](https://www.ncbi.nlm.nih.gov/pubmed/24136562)</sup>

The Java MIAPE API is designed in 4 different modules:
 - The *model module*: This module contains the classes needed to represent the MIAPE information of the different types of experiments. The interfaces (under package *org.proteored.miapeapi.interfaces*) MiapeGEDocument, MiapeGIDocument, MiapeMSDocumen and MiapeMSIDocument, define the MIAPE information for the Gel Electrophoresis<sup>[(2)](https://www.ncbi.nlm.nih.gov/pubmed/18688234)</sup>, Gel Image Informatics<sup>[(3)](https://www.ncbi.nlm.nih.gov/pubmed/20622830)</sup>, Mass Spectrometry<sup>[(4)](https://www.ncbi.nlm.nih.gov/pubmed/18688232)</sup> and Mass Spectrometry Informatics<sup>[(5)](https://www.ncbi.nlm.nih.gov/pubmed/18688233)</sup> MIAPE modules. 
 - The *factory module*: This module provides the util classes for the creation of the MIAPE document objects.
 - The *XML module*: This module provides the methods for the extraction of the MIAPE information from commonly used proteomics data files (most of them XML), such as: **mzIdentML, mzML, pepXML, PRIDE XML, DtaSelect txt** or **XTandem XML**.
 - The *persistence model*: This module provides the methods to be implemented by a persistence system, which will be able to persist the MIAPE information, on files, a database, etc...
 
 ### About developers
 This API was firstly designed by Emilio Salazar Do?ate and [Salvador Martinez-Bartolome](https://www.ncbi.nlm.nih.gov/pubmed/?term=Martinez-Bartolome+S) members of the [ProteoRed](http://www.proteored.org) Bioinformatics Working Group, under the supervision of Juan Pablo Albar, at the [Proteomics Laboratory](http://proteo.cnb.csic.es/proteomica/) of the  [National Center for Biotechnology (CNB-CSIC)](http://www.cnb.csic.es) in Madrid, Spain. Later, the project was continued by Salvador Martinez-Bartolome under the supervision of John R. Yates III at the [John Yates laboratory](http://www.scripps.edu/yates) at [The Scripps Research Institute](http://www.scripps.edu), La Jolla, California, USA.
 
 ### How to get the API
 
 **Latest build** available at: [http://sealion.scripps.edu:8080/hudson/job/java-miape-api/lastSuccessfulBuild/artifact/](http://sealion.scripps.edu:8080/hudson/job/java-miape-api/lastSuccessfulBuild/artifact/)
 
 **Using MAVEN**:
 Add this to your pom.xml: 
 ```xml
<repository>
    <id>internal</id>
    <name>John Yates lab Internal Repository</name>
    <url>http://sealion.scripps.edu/archiva/repository/internal/</url>
</repository>
<repository>
    <id>snapshots</id>
    <name>John Yates lab snapshots maven repository</name>
    <url>http://sealion.scripps.edu/archiva/repository/snapshots/</url>
</repository>
 ```
 Including this dependency:
 ```xml 
<dependency>
    <groupId>org.proteored.miape.api</groupId>
    <artifactId>miape-api</artifactId>
    <version>1.9.2-SNAPSHOT</version> <!-- or the latest version available -->
</dependency>
```

 ### How to use the API
 
 #### Example 1:
 This example shows how a MIAPE MS document object (*object module*) is created using the *factory module* and then is exported to a XML file (*XML module*) and stored in a database (*persistence module*): 
```java
public class createMiape(PersistenceManager databaseManager, ControlVocabularyManager cvManager) {
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
		.spectrometer(spectrometerBuilder.build()).cvManager(cvManager)  
		.dbManager(databaseManager) // needed for later use of .store()
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
```

 #### Example 2: 
This example shows how to extract the MIAPE information from commonly used proteomics data files:
```java
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
```
 #### Example 3:
This example show the *printMiapeMS* and *printMiapeMSI* methods called in the previous example, showing how the information contained in each document can be extracted and printed
```java
private void printMiapeMSI(MiapeMSIDocument miapeMSI) {
	System.out.println("MIAPE MSI: ");
	System.out.println("Name: " + miapeMSI.getName());
	System.out.println("");
	System.out.println("Total number of PSMs: " + miapeMSI.getIdentifiedPeptides().size());
	for (IdentifiedProteinSet proteinSet : miapeMSI.getIdentifiedProteinSets()) {
		InputParameter ip = proteinSet.getInputParameter();
		System.out.println("Input parameters: " + ip.getName());
		System.out.println("Search engine: " + ip.getSoftware().getName());
		System.out.println("Parent tolerance: " + ip.getPrecursorMassTolerance() + " " + ip.getPrecursorMassToleranceUnit());
		System.out.println("Fragment tolerance: " + ip.getFragmentMassTolerance() + " " + ip.getFragmentMassToleranceUnit());
		System.out.println("Num miss-cleavages: " + ip.getMisscleavages());

		System.out.println("Number of proteins: " + proteinSet.getIdentifiedProteins().size());
		for (String proteinACC : proteinSet.getIdentifiedProteins().keySet()) {
			IdentifiedProtein protein = proteinSet.getIdentifiedProteins().get(proteinACC);
			System.out.println("Protein " + proteinACC + " contains " + protein.getIdentifiedPeptides().size() + " PSMs");
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
```

---
(1) [The Minimal Information about a Proteomics Experiment (MIAPE) from the Proteomics Standards Initiative. 
Martinez-Bartolome S, Binz PA, Albar JP. 
Methods Mol Biol. 2014;1072:765-80. doi: 10.1007/978-1-62703-631-3_53.](https://www.ncbi.nlm.nih.gov/pubmed/24136562).

(2) [Guidelines for reporting the use of gel electrophoresis in proteomics.
Gibson F, Anderson L, Babnigg G, Baker M, Berth M, Binz PA, Borthwick A, Cash P, Day BW, Friedman DB, Garland D, Gutstein HB, Hoogland C, Jones NA, Khan A, Klose J, Lamond AI, Lemkin PF, Lilley KS, Minden J, Morris NJ, Paton NW, Pisano MR, Prime JE, Rabilloud T, Stead DA, Taylor CF, Voshol H, Wipat A, Jones AR.
Nat Biotechnol. 2008 Aug;26(8):863-4. doi: 10.1038/nbt0808-863.](https://www.ncbi.nlm.nih.gov/pubmed/18688234)

(3) [Guidelines for reporting the use of gel image informatics in proteomics. 
Hoogland C, O'Gorman M, Bogard P, Gibson F, Berth M, Cockell SJ, Ekefj?rd A, Forsstrom-Olsson O, Kapferer A, Nilsson M, Martinez-Bartolome S, Albar JP, Echevarr?a-Zome?o S, Martinez-Gomariz M, Joets J, Binz PA, Taylor CF, Dowsey A, Jones AR.
Nat Biotechnol. 2010 Jul;28(7):655-6. doi: 10.1038/nbt0710-655.](https://www.ncbi.nlm.nih.gov/pubmed/20622830)

(4) [Guidelines for reporting the use of mass spectrometry in proteomics.
Taylor CF, Binz PA, Aebersold R, Affolter M, Barkovich R, Deutsch EW, Horn DM, H?hmer A, Kussmann M, Lilley K, Macht M, Mann M, M?ller D, Neubert TA, Nickson J, Patterson SD, Raso R, Resing K, Seymour SL, Tsugita A, Xenarios I, Zeng R, Julian RK Jr.
Nat Biotechnol. 2008 Aug;26(8):860-1. doi: 10.1038/nbt0808-860.](https://www.ncbi.nlm.nih.gov/pubmed/18688232)

(5) [Guidelines for reporting the use of mass spectrometry informatics in proteomics.
Binz PA, Barkovich R, Beavis RC, Creasy D, Horn DM, Julian RK Jr, Seymour SL, Taylor CF, Vandenbrouck Y.
Nat Biotechnol. 2008 Aug;26(8):862. doi: 10.1038/nbt0808-862.](https://www.ncbi.nlm.nih.gov/pubmed/18688233)
