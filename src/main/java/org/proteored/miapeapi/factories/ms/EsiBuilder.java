package org.proteored.miapeapi.factories.ms;

import java.util.HashSet;
import java.util.Set;

import org.proteored.miapeapi.cv.ms.EsiName;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ms.Esi;

public class EsiBuilder {

	final String name;
	String parameters;
	String supplyType;

	Set<Equipment> interfaces;
	Set<Equipment> sprayers;

	/**
	 * Set the ESI name. It should be one of the possible values from
	 * {@link EsiName}
	 **/
	EsiBuilder(String name) {
		this.name = name;
	}

	public EsiBuilder parameters(String value) {
		this.parameters = value;
		return this;
	}

	public EsiBuilder supplyType(String value) {
		this.supplyType = value;
		return this;
	}

	public EsiBuilder interfaces(Set<Equipment> value) {
		this.interfaces = value;
		return this;
	}

	public EsiBuilder interfaceEquipment(Equipment value) {
		if (this.interfaces == null)
			this.interfaces = new HashSet<Equipment>();
		this.interfaces.add(value);
		return this;
	}

	public EsiBuilder sprayers(Set<Equipment> value) {
		this.sprayers = value;
		return this;
	}

	public EsiBuilder sprayer(Equipment value) {
		if (this.sprayers == null)
			this.sprayers = new HashSet<Equipment>();
		this.sprayers.add(value);
		return this;
	}

	public Esi build() {
		return new EsiImpl(this);
	}
}