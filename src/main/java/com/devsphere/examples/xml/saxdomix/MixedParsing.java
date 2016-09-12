/*
 * Copyright (c) 2000-2002 Devsphere.com. All rights reserved.
 *
 * The source code and documentation are provided for reference purposes and
 * to modify and recompile the code if necessary. You may not redistribute 
 * the source code or documentation without prior written permission of 
 * Devsphere. For written permission, please contact info@devsphere.com
 *
 * Redistribution and use of the xmltaglib.jar, saxdomix.jar, dslogging.jar,
 * OutputXML.tld and ProcessXML.tld files, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. The redistribution must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 2. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by Devsphere (www.devsphere.com)"
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 3. The names "SAXDOMIX", "XJTL" and "Devsphere" must not be used to 
 * endorse or promote products derived from this software without prior 
 * written permission of Devsphere.
 *
 * 4. Products derived from this software may not be called "Devsphere",
 * nor may "Devsphere" appear in their name, without prior written
 * permission of Devsphere.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DEVSPHERE OR ITS DEVELOPERS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.devsphere.examples.xml.saxdomix;

import com.devsphere.xml.saxdomix.SDXBuilder;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;

import java.util.Vector;

/**
 * This example uses <CODE>SDXBuilder</CODE> and <CODE>SDXController</CODE>
 * to do mixed SAX-DOM parsing.
 *
 * <P>The command line takes as arguments the system ID of the XML document that
 * must be parsed, an optional <CODE>-validation</CODE> switch and the list of
 * elements for which <CODE>wantDOM()</CODE> will return true.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilder
 * @see com.devsphere.xml.saxdomix.SDXController
 * @see com.devsphere.examples.xml.saxdomix.SAXOutputter
 * @see com.devsphere.examples.xml.saxdomix.DOMOutputter
 */
public class MixedParsing {
    /** <CODE>SAXOutputter</CODE> used as handler */
    protected SAXOutputter handler;

    /** <CODE>DOMOutputter</CODE> used as controller */
    protected DOMOutputter controller;

    /**
     * Does the initialization
     */
    public MixedParsing(Vector elements) {
        handler = new SAXOutputter();
        controller = new DOMOutputter(elements) {
            public void handleDOM(Element element) throws SAXException {
                setLevel(handler.getLevel());
                super.handleDOM(element);
            }
        };
    }

    /**
     * Parses the document whose system ID is passed as parameter
     */
    public void parse(String systemID, boolean validation)
            throws IOException, SAXException, ParserConfigurationException {
        // Create the builder that provides the mixed parsing support
        ContentHandler builder = new SDXBuilder(handler, controller);

        // Create and configure a JAXP parser factory
        SAXParserFactory jaxpFactory = SAXParserFactory.newInstance();
        jaxpFactory.setValidating(validation);
        jaxpFactory.setNamespaceAware(true);

        // Create and configure a SAX parser
        SAXParser saxParser = jaxpFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(builder);
        xmlReader.setProperty(
            "http://xml.org/sax/properties/lexical-handler", builder);
        xmlReader.setErrorHandler(handler);

        // Do the parsing
        xmlReader.parse(systemID);
    }

    /**
     * The example's main method
     */
    public static void main(String args[]) {
        if (args.length == 0) {
            // Print the syntax of the command line and exit
            System.out.println(
                "java com.devsphere.examples.xml.saxdomix.MixedParsing"
                    + " systemID [-validation] element1 element2 element3 ...");
            System.exit(1);
        }

        // Get the path of the XML document
        String systemID = args[0];

        // Get the validation flag
        boolean validation = false;
        if (args.length > 1 && args[1].equals("-validation"))
            validation = true;

        // Get the names of the elements that can be roots for the DOM sub-tree
        Vector elements = new Vector();
        for (int i = validation ? 2 : 1; i < args.length; i++)
            elements.addElement(args[i]);

        // Parse the document mixing SAX and DOM
        try {
            MixedParsing example = new MixedParsing(elements);
            example.parse(systemID, validation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
