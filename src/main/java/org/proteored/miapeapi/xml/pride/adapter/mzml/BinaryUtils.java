package org.proteored.miapeapi.xml.pride.adapter.mzml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;

import uk.ac.ebi.jmzml.model.mzml.BinaryDataArray.Precision;

public class BinaryUtils {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	// ByteOrder.LITTLE_ENDIAN

	public static byte[] transformBinaryArray(byte[] oldbinaryArray, Precision precision,
			ByteOrder order) {
		// transform to double array
		double[] doubleArray = BinaryUtils.toDoubleArray(oldbinaryArray, precision, order);
		return BinaryUtils.toByteArray(doubleArray, order, precision);
	}

	private static byte[] toByteArray(double[] doubleArray, ByteOrder byteOrder, Precision precision) {
		// public static String encode( final double[] values, final ByteOrder
		// byteOrder ) {
		// String _ret = "";
		int BYTES_PER_DOUBLE = Double.SIZE / Byte.SIZE;
		// int BYTES_PER_DOUBLE = getNumOfByte(precision);
		int _length;

		byte[] byteArray = new byte[doubleArray.length * BYTES_PER_DOUBLE];
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		buffer = buffer.order(byteOrder);
		for (int i = 0; i < doubleArray.length; i++) {
			buffer.putDouble(doubleArray[i]);
		}
		// make the base64 strings from the bytes
		// _ret = Base64.encode(buffer.array());
		// return (_ret);

		return buffer.array();
	}

	public static byte[] decompress(byte[] compressedData) {
		byte[] decompressedData;

		// using a ByteArrayOutputStream to not having to define the result
		// array size beforehand
		Inflater decompressor = new Inflater();
		decompressor.setInput(compressedData);
		// Create an expandable byte array to hold the decompressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (DataFormatException e) {
				throw new IllegalStateException("Encountered wrong data format "
						+ "while trying to decompress binary data!", e);
			}
		}
		try {
			bos.close();
		} catch (IOException e) {
			// ToDo: add logging
			e.printStackTrace();
		}
		// Get the decompressed data
		decompressedData = bos.toByteArray();

		if (decompressedData == null) {
			throw new IllegalStateException(
					"Decompression of binary data prodeuced no result (null)!");
		}
		// log.info("Decompressing from " + compressedData.length + "bytes to "
		// + decompressedData.length);
		return decompressedData;
	}

	/**
	 * Compress byte array
	 * 
	 * @param uncompressedData
	 *            uncompressed byte array
	 * @return byte[] compressed byte array
	 */
	public static byte[] compress(byte[] uncompressedData) {
		byte[] compressedData;// Decompress the data

		// create a temporary byte array big enough to hold the compressed data
		// with the worst compression (the length of the initial (uncompressed)
		// data)
		byte[] temp = new byte[uncompressedData.length];
		// compress
		Deflater compresser = new Deflater();
		compresser.setInput(uncompressedData);
		compresser.finish();
		int cdl = compresser.deflate(temp);
		// create a new array with the size of the compressed data (cdl)
		compressedData = new byte[cdl];
		System.arraycopy(temp, 0, compressedData, 0, cdl);

		// log.info("Compressing from " + uncompressedData.length + " bytes to "
		// + compressedData.length + " bytes");
		return compressedData;
	}

	private static Number[] toNumberArray(byte[] arr, Precision precision, ByteOrder order) {
		int numOfByte = getNumOfByte(precision);
		int arrLength = arr.length;
		Number[] results = new Number[arrLength / numOfByte];
		ByteBuffer buffer = ByteBuffer.wrap(arr);
		buffer.order(order);
		try {
			for (int i = 0; i < arrLength; i += numOfByte) {
				Number num;
				if (precision.equals(Precision.INT32BIT))

					num = buffer.getInt(i);

				else if (precision.equals(Precision.FLOAT32BIT))
					num = buffer.getFloat(i);

				else if (precision.equals(Precision.INT64BIT))
					num = buffer.getLong(i);

				else if (precision.equals(Precision.FLOAT64BIT))
					num = buffer.getDouble(i);

				else
					num = null;

				results[i / numOfByte] = num;
			}
		} catch (Exception ex) {
			log.error("Failed to byte array to number array: " + precision + "\t"
					+ order.toString());
			return new Number[0];
		}

		return results;
	}

	/**
	 * Convert to double array
	 * 
	 * @param arr
	 *            byte array
	 * @param precision
	 *            precision
	 * @param order
	 *            endianess
	 * @return double[] double array
	 */
	private static double[] toDoubleArray(byte[] arr, Precision precision, ByteOrder order) {
		Number[] numArr = toNumberArray(arr, precision, order);
		double[] doubleArr = new double[numArr.length];

		for (int i = 0; i < numArr.length; i++) {
			doubleArr[i] = numArr[i].doubleValue();
		}
		return doubleArr;
	}

	private static int getNumOfByte(Precision precision) {
		int numOfByte;

		if (precision.equals(Precision.INT32BIT))
			numOfByte = 4;

		else if (precision.equals(Precision.FLOAT32BIT))
			numOfByte = 4;

		else if (precision.equals(Precision.INT64BIT))
			numOfByte = 8;

		else if (precision.equals(Precision.FLOAT64BIT))
			numOfByte = 8;

		else
			numOfByte = -1;

		return numOfByte;
	}
}