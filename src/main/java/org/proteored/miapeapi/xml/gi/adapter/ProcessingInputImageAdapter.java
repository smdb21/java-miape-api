package org.proteored.miapeapi.xml.gi.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.xml.gi.autogenerated.GIProcessingInputImage;
import org.proteored.miapeapi.xml.gi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class ProcessingInputImageAdapter implements Adapter<GIProcessingInputImage> {
	private final ImageGelInformatics image;
	private final ObjectFactory factory;

	public ProcessingInputImageAdapter(ImageGelInformatics inputImage, ObjectFactory factory) {
		this.factory = factory;
		this.image = inputImage;
	}
	@Override
	public GIProcessingInputImage adapt() {
		GIProcessingInputImage xmlInputImage = factory.createGIProcessingInputImage();
		if (image !=null)
			xmlInputImage.setImageRef(MiapeXmlUtil.IdentifierPrefixes.IMAGE.getPrefix() + image.getId());

		return xmlInputImage;
	}

}
