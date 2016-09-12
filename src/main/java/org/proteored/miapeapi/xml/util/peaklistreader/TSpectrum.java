/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.proteored.miapeapi.xml.util.peaklistreader;

import java.util.List;

public class TSpectrum {

	private int msLevel;
	private TBase64PeakList myPeakList;
	public static final int MZ = 1;
	public static final int INTENSITY = 2;
	private double peptideMass;
	private Double mzStart = Double.MAX_VALUE;
	private Double mzEnd = Double.MIN_VALUE;
	private Double peptideMz;
	private Double rt;
	// Nueva variable.
	// Tabla hash para la informaci�n adicional
	// novedad para la carga
	private int peptideCharge;
	// modos para la lectura
	private final int queryNumber;
	private String peak_list_id;
	private int precursorQueryNumber;

	public TSpectrum(int queryNumber, int precursorQueryNumber,
			String _peak_list_id, Double _mz, Double rt) {
		setPeptideMZ(_mz);
		setPeptideCharge(-1);
		this.queryNumber = queryNumber;
		this.precursorQueryNumber = precursorQueryNumber;
		peak_list_id = _peak_list_id;
		setRT(rt);
		try {
			myPeakList = new TBase64PeakList();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
	}

	public void fillMassValues(List<Double> masses, List<Double> intensities) {
		this.getPeakList().InicializeSpectrum(masses.size(), INTENSITY);
		this.FillMassValues(masses, MZ);
		this.FillMassValues(intensities, INTENSITY);
		if (masses != null && !masses.isEmpty())
			for (Object object : masses) {
				try {
					Double d = (Double) object;
					if (d < this.mzStart)
						this.mzStart = d;
					if (d > this.mzEnd)
						this.mzEnd = d;
				} catch (NumberFormatException e) {

				}
			}
		else {
			this.mzEnd = null;
			this.mzStart = null;
		}
	}

	public void addMassValue(Double mass, Double intensity) {
		this.fillMassValue(mass, MZ);
		this.fillMassValue(intensity, INTENSITY);
		for (Double d : this.myPeakList.getMzArray()) {
			try {
				if (d < this.mzStart)
					this.mzStart = d;
				if (d > this.mzEnd)
					this.mzEnd = d;
			} catch (NumberFormatException e) {

			}
		}
	}

	public void FillMassValues(List<Double> _values, int _mode) {
		if (_values != null)
			for (Double value : _values) {
				if (_mode == MZ)
					this.myPeakList.AddMzElement(value);
				else
					this.myPeakList.AddIntensityElement(value);
			}
	}

	public void fillMassValue(Double value, int _mode) {
		if (value != null)
			if (_mode == MZ)
				this.myPeakList.AddMzElement(value);
			else
				this.myPeakList.AddIntensityElement(value);

	}

	public int getQueryNumber() {
		return (queryNumber);
	}

	public String getPeak_list_id() {
		return (peak_list_id);
	}

	public Double getPeptideMz() {
		return (peptideMz);
	}

	public double getPeptideMass() {
		return (peptideMass);
	}

	public Double getMzStart() {
		return this.mzStart;
	}

	public Double getMzEnd() {
		return this.mzEnd;
	}

	public Double getPeptideMoverZ() {
		return peptideMz;
	}

	public int getPeptideCharge() {
		return peptideCharge;
	}

	// nuevo
	public void setPeptideCharge(Integer _value) {
		if (_value != null) {
			peptideCharge = _value;
			// calculamos la masa monoparental
			final Double peptideMz = getPeptideMz();
			if (peptideMz != null)
				setPeptideMass((peptideMz - 1) * peptideCharge);
		}
	}

	private void setPeptideMass(double _value) {
		peptideMass = _value;
	}

	private void setRT(Double rt) {
		this.rt = rt;
	}

	public Double getRT() {
		return this.rt;
	}

	private void setPeptideMZ(Double _value) {
		peptideMz = _value;
	}

	public int getMsLevel() {
		return this.msLevel;
	}

	public void setMsLevel(int level) {
		this.msLevel = level;
	}

	public TBase64PeakList getPeakList() {
		return (myPeakList);
	}

	public int getPrecursorQueryNumber() {
		return precursorQueryNumber;
	}

	public void setPrecursorQueryNumber(int precursorQueryNumber) {
		this.precursorQueryNumber = precursorQueryNumber;

	}

}
