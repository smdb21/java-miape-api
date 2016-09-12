package org.proteored.miapeapi.interfaces.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;

public class ProjectFile {
	protected File file;

	public ProjectFile() {
		try {
			this.file = File.createTempFile(this.getClass().getName() + System.currentTimeMillis(), "temp");

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalMiapeArgumentException(e);
		}
	}

	public ProjectFile(File file) {
		this.file = file;
	}

	public ProjectFile(String name) {
		this.file = new File(name);
	}

	public ProjectFile(byte[] bytes) throws IOException {
		this.file = createFile(bytes);
	}

	private static File createFile(byte[] bytes) throws IOException {
		File file;
		file = File.createTempFile("temp" + System.currentTimeMillis(), ".temp");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
		return file;
	}

	public byte[] toBytes() throws IOException {
		return getBytesFromFile(file);
	}

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public File toFile() {
		return file;
	}
}
