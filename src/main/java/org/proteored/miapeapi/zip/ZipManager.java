package org.proteored.miapeapi.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class ZipManager {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static final int BUFFER_SIZE = 8192;

	/**
	 * Compress a file in a zip file
	 * 
	 * @throws IOException
	 */
	// public static String compressFile(File inputFile, File outputFile) throws
	// IOException {
	// if (outputFile == null)
	// outputFile = new File(inputFile.getAbsolutePath() + ".zip");
	//
	// // Create a buffer for reading the files
	// byte[] buf = new byte[1024];
	//
	// // Create the ZIP file
	//
	// ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new
	// FileOutputStream(
	// outputFile)));
	//
	// // Compress the files
	//
	// FileInputStream in = new FileInputStream(inputFile);
	//
	// String name = inputFile.getName();
	//
	// ZipEntry zipEntry = new ZipEntry(name);
	// // Add ZIP entry to output stream.
	// out.putNextEntry(zipEntry);
	//
	// // Transfer bytes from the file to the ZIP file
	// int len;
	// while ((len = in.read(buf)) > 0) {
	// out.write(buf, 0, len);
	// }
	//
	// // Complete the entry
	// out.closeEntry();
	// in.close();
	//
	// // Complete the ZIP file
	// out.close();
	//
	// return outputFile.getAbsolutePath();
	//
	// }

	public static File compressGZipFile(File inputFile) throws IOException {

		return ZipManager.compressGZipFile(inputFile, null);

	}

	public static File compressGZipFile(File inputFile, File outputFile)
			throws IOException {
		// if it is already compressed, return the input file
		if (FilenameUtils.getExtension(inputFile.getName()).equalsIgnoreCase(
				"gz"))
			return inputFile;

		if (outputFile == null)
			outputFile = new File(inputFile.getAbsolutePath() + ".gz");

		// Create the GZIP file
		BufferedOutputStream bufferedOut = new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(outputFile)));
		BufferedInputStream bufferedIn = new BufferedInputStream(
				new FileInputStream(inputFile));

		copyInputStream(bufferedIn, bufferedOut);

		return outputFile;

	}

	/**
	 * Decompress the FIRST entry of a zip file in the same folder as the zip,
	 * or in a temp file if there is some problem writting in that folder
	 * 
	 * @param file
	 * @return the first entry of the zipped file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	// public static File decompressFile(File file) throws
	// FileNotFoundException, IOException {
	// log.info("processing ZIP file: " + file.getAbsolutePath());
	// ZipFile zipFile = new ZipFile(file);
	//
	// Enumeration entries = zipFile.entries();
	//
	// // while (entries.hasMoreElements()) {
	// // the zip file only has to have one file
	// ZipEntry entry = (ZipEntry) entries.nextElement();
	//
	// // if (entry.isDirectory()) {
	// // // Assume directories are stored parents first then children.
	// // System.err.println("Extracting directory: " + entry.getName());
	// // // This is not robust, just for demonstration purposes.
	// // (new File(entry.getName())).mkdirs();
	// //
	// // }
	//
	// log.info("Extracting zip entry " + entry.getName() + " from zip");
	// String name = file.getParent() + File.separator + entry.getName();
	// log.info("Saving zip entry to " + name);
	// FileOutputStream out;
	// try {
	// out = new FileOutputStream(name);
	// } catch (Exception ex) {
	// File tempFile = File.createTempFile("temp", null);
	// name = tempFile.getAbsolutePath();
	// log.info("Trying saving it in " + name);
	// out = new FileOutputStream(tempFile);
	// }
	// BufferedOutputStream outputBufferedOutputStream = new
	// BufferedOutputStream(out);
	//
	// InputStream inputStream = zipFile.getInputStream(entry);
	// BufferedInputStream inputBufferedInputStream = new
	// BufferedInputStream(inputStream);
	// byte[] buffer = new byte[8192];
	// inputStream.read(buffer);
	// copyInputStream(inputStream, out);
	// // }
	// outputBufferedOutputStream.close();
	// zipFile.close();
	// log.info("Zip decompressed successfully");
	// return new File(name);
	//
	// }

	public static File decompressGZipFile(File file)
			throws FileNotFoundException, IOException {
		log.info("processing GZip file: " + file.getAbsolutePath());

		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bufferedIn = new BufferedInputStream(
				new GZIPInputStream(fin));

		String outputName = FilenameUtils.getFullPath(file.getAbsolutePath())
				+ FilenameUtils.getBaseName(file.getAbsolutePath());
		if (outputName.equals(file.getAbsolutePath()))
			outputName = outputName + "_";
		BufferedOutputStream bufferedOut = new BufferedOutputStream(
				new FileOutputStream(outputName));
		log.info("Decompressing to : " + outputName);

		copyInputStream(bufferedIn, bufferedOut);

		return new File(outputName);

	}

	// private static final void copyInputStream(BufferedInputStream in,
	// BufferedOutputStream out)
	// throws IOException {
	//
	// while (true) {
	// int data = in.read();
	// if (data == -1) {
	// break;
	// }
	// out.write(data);
	// }
	//
	// in.close();
	// out.close();
	// }

	public static final void copyInputStream(BufferedInputStream bufferedIn,
			BufferedOutputStream bufferedOut) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			while (true) {
				int amountRead = bufferedIn.read(buffer);
				if (amountRead == -1) {
					break;
				}
				bufferedOut.write(buffer, 0, amountRead);
			}
		} finally {
			// CLose streams
			if (bufferedIn != null)
				bufferedIn.close();
			if (bufferedOut != null)
				bufferedOut.close();
		}
	}

	/**
	 * 
	 * 
	 * @param file
	 * @return
	 */
	// public static File decompressFileIfNeccessary(File file) {
	// log.info("Decompressing file " + file.getAbsolutePath());
	//
	// try {
	// return ZipManager.decompressFile(file);
	// } catch (Exception e) {
	// log.info("The file " + file.getAbsolutePath()
	// + " is not a zipped file. Returning the original file");
	// e.printStackTrace();
	// }
	//
	// return file;
	// }

	public static File decompressGZipFileIfNeccessary(File file) {
		log.info("Decompressing file " + file.getAbsolutePath());

		try {
			return ZipManager.decompressGZipFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.info("The file " + file.getAbsolutePath() + " has not found");
		} catch (IOException e) {
			log.info("The file " + file.getAbsolutePath()
					+ " is not a zipped file. Returning the original file");
		}

		return file;
	}

	/**
	 * 
	 * @param file
	 * @param deleteOnExit
	 *            indicates if the decompressed file (if the source is is a gzip
	 *            file) will be delete on exit or not
	 * @return
	 */
	public static File decompressGZipFileIfNeccessary(File file,
			boolean deleteOnExit) {
		log.info("Decompressing file " + file.getAbsolutePath());

		try {
			File decompressGZipFile = ZipManager.decompressGZipFile(file);
			if (decompressGZipFile != null && deleteOnExit) {
				if (decompressGZipFile.getAbsolutePath() != file
						.getAbsolutePath()) {
					log.info("Setting 'deleteOnExit' to file: '"
							+ decompressGZipFile.getAbsolutePath() + "'");
					decompressGZipFile.deleteOnExit();
					file.deleteOnExit();
				}
			}
			return decompressGZipFile;
		} catch (Exception e) {
			log.info("The file " + file.getAbsolutePath()
					+ " is not a zipped file. Returning the original file");
			log.warn(e.getMessage());
		}

		return file;
	}

	public static String getExtension(String name) {
		if (name == null || "".equals(name)) {
			return null;
		}
		return FilenameUtils.getExtension(name);
	}

}
