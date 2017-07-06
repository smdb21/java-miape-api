package org.proteored.miapeapi.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import gnu.trove.map.hash.THashMap;
import sun.net.www.protocol.ftp.FtpURLConnection;

public class URLValidator {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static Map<String, Boolean> validatedURLs = new THashMap<String, Boolean>();

	/**
	 * This function validates the URL. If the url is a file ("file://") and the
	 * file doesn't exist, return false. If the url is a remote URL, then check
	 * if the response is "Not found";
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateURL(String urlString) {
		if (validatedURLs.containsKey(urlString))
			return validatedURLs.get(urlString);
		try {
			URL url = new URL(urlString);
			final boolean validateURL = validateURL(url);
			validatedURLs.put(urlString, validateURL);
			return validateURL;
		} catch (MalformedURLException e) {
			log.debug(e.getMessage());
		}
		return false;
	}

	/**
	 * This function validates the URL. If the url is a file ("file://") and the
	 * file doesn't exist, return false. If the url is a remote URL, then check
	 * if the response is "Not found";
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateURL(URL url) {
		if (url != null && validatedURLs.containsKey(url.toString()))
			return validatedURLs.get(url.toString());
		boolean isValid = isValidURL(url);
		if (!isValid) {
			log.info("Trying URL connection: " + url);
			isValid = isValidURL(url);
		}
		validatedURLs.put(url.toString(), isValid);
		return isValid;
	}

	private static boolean isValidURL(URL url) {
		if (url == null)
			return false;
		String urlFile = url.getFile();

		// if it is a file:// protocol
		if (url.getProtocol().equals("file")) {
			try {
				urlFile = URLDecoder.decode(urlFile, "UTF-8");
			} catch (UnsupportedEncodingException e) {

			}
			final File file = new File(urlFile);
			if (!file.exists())
				return false;
			return true;
		}

		try {
			URLConnection uc = url.openConnection();
			int responseCode = -1;
			if (uc instanceof HttpURLConnection) {
				HttpURLConnection huc = (HttpURLConnection) uc;
				huc.setRequestMethod("GET");
				huc.connect();
				responseCode = huc.getResponseCode();
				if (responseCode != 200)
					return false;
			} else if (uc instanceof FtpURLConnection) {
				return checkFTPConnection(url);

			}
			log.info("Response code: " + responseCode);
			if (responseCode != HttpURLConnection.HTTP_OK)
				return false;

		} catch (IOException e) {
			e.printStackTrace();
			log.info(url + " not reachable. Discarded: " + e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		return true;
	}

	private static boolean checkFTPConnection(URL url) throws IOException {
		log.info("Checking FTP URL: " + url);
		String userName = null;
		String password = null;
		if (url.getUserInfo() != null) {
			if (url.getUserInfo().contains(":")) {
				String[] split = url.getUserInfo().split(":");
				userName = split[0];
				password = split[1];
			} else {
				userName = url.getUserInfo();
			}
		}
		FTPClient ftp = new FTPClient();
		try {
			ftp.setConnectTimeout(5000);
			ftp.setDefaultTimeout(5000);
			ftp.setDataTimeout(5000);
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			log.info("Getting connection");
			ftp.connect(url.getHost());
			log.info("Connection received");

			// After connection attempt, you should check the reply code
			// to verify success.
			int reply = ftp.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {

				boolean logged = true;
				if (userName != null)
					logged = ftp.login(userName, password);

				if (logged) {
					// ftp.enterRemotePassiveMode();
					log.info("getting inputstream");
					InputStream inputStream = null;
					try {
						inputStream = ftp.retrieveFileStream(url.getFile());
						if (inputStream != null) {
							log.info("getting first byte");
							int firstByte = inputStream.read();
							log.info("first byte received");
							if (firstByte != -1) {
								return true;
							} else {
								log.info("Not valid url");
							}
						}
					} finally {
						if (inputStream != null)
							inputStream.close();
					}
				} else {
					ftp.logout();
				}
			}
		} finally {
			try {
				ftp.logout();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;

	}

	public static void main(String[] args) {
		String url = "ftp://pme6:pme6@estrellapolar.cnb.csic.es/data/INIBIC/PME6_INIBIC_rep";
		String post = "_T2D.rar";
		for (int j = 0; j < 30; j++) {
			for (int num = 1; num < 4; num++) {
				boolean valid = URLValidator.validateURL(url + num + post);
				if (!valid)
					System.out.println(valid);
			}
		}
	}
}
