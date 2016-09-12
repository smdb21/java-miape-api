package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.GelMatrixName;
import org.proteored.miapeapi.cv.ge.MatrixDimensionUnit;
import org.proteored.miapeapi.cv.ge.MwRangeType;
import org.proteored.miapeapi.cv.ge.MwRangeUnit;
import org.proteored.miapeapi.cv.ge.PhRangeType;
import org.proteored.miapeapi.interfaces.Buffer;

public interface GelMatrix {
	/**
	 * Gets the identifier that can be referenced from ImageAcquisition and from
	 * Image
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Gets the name of the gel matrix. It should be one of the possible values
	 * of {@link GelMatrixName}
	 * 
	 * @return the name
	 */
	public String getName();

	public String getType();

	public String getComposed();

	public String getGelManufacture();

	public Double getDimensionsX();

	public Double getDimensionsY();

	public Double getDimensionsZ();

	/**
	 * Gets the dimentions of the gel matrix. It should be one of the possible
	 * values of {@link MatrixDimensionUnit}
	 * 
	 * @return the name
	 */
	public String getDimensionsUnit();

	public String getPhRangeL();

	public String getPhRangeH();

	/**
	 * Gets the dimentions of the gel matrix. It should be one of the possible
	 * values of {@link PhRangeType}
	 * 
	 * @return the name
	 */
	public String getPhRangeType();

	public String getMwRangeL();

	public String getMwRangeH();

	/**
	 * Gets the molecular weigth range unit. It should be one of the possible
	 * values from {@link MwRangeUnit}
	 * 
	 * @return the range unit
	 */
	public String getMwRangeUnit();

	/**
	 * Gets the molecular weigth range type. It should be one of the possible
	 * values from {@link MwRangeType}
	 * 
	 * @return the range type
	 */
	public String getMwRangeType();

	public String getAcrylamideConcentration();

	public String getAcry();

	public String getBisacry();

	public Set<Buffer> getPolymerizationMatrixBuffers();

	public Set<Agent> getAdditionalMatrixSubstances();

	public Set<Buffer> getAdditionalMatrixBuffers();

	public Set<SampleApplication> getSampleApplications();
}
