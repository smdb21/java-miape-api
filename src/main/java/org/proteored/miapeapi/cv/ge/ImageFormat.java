package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageFormat extends ControlVocabularySet {
	private static ImageFormat instance;

	/*
	 * GRAPHICS_INTERCHANGE_FORMAT("sep:00049", "graphics interchange format"),
	 * JOINT_PHOTOGRAPHIC_EXPERTS_GROUP("sep:00050",
	 * "Joint Photographic Experts Group"),
	 * PORTABLE_NETWORK_GRAPHICS("sep:00051", "Portable Network Graphics"),
	 * SCALABLE_VECTOR_GRAPHICS("sep:00052", "Scalable Vector Graphics"),
	 * TAGGED_IMAGE_FILE_FORMAT("sep:00053", "Tagged Image File Format"),
	 * WINDOWS_BITMAP("sep:00054", "Windows Bitmap"),
	 * IMAGE_FILE_FORMAT("sep:00048", "image file format");
	 */
	public static ImageFormat getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ImageFormat(cvManager);
		return instance;
	}

	private ImageFormat(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00048" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 22;

	}
}
