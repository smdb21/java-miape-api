package org.proteored.miapeapi.factories.ms;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ms.Esi;

public class EsiImpl implements Esi {

	private final String name;
	private final String parameters;
	private final String supplyType;

	private final Set<Equipment> interfaces;
	private final Set<Equipment> sprayers;

	@SuppressWarnings("unused")
	private EsiImpl() {
		this(null);
	}

	public EsiImpl(EsiBuilder esiBuilder) {
		this.name = esiBuilder.name;
		this.parameters = esiBuilder.parameters;
		this.supplyType = esiBuilder.supplyType;
		this.interfaces = esiBuilder.interfaces;
		this.sprayers = esiBuilder.sprayers;
	}

	@Override
	public Set<Equipment> getInterfaces() {
		return interfaces;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameters() {
		return parameters;
	}

	@Override
	public Set<Equipment> getSprayers() {
		return sprayers;
	}

	@Override
	public String getSupplyType() {
		return supplyType;
	}

}
