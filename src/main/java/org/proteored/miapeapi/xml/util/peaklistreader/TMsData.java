/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.util.peaklistreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * 
 * @author Alberto
 * 
 *         Class description:
 * 
 *         Esta clase servirá como interface para la lectura de un fichero de
 *         lista de picos que se pasa como input (String) y lo modelizará para
 *         que sea llamada desde la aplicación de MIAPE para la incorporación
 *         de los datos como un MIAPE MS
 */
public class TMsData {
	private static final String COM = "COM=";
	// private boolean msmsSpectrum = true;
	// indica que cuando lea un espectro MSMS, debo insertarlo en la posici�n
	// donde me encuentre un espectro con la masa del pico igual a la parental
	// del MSMS
	// private boolean associateMSMS2MS = false;
	// public static String RETENTION_TIME_ORDER = "RT_ORDER";
	// public static String PARENT_MASS_ORDER = "PARENT_MASS_ORDER";
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	// Elementos globales....
	private final String Inputfilename;
	// El conjunto de spectros .... Aunque también puede ser una tabla hash
	private final TIntObjectHashMap<TSpectrum> pffSpectra;

	// Constantes .....
	private final String BEGINIONS = "BEGIN IONS";
	private final String ENDIONS = "END IONS";
	private final String PEPMASS = "PEPMASS=";
	private final String CHARGE = "CHARGE=";
	private final String TITLE = "TITLE";
	private final String RTINSECONDS = "RTINSECONDS";
	private final String ARRAYS_SEP = " ";
	private final String ARRAYS_TAB = "\t";
	// Para el test
	// Codificación
	private final String LITTLE_EN = "little";
	private final String BIG_EN = "big";
	private final boolean Outputview = false;
	private final boolean Sorted = false;
	private int maxSpectrumIndex = -1;

	public TMsData(final String inputfile, TObjectIntHashMap<String> mgfTitlesMaps) {
		log.info("Reading MGF from :" + inputfile);

		Inputfilename = inputfile;
		pffSpectra = new TIntObjectHashMap<TSpectrum>();
		readMGFFile(inputfile, mgfTitlesMaps);

		// for (int i = 0; i < PFFSpectra.size(); i++) {
		// // Metemos el spectrum Reference
		// setSpectrumReference(i, String.valueOf(i + 1));
		// }
		if (Outputview) {
			final TPeptideInformation _test = new TPeptideInformation();
			for (int i = 0; i < pffSpectra.size(); i++) {
				// Metemos el spectrum Reference
				setSpectrumReference(i, String.valueOf(i + 1));
				System.out.println("Nuevo orden: " + String.valueOf(i + 1));
				System.out.println("Spectrum Reference: " + getSpectrumReference(i));
				System.out.println(getQueryNumber(i));
				System.out.println(getPepMoverZ(i));
				System.out.println(getPepMass(i));
				System.out.println(getPepCharge(i));
				// meta data de los arrays
				System.out.println(getMzArrayPrecision(i));
				System.out.println(getMzArrayEndian(i));
				System.out.println(getMzArrayLength(i));
				System.out.println(getMzArrayPFFSpectrum(i));
				System.out.println(getIntensityPFFSpectrum(i));
				// VAmos a volver a traducir
				// _test.CreatePeakList(getMzArrayPFFSpectrum(i),
				// getIntensityPFFSpectrum(i),
				// Integer.valueOf(getMzArrayLength(i)), getMzArrayPrecision
				// (i), "little");
			}
		}

	}

	// GetMethods

	public String getFilename() {
		return Inputfilename;
	}

	public TIntObjectHashMap<TSpectrum> getSpectra() {
		return pffSpectra;
	}

	public TSpectrum getSpectrum(int queryNumber) {
		return pffSpectra.get(queryNumber);
	}

	// ReadFile

	public void readMGFFile(String _inputfile, TObjectIntHashMap mgfTitlesMaps)
	// Método para leer los ficheros de datos.
	// tanto para PMF como para PFF
	{
		int queryNumber;

		Double precursorMass;
		Integer charge;
		Double rt;
		String peakListId;
		Double centroidMass;
		Double peakHeight;
		ArrayList<Double> precursorMasses;
		ArrayList<Double> massesLocal;
		ArrayList<Double> intensities;
		int precursorCharge = -1;
		try {
			final URL url = new URL(_inputfile);
			final BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
			precursorMasses = new ArrayList<Double>();
			massesLocal = new ArrayList<Double>();
			intensities = new ArrayList<Double>();
			queryNumber = 0;
			peakListId = "";
			precursorMass = null;
			charge = null;
			rt = null;
			// initialize heads

			do {

				String inLine;
				if ((inLine = inStream.readLine()) == null)
					break;
				inLine = inLine.trim();
				log.debug(inLine);
				if (!inLine.equals("") && !inLine.startsWith("#")) {
					if (inLine.compareTo(BEGINIONS) == 0) {
						log.debug("MS/MS spectrum");
						precursorMass = null;
						charge = null;
						intensities.clear();
						peakListId = "";
						queryNumber++;
						if (queryNumber > maxSpectrumIndex)
							maxSpectrumIndex = queryNumber;
						rt = null;
					} else {
						if (inLine.compareTo(ENDIONS) == 0) {
							// if massesLocal is empty means that it is from a
							// MS1 spectrum, so add the mass to the
							// precursorMasses array
							if (massesLocal.isEmpty()) {
								precursorMasses.add(precursorMass);
								if (charge != null)
									precursorCharge = charge;
							} else {

								// create the MS2 spectrum with the masses in
								// "massesLocal" array with the reference to
								// precursor to -1. It will be updated at the
								// end
								final TSpectrum mySpectrum = createSpectrum(queryNumber, -1, peakListId, precursorMass,
										charge, rt);

								mySpectrum.fillMassValues(massesLocal, intensities);
								mySpectrum.setMsLevel(2);
								pffSpectra.put(queryNumber, mySpectrum);
								// empty array "massesLocal"
								massesLocal.clear();

							}
						} else {
							if (inLine.startsWith(PEPMASS))
								precursorMass = parsePrecursorMass(inLine);
							else if (inLine.startsWith(CHARGE))
								charge = parseCharge(inLine);
							else if (inLine.startsWith(TITLE)) {
								peakListId = parseTitle(inLine);
								// if map is not null, store the relationship
								// between the TITLE and the index of the
								// spectrum
								if (mgfTitlesMaps != null)
									mgfTitlesMaps.put(peakListId, queryNumber);
							} else if (inLine.startsWith(RTINSECONDS))
								rt = parseRT(inLine);
							// en otro caso será una pareja de elemenos
							else {
								if (inLine.contains(ARRAYS_SEP) || inLine.contains(ARRAYS_TAB)) {
									String separator = null;
									if (inLine.contains(ARRAYS_SEP))
										separator = ARRAYS_SEP;
									else {
										separator = ARRAYS_TAB;
									}

									final String[] split = inLine.split(separator);
									if (split.length == 2 || split.length == 3) {

										centroidMass = Double.valueOf(split[0]);
										peakHeight = Double.valueOf(split[1]);

										massesLocal.add(centroidMass);
										intensities.add(peakHeight);

									}
								}
							}

						} // else de contenido
					} // else begin ions
				} // Parseo de la línea.....
			}

			while (true);

			// if there are masses in the array "masses" is because the MGF
			// comes from a MALDI processing where the MS1 spectra is
			// distributed along the file. So, I will create it now and then I
			// will relate all the MS/MS spectrum to this MS1 spectrum
			// create the MS1 spectrum with the masses in
			// "masses" array
			if (!precursorMasses.isEmpty()) {
				// search backward a query number that is not used to use it as
				// precursorQueryNumber, starting by the maximum queryNumber
				// captured
				final TIntArrayList queryNumbers = new TIntArrayList();
				for (final int key : pffSpectra.keys()) {
					queryNumbers.add(key);
				}
				queryNumbers.sort();
				queryNumber = queryNumbers.get(queryNumbers.size() - 1);
				while (pffSpectra.containsKey(queryNumber)) {
					log.info("Searching a valid precursor queryNumber: " + queryNumber);
					queryNumber--;
				}
				final int precursorQueryNumber = queryNumber;
				final TSpectrum precursorSpectrum = createSpectrum(precursorQueryNumber, -1, peakListId, null,
						precursorCharge, rt);
				precursorSpectrum.fillMassValues(precursorMasses, null);
				precursorSpectrum.setMsLevel(1);
				log.info("MS1 spectrum created with index=" + queryNumber);
				// iterate all MS2 spectra to asign the precursor spectrum to
				// precursorQueryNumber
				for (final TSpectrum spectrum : pffSpectra.valueCollection()) {
					spectrum.setPrecursorQueryNumber(precursorQueryNumber);
				}
				pffSpectra.put(precursorQueryNumber, precursorSpectrum);
			}

		} catch (

		final IOException ex) {
			ex.printStackTrace();
		}
		log.info(pffSpectra.size() + " spectra readed from '" + _inputfile + "'");
	}

	// Métodos de parseo.....
	private Double parsePrecursorMass(String _line) {
		// En principio es del tipo
		String _ret = "";
		int nexttabPos = _line.length();
		if (_line.indexOf(ARRAYS_SEP) > 0)
			nexttabPos = _line.indexOf(ARRAYS_SEP);
		else if (_line.indexOf(ARRAYS_TAB) > 0)
			nexttabPos = _line.indexOf(ARRAYS_TAB);
		_ret = _line.substring(_line.indexOf(PEPMASS) + PEPMASS.length(), nexttabPos);
		try {

			return Double.valueOf(_ret);
		} catch (final NumberFormatException e) {

		}
		return null;
	}

	private Double parseRT(String _line) {
		// En principio es del tipo RTINSECONDS=25
		if (_line.contains("=")) {
			final String[] split = _line.split("=");
			if (split.length == 2) {
				try {
					return Double.valueOf(split[1]);
				} catch (final NumberFormatException e) {
					// it can be also like: RTINSECONDS=95-97
					// take the first value, in the example, the 95
					if (split[1].contains("-")) {
						final String[] split2 = split[1].split("-");
						if (split2.length == 2) {
							try {
								return Double.valueOf(split2[0]);
							} catch (final NumberFormatException e2) {

							}
						}

					}
				}
			}
		}
		return null;
	}

	private Integer parseCharge(String _line) {
		// En principio es del tipo
		String _ret = null;
		int index = -1;
		if ((index = _line.indexOf("+")) > 0)
			_ret = _line.substring(_line.indexOf(CHARGE) + CHARGE.length(), index);
		else if ((index = _line.indexOf("-")) > 0)
			_ret = _line.substring(_line.indexOf(CHARGE), index);

		try {
			return Integer.valueOf(_ret);
		} catch (final NumberFormatException e) {

		}
		return null;
	}

	private String parseTitle(String _line) {
		// En principio es del tipo
		String _ret = "";
		if (_line.contains("=")) {
			final String[] split = _line.split("=");
			if (split.length == 2)
				return split[1];
		}
		_ret = _line.substring(_line.indexOf(TITLE) + TITLE.length(), _line.length());
		return (_ret);
	}

	private TSpectrum createSpectrum(int queryNumber, int precursorQueryNumber, String _peak_list_id,
			Double precursor_mass, Integer _charge, Double rt) {

		try {
			final TSpectrum mySpectrum = new TSpectrum(queryNumber, precursorQueryNumber, _peak_list_id, precursor_mass,
					rt);

			mySpectrum.setPeptideCharge(_charge);
			return mySpectrum;
		} catch (final Exception ex2) {
			ex2.printStackTrace();
		}
		return null;
	}

	// A modo de prueb

	// Metadata
	public String getMzArrayPrecision(int _pos) {
		String _ret = "";
		_ret = pffSpectra.get(_pos).getPeakList().getMzDataPrecision();
		return (_ret);
	}

	public String getMzArrayEndian(int _pos) {
		String _ret = "";
		_ret = pffSpectra.get(_pos).getPeakList().getCodification();
		return (_ret);
	}

	public int getMzArrayLength(int _pos) {

		final TSpectrum tSpectrum = pffSpectra.get(_pos);
		final TBase64PeakList peakList = tSpectrum.getPeakList();
		final int mzArrayCount = peakList.getMzArrayCount();
		return mzArrayCount;
	}

	public String getMzArrayPFFSpectrum(int _pos) {
		String _ret = "";
		_ret = pffSpectra.get(_pos).getPeakList().getMzData(TBase64PeakList.MZ);
		return (_ret);
	}

	public String getIntensityPFFSpectrum(int _pos) {
		String _ret = "";
		_ret = pffSpectra.get(_pos).getPeakList().getMzData(TBase64PeakList.INTENSITY);
		return (_ret);
	}

	// métodos para el array de bytes.
	public byte[] getMzByteArrayPFFSpectrum(int _pos) {
		return pffSpectra.get(_pos).getPeakList().getByteMzData(TBase64PeakList.MZ);
	}

	public byte[] getIntensityByteArrayPFFSpectrum(int _pos) {
		return pffSpectra.get(_pos).getPeakList().getByteMzData(TBase64PeakList.INTENSITY);
	}

	public Double getPepMass(int _pos) {
		Double _ret = 0.0;
		_ret = pffSpectra.get(_pos).getPeptideMass();
		return (_ret);
	}

	public Double getPepMoverZ(int _pos) {
		Double _ret = 0.0;
		_ret = pffSpectra.get(_pos).getPeptideMoverZ();
		return (_ret);
	}

	public Double getRT(int _pos) {
		Double _ret = 0.0;
		_ret = pffSpectra.get(_pos).getRT();
		return (_ret);
	}

	public int getPepCharge(int _pos) {
		int _ret = -1;
		_ret = pffSpectra.get(_pos).getPeptideCharge();
		return (_ret);
	}

	public int getQueryNumber(int _pos) {
		int _ret = -1;
		_ret = pffSpectra.get(_pos).getQueryNumber();
		return (_ret);
	}

	public String getSpectrumReference(int _pos) {
		String _ret = "";
		_ret = pffSpectra.get(_pos).getPeakList().getSpectrumReference();
		return (_ret);
	}

	public void setSpectrumReference(int _pos, String _value) {
		pffSpectra.get(_pos).getPeakList().setSpectrumReference(_value);
	}

	public int getMaxSpectrumIndex() {
		return maxSpectrumIndex;
	}

}
