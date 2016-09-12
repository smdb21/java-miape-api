// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 12/14/2007 12:59:38 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   TPeptideInformation.java
//NOTAS:
//FIRMADO EL APPLET EN PORTATIL

package org.proteored.miapeapi.xml.util.peaklistreader;

import java.util.List;

public class TPeptideInformation {

	public TPeptideInformation() {
		PeptideSequence = "NA";
		PeptideMass = 0.0D;
		Spot_id = "";
		job_item_id = "";
		peak_list_id = "";
		try {
			MyPeakList = new TBase64PeakList();
		} catch (Exception ex) {
		}
	}

	public String getPeptideSequence() {
		return PeptideSequence;
	}

	public double getPeptideMass() {
		return PeptideMass;
	}

	public int getPeptideCharge() {
		return PeptideCharge;
	}

	public TBase64PeakList getPeakList() {
		return (MyPeakList);
	}

	public String getSpectrumReference() {
		return getPeakList().getSpectrumReference();
	}

	public int getMzArrayCount() {
		return getPeakList().getMzArrayCount();
	}

	public List<Double> getMzArray() {
		return getPeakList().getMzArray();
	}

	public List<Double> getIntensityArray() {
		return getPeakList().getIntensityArray();
	}

	public double getMzElement(int _pos) {
		double _ret = -1D;
		if (_pos < getMzArrayCount())
			_ret = getMzArray().get(_pos);
		return _ret;
	}

	public double getIntensityElement(int _pos) {
		double _ret = -1D;
		if (_pos < getMzArrayCount())
			_ret = getIntensityArray().get(_pos);
		return _ret;
	}

	public double getRelativeIntensityElement(int _pos) {
		double _ret = getIntensityElement(_pos);
		if (_ret > (double) 0)
			_ret /= getMaxIntensity();
		return _ret;
	}

	public double getMinMass() {
		return getPeakList().getMinMass();
	}

	public double getMaxMass() {
		return getPeakList().getMaxMass();
	}

	public double getMinIntensity() {
		return getPeakList().getMinIntensity();
	}

	public double getMaxIntensity() {
		return getPeakList().getMaxIntensity();
	}

	// Set methods

	public void SetPeptideMass(double _arg) {
		PeptideMass = _arg;
	}

	public void SetPeptideCharge(int _arg) {
		PeptideCharge = _arg;
	}

	// M�todo nuevo para insertar el peaklist
	// La instancia est� creada, pero ahora le enviamos todos los
	// datos desde la lectura externa.

	public void SetSpectrumReference(String _reference) {
		getPeakList().setSpectrumReference(_reference);
	}

	// public void SetMzArrayCount(int _count) {
	// getPeakList().SetMzArrayCount(_count);
	// }

	public void SetMinMass(double _arg) {
		getPeakList().SetMinMass(_arg);
	}

	public void SetMaxMass(double _arg) {
		getPeakList().SetMaxMass(_arg);
	}

	public void SetMinIntensity(double _arg) {
		getPeakList().SetMinIntensity(_arg);
	}

	public void SetMaxIntensity(double _arg) {
		getPeakList().SetMaxIntensity(_arg);
	}

	// Nueva clase
	private TBase64PeakList MyPeakList;
	// Lo que si los datos de la base de datos
	private String Spot_id;
	private String job_item_id;
	private String peak_list_id;

	public static final int MZ = 1;
	public static final int INTENSITY = 2;
	private String PeptideSequence;
	private double PeptideMass;
	private int PeptideCharge;
	// modos para la lectura
}