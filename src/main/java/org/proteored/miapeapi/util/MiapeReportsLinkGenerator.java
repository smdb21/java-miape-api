package org.proteored.miapeapi.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.soap.encoding.Hex;

public class MiapeReportsLinkGenerator {
	private static String wkServidorName = "estrellapolar.cnb.csic.es/proteored/MIAPE/";

	private static String getHashCode(int idUsuario, int miapeID) {
		String toEncode = idUsuario + "" + miapeID;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");

			md.update(toEncode.getBytes("UTF-8")); // Change this to "UTF-16" if
													// needed
			byte[] digest = md.digest();

			String code = new String(Hex.encode(digest));
			return code;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URL getMiapePublicLink(int idUsuario, int miapeID,
			String miapeType) {
		String wkCodigoAcceso = getHashCode(idUsuario, miapeID);
		String link = "http://" + wkServidorName + "MIAPE_" + miapeType
				+ ".asp?pmCodigoAcceso=" + wkCodigoAcceso + "&pmIDUsuario="
				+ idUsuario + "&pmId=" + miapeID;

		try {
			return new URL(link);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
