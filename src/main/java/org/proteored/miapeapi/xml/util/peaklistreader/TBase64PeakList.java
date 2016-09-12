package org.proteored.miapeapi.xml.util.peaklistreader;

//Exportada de PIKEAPI
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.soap.encoding.soapenc.Base64;

//Clase generada para la traducci�n de las listas de picos.
//Esta clase contendr� masas relativas a p�ptidos (PMF) o
//masas relativas a fragmentos (PFF).

public class TBase64PeakList {

	public static final int MZ = 1;
	public static final int INTENSITY = 2;
	// modos para la lectura
	private final String little_id = "little";
	private final String big_id = "big";
	// Vectores de datos
	private List<Double> MzArray = new ArrayList<Double>();
	private List<Double> IntensityArray = new ArrayList<Double>();
	// Referencia en el PRIDE
	private String SpectrumReference;
	// Los datos originales
	private String BinaryMzArray;
	private String BinaryIntensityArray;
	// n�mero de picos y codificaci�n
	private String MzDataPrecision;
	private ByteOrder MzDataCode;
	// Parece l�gico que si se inserta la lista de picos, entonces, la
	// comprobaci�n
	// de intervalos deber�a ser aqu�
	private double min_mass;
	private double max_mass;
	private double min_intensity;
	private double max_intensity;

	public TBase64PeakList() {
		SpectrumReference = "";
		MzDataPrecision = "64";
		MzDataCode = ByteOrder.LITTLE_ENDIAN;
		BinaryMzArray = "";
		BinaryIntensityArray = "";
		min_mass = 0.0D;
		max_mass = 0.0D;
		min_intensity = 1.7976931348623157E+308D;
		max_intensity = 0.0D;
	}

	// getMethods

	public String getSpectrumReference() {
		return SpectrumReference;
	}

	public int getMzArrayCount() {
		return this.MzArray.size();
	}

	public String getMzDataPrecision() {
		return MzDataPrecision;
	}

	public ByteOrder getDataCode() {
		return MzDataCode;
	}

	public String getCodification() {
		String _ret = "";
		if (MzDataCode == ByteOrder.BIG_ENDIAN)
			_ret = big_id;
		if (MzDataCode == ByteOrder.LITTLE_ENDIAN)
			_ret = little_id;
		return (_ret);
	}

	public String getBinaryMzArray() {
		return BinaryMzArray;
	}

	public String getBinaryIntensityArray() {
		return BinaryIntensityArray;
	}

	public List<Double> getMzArray() {
		return MzArray;
	}

	public List<Double> getIntensityArray() {
		return IntensityArray;
	}

	public double getMzElement(int _pos) {
		double _ret = -1D;
		if (_pos < getMzArrayCount())
			_ret = getMzArray().get(_pos);
		return _ret;
	}

	public Double getIntensityElement(int _pos) {
		Double _ret = -1D;
		if (_pos < getMzArrayCount()) {
			final List<Double> intensityArray = getIntensityArray();
			if (intensityArray != null && !intensityArray.isEmpty()
					&& _pos < intensityArray.size())
				_ret = intensityArray.get(_pos);
		}
		return _ret;
	}

	public double getRelativeIntensityElement(int _pos) {
		double _ret = getIntensityElement(_pos);
		if (_ret > (double) 0)
			_ret /= getMaxIntensity();
		return _ret;
	}

	public double getMinMass() {
		return min_mass;
	}

	public double getMaxMass() {
		return max_mass;
	}

	public double getMinIntensity() {
		return min_intensity;
	}

	public double getMaxIntensity() {
		return max_intensity;
	}

	// SetMethods

	public void setSpectrumReference(String _reference) {
		SpectrumReference = _reference;
	}

	public void SetMzDataPrecision(String _precision) {
		MzDataPrecision = _precision;
	}

	public void SetMzDataCode(String _code) {
		MzDataCode = ByteOrder.BIG_ENDIAN; // valor por defecto
		if (_code.compareTo(this.little_id) == 0)
			MzDataCode = ByteOrder.LITTLE_ENDIAN;
	}

	public void SetBinaryMzArray(String _mzArray) {
		BinaryMzArray = _mzArray;
	}

	public void SetBinaryIntensityArray(String _intensityArray) {
		BinaryIntensityArray = _intensityArray;
	}

	public void AddMzElement(double _value) {
		getMzArray().add(_value);
	}

	public void AddIntensityElement(double _value) {
		getIntensityArray().add(_value);
	}

	public void SetMinMass(double _arg) {
		min_mass = _arg;
	}

	public void SetMaxMass(double _arg) {
		max_mass = _arg;
	}

	public void SetMinIntensity(double _arg) {
		min_intensity = _arg;
	}

	public void SetMaxIntensity(double _arg) {
		max_intensity = _arg;
	}

	public void CheckMassInterval(double _value) {
		if (_value > getMaxMass())
			SetMaxMass(_value);
		if (_value < getMinMass())
			SetMinMass(_value);
	}

	public void CheckIntensityInterval(double _value) {
		if (_value > getMaxIntensity())
			SetMaxIntensity(_value);
		if (_value < getMinIntensity())
			SetMinIntensity(_value);
	}

	// public int MzDatatoBinary() {
	// int _ret = -1;
	// this.MzDataToBinary(MZ, this.GetBinaryMzArray());
	// this.MzDataToBinary(INTENSITY, this.GetBinaryIntensityArray());
	// return (_ret);
	// }

	public byte[] getByteMzData(int _mode) {
		// public static String encode( final double[] values, final ByteOrder
		// byteOrder ) {
		// String _ret = "";
		int BYTES_PER_DOUBLE = Byte.SIZE;
		int _length;

		_length = this.getMzArrayCount();
		byte[] byteArray = new byte[_length * BYTES_PER_DOUBLE];
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		buffer = buffer.order(this.MzDataCode);
		for (int i = 0; i < _length; i++) {
			if (_mode == MZ)
				buffer.putDouble(this.getMzElement(i));
			else {
				// the intensity can be null
				if (this.getIntensityElement(i) != null)
					buffer.putDouble(this.getIntensityElement(i));
				else
					buffer.putDouble(1.0);
			}
		}
		// make the base64 strings from the bytes
		// _ret = Base64.encode(buffer.array());
		// return (_ret);
		return byteArray;
	}

	public String getMzData(int _mode) {
		String _ret = "";
		_ret = Base64.encode(getByteMzData(_mode));
		return (_ret);
	}

	// private int MzDataToBinary(int _mode, String _input) {
	// int _ret, i, _capacity;
	// double _value;
	// try {
	// System.out.println(((String) ("El array en principio tiene: "))
	// .concat(String.valueOf(getMzArrayCount())));
	// // Insertamos las operaciones de lectura para que admita a 32 y 64
	// // little y big.
	// // Cambio
	// /*
	// * final byte[] byteArray = Base64.decode( encoded ); ByteBuffer
	// * byteBuffer = ByteBuffer.wrap( byteArray ); byteBuffer =
	// * byteBuffer.order( byteOrder ); final DoubleBuffer doubleBuffer =
	// * byteBuffer.asDoubleBuffer(); final double[] values = new double[
	// * doubleBuffer.capacity() ];
	// *
	// * for( int i = 0; i < values.length; i++ ) { values[ i ] =
	// * doubleBuffer.get(); }
	// *
	// * return values;
	// */
	//
	// byte byteArray[] = Base64.decode(_input);
	// /*
	// * Antiguo c�digo ByteArrayInputStream byte_in = new
	// * ByteArrayInputStream(byteArray); DataInputStream _mzarray = new
	// * DataInputStream(byte_in);
	// */
	// ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
	// byteBuffer = byteBuffer.order(this.GetDataCode());
	// // final DoubleBuffer doubleBuffer = byteBuffer. asDoubleBuffer();
	// // final double[] values = new double[ doubleBuffer.capacity() ];
	// _capacity = Integer.valueOf((byteBuffer.capacity() / Byte.SIZE))
	// .intValue();
	// this.SetMzArrayCount(_capacity);
	// if (_mode == 1)
	// MzArray = new double[getMzArrayCount()];
	// else if (_mode == 2)
	// IntensityArray = new double[getMzArrayCount()];
	// for (i = 0; i < getMzArrayCount(); i++)
	// // values[ i ] = doubleBuffer.get();
	// // for(int i = 0; i < getMzArrayCount(); i++)
	// {
	// try {
	// _value = byteBuffer.getDouble(); // doubleBuffer.get();
	// // double _value =
	// // Double.parseDouble(String.valueOf(_mzarray.readDouble()));
	// if (_mode == 1) {
	// System.out.println(((String) ("Insertando: "))
	// .concat(String.valueOf(_value)));
	// AddMzElement(i, _value);
	// CheckMassInterval(_value);
	// } else if (_mode == 2) {
	// System.out.println(((String) ("Insertando: "))
	// .concat(String.valueOf(_value)));
	// AddIntensityElement(i, _value);
	// CheckIntensityInterval(_value);
	// }
	// } catch (BufferUnderflowException ex) {
	// // hay menos picos que los indicados.
	// System.out.print(((String) ("El indice es: "))
	// .concat(String.valueOf(i)));
	// // ex.printStackTrace();
	// }
	// }
	// _ret = i;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// _ret = -1;
	// }
	// return (_ret);
	// }

	// nueva funci�n par meter un espectro desde un vector de puntos
	// public void InsertSpectrumValues(double _mz[], double _intensity[],
	// int _length) {
	// int i;
	// try // comprobar esto. A ver si se puede
	// {
	// MzArray = new double[_length];
	// IntensityArray = new double[_length];
	// for (i = 0; i < _length; i++) {
	// AddMzElement(i, _mz[i]);
	// CheckMassInterval(_mz[i]);
	// AddIntensityElement(i, _intensity[i]);
	// CheckIntensityInterval(_intensity[i]);
	// }
	// this.SetMzArrayCount(_length);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// }

	// nueva función para inicializar los vectores
	public void InicializeSpectrum(int _length, int _mode) {
		try // comprobar esto. A ver si se puede
		{

			MzArray.clear();
			if (_mode == INTENSITY)
				IntensityArray.clear();
			// this.SetMzArrayCount(_length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getPeakList(boolean _intensity, boolean _combined) {
		int i;
		String _ret = "";
		String _separator = " ";
		String _endofline = "\n";
		if (_combined) {
			_separator = ":";
			_endofline = ",";
		}
		for (i = 0; i < this.getMzArrayCount(); i++) {
			_ret += String.valueOf(this.getMzElement(i));
			if (_intensity) {
				_ret += _separator;
				_ret += String.valueOf(this.getIntensityElement(i));
			}
			_ret += _endofline;
		}
		// lo �ltimo para la combinada
		if (_combined && this.getMzArrayCount() > 0) // esto puede fallar si no
														// hay picos. Ojo!
			_ret = ((String) (" ions(")).concat(
					_ret.substring(0, _ret.length() - 1)).concat(")");
		return (_ret);
	}

}