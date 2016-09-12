package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.ImageImpl;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;

public class ImageGelInformaticsImpl extends ImageImpl implements ImageGelInformatics {
	private final String type;

	public ImageGelInformaticsImpl(ImageGelInformaticsBuilder imageBuilder) {
		super(imageBuilder);
		this.type = imageBuilder.type;

	}

	@Override
	public String getType() {
		return type;
	}


}
