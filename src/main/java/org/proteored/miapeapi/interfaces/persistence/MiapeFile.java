package org.proteored.miapeapi.interfaces.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;

/**
 * Represents a MIAPE document in a File
 * 
 * @author Salva
 * 
 */
public class MiapeFile {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	protected File file;
	private boolean isTempFile = false; // means that the file is a temp file,
										// so it will be deleted when "saveAs"
										// is invoqued

	public MiapeFile() {
		try {
			this.file = File.createTempFile(
					this.getClass().getName() + System.currentTimeMillis(),
					"temp");
			this.isTempFile = true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalMiapeArgumentException(e);
		}
	}

	public MiapeFile(File file) {
		this.file = file;
	}

	public MiapeFile(String name) {
		this.file = new File(name);
	}

	public MiapeFile(byte[] bytes) throws IOException {
		this.file = createFile(bytes);
		this.isTempFile = true;
	}

	public String getPath() {
		return file.getAbsolutePath();
	}

	public File saveAs(String path) throws IOException {
		log.info("Trying to save MIAPE file to: " + path);
		// if (file.exists() == false)
		// throw new IOException("The file in " + file.getPath() +
		// " does not exist");
		File newFile = new File(path);
		if (newFile.exists()) {
			newFile.delete();
		}
		String oldPath = this.file.getAbsolutePath();
		MiapeFile.copy(file, newFile);
		File oldFile = file;
		this.file = newFile;
		log.info("File renamed from " + oldPath + " to "
				+ this.file.getAbsolutePath() + " - "
				+ newFile.getAbsolutePath());
		if (this.isTempFile) {
			oldFile.delete();
			log.info("Temp file deleted: " + oldFile.getAbsolutePath());
		}
		return newFile;
	}

	private static File createFile(byte[] bytes) throws IOException {
		if (bytes == null)
			return null;

		File file;
		file = File.createTempFile("temp", null);
		if (file != null) {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		}
		return file;
	}

	public byte[] toBytes() throws IOException {
		return getBytesFromFile(file);
	}

	public File toFile() {
		return file;
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
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	@Override
	public String toString() {
		if (file.exists() == false)
			return null;
		try {
			return readFileAsString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void copy(File fromFile, File toFile) throws IOException {

		if (!fromFile.exists())
			throw new IOException("FileCopy: " + "no such source file: "
					+ fromFile.getName());
		if (!fromFile.isFile())
			throw new IOException("FileCopy: " + "can't copy directory: "
					+ fromFile.getName());
		if (!fromFile.canRead())
			throw new IOException("FileCopy: " + "source file is unreadable: "
					+ fromFile.getName());

		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		String parent = toFile.getParent();
		if (parent == null)
			parent = System.getProperty("user.dir");
		File dir = new File(parent);
		if (!dir.exists())
			throw new IOException("FileCopy: "
					+ "destination directory doesn't exist: " + parent);
		if (dir.isFile())
			throw new IOException("FileCopy: "
					+ "destination is not a directory: " + parent);
		if (!dir.canWrite())
			throw new IOException("FileCopy: "
					+ "destination directory is unwriteable: " + parent);

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}

	private static String readFileAsString(File file)
			throws java.io.IOException {

		InputStream is = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(is, "UTF-8");

		StringBuffer fileData = new StringBuffer(1000);
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		is.close();
		return fileData.toString();
	}

	public boolean exists() {
		return file.exists();
	}

	public void delete() {
		file.delete();
	}
}
