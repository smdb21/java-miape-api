package org.proteored.miapeapi.xml.gi;

import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.DataExtraction;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.Matching;
import org.proteored.miapeapi.xml.gi.autogenerated.GIDataExtraction;
import org.proteored.miapeapi.xml.gi.autogenerated.GIExtractionInputImage;
import org.proteored.miapeapi.xml.gi.autogenerated.GIFeatureDetectionType;
import org.proteored.miapeapi.xml.gi.autogenerated.GIFeatureQuantitation;
import org.proteored.miapeapi.xml.gi.autogenerated.GIImage;
import org.proteored.miapeapi.xml.gi.autogenerated.GIMatchingType;

import gnu.trove.set.hash.THashSet;

public class DataExtractionImpl implements DataExtraction {
	private final GIDataExtraction dataExtractionXML;
	private final Map<String, GIImage> imageMap;

	public DataExtractionImpl(GIDataExtraction xmlDataExtraction, Map<String, GIImage> imageMap) {
		this.dataExtractionXML = xmlDataExtraction;
		this.imageMap = imageMap;

	}

	@Override
	public Set<FeatureDetection> getFeatureDetections() {
		Set<FeatureDetection> featureDetections = new THashSet<FeatureDetection>();
		if (dataExtractionXML.getGIFeatureDetection() != null) {
			for (GIFeatureDetectionType featureDetectionXML : dataExtractionXML.getGIFeatureDetection()) {
				featureDetections.add(new FeatureDetectionImpl(featureDetectionXML));
			}
		}

		return featureDetections;
	}

	@Override
	public Set<FeatureQuantitation> getFeatureQuantitations() {
		Set<FeatureQuantitation> featureQuantitations = new THashSet<FeatureQuantitation>();
		if (dataExtractionXML.getGIFeatureQuantitation() != null) {
			for (GIFeatureQuantitation featureQuantitatioXML : dataExtractionXML.getGIFeatureQuantitation()) {
				featureQuantitations.add(new FeatureQuantitationImpl(featureQuantitatioXML));
			}
		}
		return featureQuantitations;
	}

	@Override
	public Set<Matching> getMatchings() {
		Set<Matching> matching = new THashSet<Matching>();
		if (dataExtractionXML.getGIMatching() != null) {
			for (GIMatchingType matchingXML : dataExtractionXML.getGIMatching()) {
				matching.add(new MatchingImpl(matchingXML, imageMap));
			}
		}
		return matching;
	}

	@Override
	public String getName() {
		return dataExtractionXML.getName();
	}

	@Override
	public Set<String> getInputImageUris() {
		Set<String> inputImageUris = new THashSet<String>();
		if (dataExtractionXML.getGIExtractionInputImage() != null) {
			for (GIExtractionInputImage inputImageXML : dataExtractionXML.getGIExtractionInputImage()) {
				if (inputImageXML.getImageURI() != null)
					inputImageUris.add(inputImageXML.getImageURI());
			}
		}
		return inputImageUris;
	}

	@Override
	public Set<ImageGelInformatics> getInputImages() {
		Set<ImageGelInformatics> inputImages = new THashSet<ImageGelInformatics>();
		if (dataExtractionXML.getGIExtractionInputImage() != null) {
			for (GIExtractionInputImage inputImageXML : dataExtractionXML.getGIExtractionInputImage()) {
				if (imageMap.get(inputImageXML) != null)
					inputImages.add(new ImageImpl(imageMap.get(inputImageXML)));
			}
		}
		return inputImages;
	}

}
