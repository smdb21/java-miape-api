package org.proteored.miapeapi.xml.gi.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.xml.gi.autogenerated.GIExtractionInputImage;
import org.proteored.miapeapi.xml.gi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class ExtractionInputImageAdapter implements Adapter<GIExtractionInputImage> {
	private final ImageGelInformatics image;
	private final String imageURI;
	private final ObjectFactory factory;

	public ExtractionInputImageAdapter(ImageGelInformatics image, ObjectFactory factory) {
		this.image = image;
		this.factory = factory;
		this.imageURI = null;
	}

	public ExtractionInputImageAdapter(String imageURI, ObjectFactory factory2) {
		this.image = null;
		this.factory = factory2;
		this.imageURI = imageURI;
	}

	@Override
	public GIExtractionInputImage adapt() {
		GIExtractionInputImage inputImageXML = factory.createGIExtractionInputImage();
		if (image != null)
			inputImageXML.setImageRef(MiapeXmlUtil.IdentifierPrefixes.IMAGE.getPrefix() + image.getId());
		if (imageURI != null)
			inputImageXML.setImageURI(imageURI);

		return inputImageXML;
	}

}
