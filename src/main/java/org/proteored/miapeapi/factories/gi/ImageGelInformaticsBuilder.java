package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.ImageBuilder;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;

public class ImageGelInformaticsBuilder extends ImageBuilder {
	String type;

	ImageGelInformaticsBuilder(String name) {
		super(name);

	}

	public ImageGelInformaticsBuilder type(String value) {
		type = value;
		return this;
	}

	@Override
	public ImageGelInformatics build() {
		return new ImageGelInformaticsImpl(this);
	}
}