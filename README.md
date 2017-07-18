# Java MIAPE API
The Java MIAPE API is an open source Java API designed for the extraction and management of MIAPE information from commonly used proteomics data files.

What is the *MIAPE information*?
 - MIAPE stands for the **M**inimal **I**nformation **A**bout a **P**roteomics **E**xperiment, and it is a set of guidelines defined by the Human Proteome Organization's [Proteomics Standard Initiative (HUPO-PSI)](http://psidev.info)  <sup>[(1)](https://www.ncbi.nlm.nih.gov/pubmed/24136562)</sup>

The Java MIAPE API is designed in 4 different modules:
 - The *model module*: This module contains the classes needed to represent the MIAPE information of the different types of experiments. The interfaces (under package *org.proteored.miapeapi.interfaces*) MiapeGEDocument, MiapeGIDocument, MiapeMSDocumen and MiapeMSIDocument, define the MIAPE information for the Gel Electrophoresis<sup>[(2)](https://www.ncbi.nlm.nih.gov/pubmed/18688234)</sup>, Gel Image Informatics<sup>[(3)](https://www.ncbi.nlm.nih.gov/pubmed/20622830)</sup>, Mass Spectrometry<sup>[(4)](https://www.ncbi.nlm.nih.gov/pubmed/18688232)</sup> and Mass Spectrometry Informatics<sup>[(5)](https://www.ncbi.nlm.nih.gov/pubmed/18688233)</sup> MIAPE modules. 
 - The *factory module*: This module provides the util classes for the creation of the MIAPE document objects.
 - The *XML module*: This module provides the methods for the extraction of the MIAPE information from commonly used proteomics data files (most of them XML), such as: mzIdentML, mzML, pepXML, PRIDE XML or XTandem XML.
 - The *persistence model*: This module provides the methods to be implemented by a persistence system, which will be able to persist the MIAPE information, on files, a database, etc...
 
 **Example**:
 
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
 

```css #button { border: none; } ```

~~~~ This is a piece of code in a block ~~~~

``` This too ```
---
(1) [The Minimal Information about a Proteomics Experiment (MIAPE) from the Proteomics Standards Initiative. 
Mart?nez-Bartolom? S, Binz PA, Albar JP. 
Methods Mol Biol. 2014;1072:765-80. doi: 10.1007/978-1-62703-631-3_53.](https://www.ncbi.nlm.nih.gov/pubmed/24136562).

(2) [Guidelines for reporting the use of gel electrophoresis in proteomics.
Gibson F, Anderson L, Babnigg G, Baker M, Berth M, Binz PA, Borthwick A, Cash P, Day BW, Friedman DB, Garland D, Gutstein HB, Hoogland C, Jones NA, Khan A, Klose J, Lamond AI, Lemkin PF, Lilley KS, Minden J, Morris NJ, Paton NW, Pisano MR, Prime JE, Rabilloud T, Stead DA, Taylor CF, Voshol H, Wipat A, Jones AR.
Nat Biotechnol. 2008 Aug;26(8):863-4. doi: 10.1038/nbt0808-863.](https://www.ncbi.nlm.nih.gov/pubmed/18688234)

(3) [Guidelines for reporting the use of gel image informatics in proteomics. 
Hoogland C, O'Gorman M, Bogard P, Gibson F, Berth M, Cockell SJ, Ekefj?rd A, Forsstrom-Olsson O, Kapferer A, Nilsson M, Mart?nez-Bartolom? S, Albar JP, Echevarr?a-Zome?o S, Mart?nez-Gomariz M, Joets J, Binz PA, Taylor CF, Dowsey A, Jones AR.
Nat Biotechnol. 2010 Jul;28(7):655-6. doi: 10.1038/nbt0710-655.](https://www.ncbi.nlm.nih.gov/pubmed/20622830)

(4) [Guidelines for reporting the use of mass spectrometry in proteomics.
Taylor CF, Binz PA, Aebersold R, Affolter M, Barkovich R, Deutsch EW, Horn DM, H?hmer A, Kussmann M, Lilley K, Macht M, Mann M, M?ller D, Neubert TA, Nickson J, Patterson SD, Raso R, Resing K, Seymour SL, Tsugita A, Xenarios I, Zeng R, Julian RK Jr.
Nat Biotechnol. 2008 Aug;26(8):860-1. doi: 10.1038/nbt0808-860.](https://www.ncbi.nlm.nih.gov/pubmed/18688232)

(5) [Guidelines for reporting the use of mass spectrometry informatics in proteomics.
Binz PA, Barkovich R, Beavis RC, Creasy D, Horn DM, Julian RK Jr, Seymour SL, Taylor CF, Vandenbrouck Y.
Nat Biotechnol. 2008 Aug;26(8):862. doi: 10.1038/nbt0808-862.](https://www.ncbi.nlm.nih.gov/pubmed/18688233)