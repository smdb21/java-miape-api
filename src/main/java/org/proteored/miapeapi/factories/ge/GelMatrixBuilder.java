package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.GelMatrixName;
import org.proteored.miapeapi.cv.ge.MatrixDimensionUnit;
import org.proteored.miapeapi.cv.ge.MwRangeType;
import org.proteored.miapeapi.cv.ge.MwRangeUnit;
import org.proteored.miapeapi.cv.ge.PhRangeType;
import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.SampleApplication;

public class GelMatrixBuilder {
	final String name;
	String type;
	String composed;
	String manufacture;
	Double dimensionsX;
	Double dimensionsY;
	Double dimensionsZ;
	String dimensionsUnit;
	String phRangeL;
	String phRangeH;
	String phRangeType;
	String mwRangeL;
	String mwRangeH;
	String mwRangeUnit;
	String mwRangeType;
	String acrylamideConcentration;
	String acrylamideManufacturer;
	String acry;
	String bisacry;
	Set<Buffer> polymerizationMatrixBuffers;
	Set<Agent> additionalMatrixSubstances;
	Set<Buffer> additionalMatrixBuffers;
	Set<SampleApplication> sampleApplications;

	/**
	 * Sets the gel matrix name. It should be one of the possible values of
	 * {@link GelMatrixName}
	 */
	GelMatrixBuilder(String name) {
		this.name = name;
	}

	public GelMatrixBuilder type(String value) {
		type = value;
		return this;
	}

	public GelMatrixBuilder composed(String value) {
		composed = value;
		return this;
	}

	public GelMatrixBuilder manufacture(String value) {
		manufacture = value;
		return this;
	}

	public GelMatrixBuilder dimensionsX(Double value) {
		dimensionsX = value;
		return this;
	}

	public GelMatrixBuilder dimensionsY(Double value) {
		dimensionsY = value;
		return this;
	}

	public GelMatrixBuilder dimensionsZ(Double value) {
		dimensionsZ = value;
		return this;
	}

	/**
	 * Sets the matrix dimension unit. It should be one of the possible values
	 * of {@link MatrixDimensionUnit}
	 */
	public GelMatrixBuilder dimensionsUnit(String value) {
		dimensionsUnit = value;
		return this;
	}

	public GelMatrixBuilder phRangeL(String value) {
		phRangeL = value;
		return this;
	}

	public GelMatrixBuilder phRangeH(String value) {
		phRangeH = value;
		return this;
	}

	/**
	 * 
	 * Sets the PH range type. It should be one of the possible values of
	 * {@link PhRangeType}
	 */

	public GelMatrixBuilder phRangeType(String value) {
		phRangeType = value;
		return this;
	}

	public GelMatrixBuilder mwRangeL(String value) {
		mwRangeL = value;
		return this;
	}

	public GelMatrixBuilder mwRangeH(String value) {
		mwRangeH = value;
		return this;
	}

	/**
	 * 
	 * Sets the molecular weigth range unit. It should be one of the possible
	 * values from {@link MwRangeUnit}
	 */
	public GelMatrixBuilder mwRangeUnit(String value) {
		mwRangeUnit = value;
		return this;
	}

	/**
	 * 
	 * Set the molecular weigth range type. It should be one of the possible
	 * values from {@link MwRangeType}
	 */
	public GelMatrixBuilder mwRangeType(String value) {
		mwRangeType = value;
		return this;
	}

	public GelMatrixBuilder acrylamideConcentration(String value) {
		acrylamideConcentration = value;
		return this;
	}

	public GelMatrixBuilder acrylamideManufacturer(String value) {
		acrylamideManufacturer = value;
		return this;
	}

	public GelMatrixBuilder acry(String value) {
		acry = value;
		return this;
	}

	public GelMatrixBuilder bisacry(String value) {
		bisacry = value;
		return this;
	}

	public GelMatrixBuilder polymerizationMatrixBuffers(Set<Buffer> value) {
		polymerizationMatrixBuffers = value;
		return this;
	}

	public GelMatrixBuilder additionalMatrixSubstances(Set<Agent> value) {
		additionalMatrixSubstances = value;
		return this;
	}

	public GelMatrixBuilder additionalMatrixBuffers(Set<Buffer> value) {
		additionalMatrixBuffers = value;
		return this;
	}

	public GelMatrixBuilder sampleApplications(Set<SampleApplication> value) {
		sampleApplications = value;
		return this;
	}

	public GelMatrix build() {
		return new GelMatrixImpl(this);
	}
}