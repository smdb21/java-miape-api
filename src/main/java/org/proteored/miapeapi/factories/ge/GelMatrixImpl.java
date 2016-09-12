package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.SampleApplication;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class GelMatrixImpl implements GelMatrix {
	private final String name;
	private final String type;
	private final String composed;
	private final String manufacture;
	private final Double dimensionsX;
	private final Double dimensionsY;
	private final Double dimensionsZ;
	private final String dimensionsUnit;
	private final String phRangeL;
	private final String phRangeH;
	private final String phRangeType;
	private final String mwRangeL;
	private final String mwRangeH;
	private final String mwRangeUnit;
	private final String mwRangeType;
	private final String acrylamideConcentration;
	private final String acry;
	private final String bisacry;
	private final Set<Buffer> polymerizationMatrixBuffers;
	private final Set<Agent> additionalMatrixSubstances;
	private final Set<Buffer> additionalMatrixBuffers;
	private final Set<SampleApplication> sampleApplications;

	public GelMatrixImpl(GelMatrixBuilder miapeGelMatrixBuilder) {
		this.name = miapeGelMatrixBuilder.name;
		this.type = miapeGelMatrixBuilder.type;
		this.composed = miapeGelMatrixBuilder.composed;
		this.manufacture = miapeGelMatrixBuilder.manufacture;
		this.dimensionsX = miapeGelMatrixBuilder.dimensionsX;
		this.dimensionsY = miapeGelMatrixBuilder.dimensionsY;
		this.dimensionsZ = miapeGelMatrixBuilder.dimensionsZ;
		this.dimensionsUnit = miapeGelMatrixBuilder.dimensionsUnit;
		this.phRangeL = miapeGelMatrixBuilder.phRangeL;
		this.phRangeH = miapeGelMatrixBuilder.phRangeH;
		this.phRangeType = miapeGelMatrixBuilder.phRangeType;
		this.mwRangeL = miapeGelMatrixBuilder.mwRangeL;
		this.mwRangeH = miapeGelMatrixBuilder.mwRangeH;
		this.mwRangeUnit = miapeGelMatrixBuilder.mwRangeUnit;
		this.mwRangeType = miapeGelMatrixBuilder.mwRangeType;
		this.acrylamideConcentration = miapeGelMatrixBuilder.acrylamideConcentration;
		this.acry = miapeGelMatrixBuilder.acry;
		this.bisacry = miapeGelMatrixBuilder.bisacry;
		this.polymerizationMatrixBuffers = miapeGelMatrixBuilder.polymerizationMatrixBuffers;
		this.additionalMatrixSubstances = miapeGelMatrixBuilder.additionalMatrixSubstances;
		this.additionalMatrixBuffers = miapeGelMatrixBuilder.additionalMatrixBuffers;
		this.sampleApplications = miapeGelMatrixBuilder.sampleApplications;

	}

	@Override
	public String getAcry() {
		return acry;
	}

	@Override
	public String getAcrylamideConcentration() {
		return acrylamideConcentration;
	}

	@Override
	public Set<Buffer> getAdditionalMatrixBuffers() {
		return additionalMatrixBuffers;
	}

	@Override
	public Set<Agent> getAdditionalMatrixSubstances() {
		return additionalMatrixSubstances;
	}

	@Override
	public String getBisacry() {
		return bisacry;
	}

	@Override
	public String getComposed() {
		return composed;
	}

	@Override
	public String getDimensionsUnit() {
		return dimensionsUnit;
	}

	@Override
	public Double getDimensionsX() {
		return dimensionsX;
	}

	@Override
	public Double getDimensionsY() {
		return dimensionsY;
	}

	@Override
	public Double getDimensionsZ() {
		return dimensionsZ;
	}

	@Override
	public String getGelManufacture() {
		return manufacture;
	}

	@Override
	public String getMwRangeH() {
		return mwRangeH;
	}

	@Override
	public String getMwRangeL() {
		return mwRangeL;
	}

	@Override
	public String getMwRangeType() {
		return mwRangeType;
	}

	@Override
	public String getMwRangeUnit() {
		return mwRangeUnit;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPhRangeH() {
		return phRangeH;
	}

	@Override
	public String getPhRangeL() {
		return phRangeL;
	}

	@Override
	public String getPhRangeType() {
		return phRangeType;
	}

	@Override
	public Set<Buffer> getPolymerizationMatrixBuffers() {
		return polymerizationMatrixBuffers;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Set<SampleApplication> getSampleApplications() {
		return sampleApplications;
	}

	@Override
	public int getId() {
		return MiapeXmlUtil.gelmatrixcounter.increaseCounter();
	}

}
