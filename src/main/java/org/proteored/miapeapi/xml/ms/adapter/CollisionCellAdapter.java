package org.proteored.miapeapi.xml.ms.adapter;

import org.proteored.miapeapi.cv.ms.DissociationMethod;
import org.proteored.miapeapi.cv.ms.GasType;
import org.proteored.miapeapi.cv.ms.PressureUnit;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.xml.ms.autogenerated.MSActivationDissociation;
import org.proteored.miapeapi.xml.ms.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ms.util.MsControlVocabularyXmlFactory;

public class CollisionCellAdapter implements Adapter<MSActivationDissociation> {
	private final ActivationDissociation collisionCell;
	private final ObjectFactory factory;
	private final MsControlVocabularyXmlFactory cvFactory;

	public CollisionCellAdapter(ActivationDissociation collisionCell, ObjectFactory factory,
			MsControlVocabularyXmlFactory cvFactory) {
		this.collisionCell = collisionCell;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public MSActivationDissociation adapt() {
		MSActivationDissociation xmlCollisionCell = factory.createMSActivationDissociation();
		xmlCollisionCell.setGas(cvFactory.createCV(collisionCell.getGasType(), null,
				GasType.getInstance(cvFactory.getCvManager())));
		xmlCollisionCell.setName(collisionCell.getName());
		xmlCollisionCell
				.setPressure(cvFactory.createParamUnit(collisionCell.getGasPressure(),
						collisionCell.getPressureUnit(),
						PressureUnit.getInstance(cvFactory.getCvManager())));

		xmlCollisionCell.setActivationType(cvFactory.createCV(collisionCell.getActivationType(), null,
				DissociationMethod.getInstance(cvFactory.getCvManager())));
		return xmlCollisionCell;
	}

}
