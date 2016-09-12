package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public class MiapeDocumentFactory {

	private MiapeDocumentFactory() {
	};

	public static ProjectBuilder createProjectBuilder(String name) {
		return new ProjectBuilder(name);
	}

	public static GEContactBuilder createGEContactBuilder(String name, String lastName, String email) {
		return new GEContactBuilder(name, lastName, email);
	}

	public static MSContactBuilder createMSContactBuilder(String name, String lastName, String email) {
		return new MSContactBuilder(name, lastName, email);
	}

	public static EquipmentBuilder createEquipmentBuilder(String name) {
		return new EquipmentBuilder(name);
	}

	public static AlgorithmBuilder createAlgorithmBuilder(String name) {
		return new AlgorithmBuilder(name);
	}

	public static UserBuilder createUserBuilder(String userName, String password,
			PersistenceManager databaseManager) {
		return new UserBuilder(userName, password, databaseManager);
	}

	public static SoftwareBuilder createSoftwareBuilder(String name) {
		return new SoftwareBuilder(name);
	}

	public static SubstanceBuilder createSubstanceBuilder(String name) {
		return new SubstanceBuilder(name);
	}
}
