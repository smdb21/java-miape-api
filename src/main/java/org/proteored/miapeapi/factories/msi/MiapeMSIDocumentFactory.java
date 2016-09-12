package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.DatabaseName;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeMSIDocumentFactory {
	private MiapeMSIDocumentFactory() {
	}

	public static MiapeMSIDocumentBuilder createMiapeDocumentMSIBuilder(Project project,
			String name, User user) {
		return new MiapeMSIDocumentBuilder(project, name, user);
	}

	public static MiapeMSIDocumentBuilder createMiapeDocumentMSIBuilder(Project project,
			String name, User user, PersistenceManager db) {
		return new MiapeMSIDocumentBuilder(project, name, user, db);
	}

	public static MiapeMSIDocumentBuilder createMiapeDocumentMSIBuilder(Project project,
			String name, User user, PersistenceManager db, XmlManager xmlManager,
			ControlVocabularyManager cvUtil) {
		return new MiapeMSIDocumentBuilder(project, name, user, db, xmlManager, cvUtil);
	}

	public static InputDataBuilder createInputDataBuilder(String name) {
		return new InputDataBuilder(name);
	}

	public static InputDataSetBuilder createInputDataSetBuilder(String name) {
		return new InputDataSetBuilder(name);
	}

	public static InputParameterBuilder createInputParameterBuilder(String name) {
		return new InputParameterBuilder(name);
	}

	public static ValidationBuilder createValidationBuilder(String name) {
		return new ValidationBuilder(name);
	}

	public static IdentifiedProteinSetBuilder createIdentifiedProteinSetBuilder(String name) {
		return new IdentifiedProteinSetBuilder(name);
	}

	/**
	 * Set the additional parameter name. It should be one of the possible values from
	 * {@link AdditionalInformationName}
	 **/
	public static AdditionalParameterBuilder createAdditionalParameterBuilder(String name) {
		return new AdditionalParameterBuilder(name);
	}

	public static IdentifiedProteinBuilder createIdentifiedProteinBuilder(String accession) {
		return new IdentifiedProteinBuilder(accession);
	}

	public static IdentifiedPeptideBuilder createIdentifiedPeptideBuilder(String sequence) {
		return new IdentifiedPeptideBuilder(sequence);
	}

	/**
	 * Sets a string value to the name of the database. It should be a possible value of the
	 * {@link DatabaseName}
	 * 
	 * @param databaseName
	 */
	public static DatabaseBuilder createDatabaseBuilder(String databaseName) {
		return new DatabaseBuilder(databaseName);
	}

	/**
	 * Set the peptide modification name. It should be one of the possible values from
	 * {@link PeptideModificationName}
	 */
	public static PeptideModificationBuilder createPeptideModificationBuilder(String name) {
		return new PeptideModificationBuilder(name);
	}

	/**
	 * Set the additional information name. It should be one of the possible values from
	 * {@link AdditionalInformationName}
	 * 
	 */
	public static AdditionalInformationBuilder createAdditionalInformationBuilder(String addinfoname) {
		return new AdditionalInformationBuilder(addinfoname);
	}

	/**
	 * Set the peptide score name and value. The name should be one of the possible values from
	 * {@link Score}
	 */
	public static PeptideScoreBuilder createPeptideScoreBuilder(String scoreName, String scoreValue) {
		return new PeptideScoreBuilder(scoreName, scoreValue);
	}

	/**
	 * Set the protein score name and value. The name should be one of the possible values from
	 * {@link Score}
	 */
	public static ProteinScoreBuilder createProteinScoreBuilder(String scoreName, String scoreValue) {
		return new ProteinScoreBuilder(scoreName, scoreValue);
	}

}
