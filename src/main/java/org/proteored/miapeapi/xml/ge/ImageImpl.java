package org.proteored.miapeapi.xml.ge;

import java.util.Map;

import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;
import org.proteored.miapeapi.xml.ge.autogenerated.GEGelMatrix;
import org.proteored.miapeapi.xml.ge.autogenerated.GEImage;
import org.proteored.miapeapi.xml.ge.autogenerated.GESample;
import org.proteored.miapeapi.xml.ge.util.GEControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class ImageImpl implements ImageGelElectrophoresis {
	private final GEImage imageXML;

	private final Map<String, GESample> sampleMap;
	private final Map<String, GEGelMatrix> gelMatrixMap;

	public ImageImpl(GEImage geImage, Map<String, GEGelMatrix> gelMatrixMap, Map<String, GESample> sampleMap) {
		this.imageXML = geImage;

		this.gelMatrixMap = gelMatrixMap;
		this.sampleMap = sampleMap;
	}
	@Override
	public GelMatrix getReferencedGelMatrix() {
		if (gelMatrixMap.containsKey(imageXML.getGelMatrixRef())) {
			return new GelMatrixImpl(gelMatrixMap.get(imageXML.getGelMatrixRef()), this.sampleMap);
		}
		return null;
	}

	@Override
	public String getBitDepth() {
		return imageXML.getBitDepth();
	}

	@Override
	public String getDimensionUnit() {
		return GEControlVocabularyXmlFactory.getUnitName(imageXML.getDimensionX());
	}

	@Override
	public String getDimensionX() {
		return GEControlVocabularyXmlFactory.getValue(imageXML.getDimensionX());
	}

	@Override
	public String getDimensionY() {
		return GEControlVocabularyXmlFactory.getValue(imageXML.getDimensionX());
	}

	@Override
	public String getFormat() {
		return GEControlVocabularyXmlFactory.getName(imageXML.getFormat());
	}

	@Override
	public String getLocation() {
		return imageXML.getLocation();
	}

	@Override
	public String getName() {
		return imageXML.getName();
	}

	@Override
	public String getOrientation() {
		return imageXML.getOrientation();
	}

	@Override
	public String getResolution() {
		return GEControlVocabularyXmlFactory.getValue(imageXML.getResolution());
	}

	@Override
	public String getResolutionUnit() {
		return GEControlVocabularyXmlFactory.getUnitName(imageXML.getResolution());
	}
	@Override
	public int getId() {
		return MiapeXmlUtil.getIdFromXMLId(imageXML.getId());
	}

}
