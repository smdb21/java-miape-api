package org.proteored.miapeapi.xml.xtandem;

import org.junit.Test;
import org.proteored.miapeapi.cv.LocalOboControlVocabularyManager;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.xml.xtandem.msi.MiapeMsiDocumentImpl;
import org.xml.sax.SAXException;

import de.proteinms.xtandemparser.xtandem.XTandemFile;

public class XTandemParser {
	@Test
	public void xTandemTest() {
		final String path2XtandemFile = "C:\\Users\\Salva\\Workspaces\\EclipseClassic\\miape-api\\src\\test\\resources\\test.tandem";

		try {

			XTandemFile xfile = new XTandemFile(path2XtandemFile);

			MiapeMSIDocument miapeMSI = new MiapeMsiDocumentImpl(xfile,
					new LocalOboControlVocabularyManager(), xfile.getFileName(), "project name");
			System.out.println(miapeMSI.toXml());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void xTandemTest2() {
		final String path2XtandemFile = "C:\\Users\\Salva\\Workspaces\\EclipseClassic\\miape-api\\src\\test\\resources\\output.2009_05_01_10_20_52.t.xml";

		try {

			XTandemFile xfile = new XTandemFile(path2XtandemFile);

			MiapeMSIDocument miapeMSI = new MiapeMsiDocumentImpl(xfile,
					new LocalOboControlVocabularyManager(), xfile.getFileName(), "project name");
			System.out.println(miapeMSI.toXml());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
