package org.proteored.miapeapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLDownloader {
	public static String downloadURL(URL url) throws IOException {

		URL encodedURL = new URL(URLParamEncoder.encode(url.toString()));
		// Get the input stream through URL Connection
		URLConnection con = encodedURL.openConnection();
		InputStream is = con.getInputStream();

		// Once you have the Input Stream, it's just plain old Java IO stuff.

		// For this case, since you are interested in getting plain-text web
		// page
		// I'll use a reader and output the text content to System.out.

		// For binary content, it's better to directly read the bytes from
		// stream and write
		// to the target file.

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;

		StringBuffer sb = new StringBuffer();
		// read each line and write to the return buffer
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}
